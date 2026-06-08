from typing import List
from fastapi import HTTPException, status
from sqlalchemy.orm import Session
from app.models.goal import Goal
from app.repositories.goal_repository import GoalRepository
from app.schemas.goal import GoalCreate, GoalProgressUpdate, GoalResponse, GoalUpdate
from app.services.gamification_service import GamificationService


class GoalService:
    def __init__(self, db: Session):
        self.repo = GoalRepository(db)
        self.gamification = GamificationService(db)

    def create(self, data: GoalCreate) -> GoalResponse:
        goal = self.repo.create(data)
        active_count = self.repo.count_active()
        self.gamification.on_goal_created(data.prioridade or "", active_count)
        return GoalResponse.model_validate(goal)

    def list_all(self) -> List[GoalResponse]:
        return [GoalResponse.model_validate(g) for g in self.repo.get_all()]

    def get_by_id(self, goal_id: int) -> GoalResponse:
        goal = self._get_or_404(goal_id)
        return GoalResponse.model_validate(goal)

    def update(self, goal_id: int, data: GoalUpdate) -> GoalResponse:
        goal = self._get_or_404(goal_id)
        goal = self.repo.update(goal, data)
        return GoalResponse.model_validate(goal)

    def update_progress(self, goal_id: int, data: GoalProgressUpdate) -> GoalResponse:
        goal = self._get_or_404(goal_id)
        goal = self.repo.update_progress(goal, data.progresso)
        self.gamification.on_progress_updated()
        return GoalResponse.model_validate(goal)

    def conclude(self, goal_id: int) -> GoalResponse:
        goal = self._get_or_404(goal_id)
        if goal.concluida:
            raise HTTPException(status_code=status.HTTP_409_CONFLICT, detail="Meta ja concluida")
        goal = self.repo.conclude(goal)
        self.gamification.on_goal_concluded(goal.prioridade or "")
        return GoalResponse.model_validate(goal)

    def delete(self, goal_id: int) -> None:
        goal = self._get_or_404(goal_id)
        self.repo.delete(goal)

    def _get_or_404(self, goal_id: int) -> Goal:
        goal = self.repo.get_by_id(goal_id)
        if not goal:
            raise HTTPException(status_code=status.HTTP_404_NOT_FOUND, detail="Meta nao encontrada")
        return goal
