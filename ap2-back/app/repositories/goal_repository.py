from typing import List, Optional
from sqlalchemy.orm import Session
from app.models.goal import Goal
from app.schemas.goal import GoalCreate, GoalUpdate


class GoalRepository:
    def __init__(self, db: Session):
        self.db = db

    def create(self, data: GoalCreate) -> Goal:
        goal = Goal(**data.model_dump())
        self.db.add(goal)
        self.db.commit()
        self.db.refresh(goal)
        return goal

    def get_all(self) -> List[Goal]:
        return self.db.query(Goal).order_by(Goal.criada_em.desc()).all()

    def get_by_id(self, goal_id: int) -> Optional[Goal]:
        return self.db.query(Goal).filter(Goal.id == goal_id).first()

    def update(self, goal: Goal, data: GoalUpdate) -> Goal:
        for field, value in data.model_dump(exclude_unset=True).items():
            setattr(goal, field, value)
        self.db.commit()
        self.db.refresh(goal)
        return goal

    def update_progress(self, goal: Goal, progresso: int) -> Goal:
        goal.progresso = progresso
        self.db.commit()
        self.db.refresh(goal)
        return goal

    def conclude(self, goal: Goal) -> Goal:
        goal.concluida = True
        goal.progresso = 100
        self.db.commit()
        self.db.refresh(goal)
        return goal

    def delete(self, goal: Goal) -> None:
        self.db.delete(goal)
        self.db.commit()

    def count_active(self) -> int:
        return self.db.query(Goal).filter(Goal.concluida.is_(False)).count()
