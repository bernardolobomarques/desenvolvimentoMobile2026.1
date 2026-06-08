from typing import List
from fastapi import APIRouter, Depends, status
from sqlalchemy.orm import Session
from app.database import get_db
from app.schemas.goal import GoalCreate, GoalProgressUpdate, GoalResponse, GoalUpdate
from app.services.goal_service import GoalService

router = APIRouter(prefix="/metas", tags=["Metas"])


@router.post("", response_model=GoalResponse, status_code=status.HTTP_201_CREATED)
def criar_meta(data: GoalCreate, db: Session = Depends(get_db)):
    return GoalService(db).create(data)


@router.get("", response_model=List[GoalResponse])
def listar_metas(db: Session = Depends(get_db)):
    return GoalService(db).list_all()


@router.get("/{goal_id}", response_model=GoalResponse)
def buscar_meta(goal_id: int, db: Session = Depends(get_db)):
    return GoalService(db).get_by_id(goal_id)


@router.put("/{goal_id}", response_model=GoalResponse)
def editar_meta(goal_id: int, data: GoalUpdate, db: Session = Depends(get_db)):
    return GoalService(db).update(goal_id, data)


@router.patch("/{goal_id}/progresso", response_model=GoalResponse)
def atualizar_progresso(goal_id: int, data: GoalProgressUpdate, db: Session = Depends(get_db)):
    return GoalService(db).update_progress(goal_id, data)


@router.patch("/{goal_id}/concluir", response_model=GoalResponse)
def concluir_meta(goal_id: int, db: Session = Depends(get_db)):
    return GoalService(db).conclude(goal_id)


@router.delete("/{goal_id}", status_code=status.HTTP_204_NO_CONTENT)
def excluir_meta(goal_id: int, db: Session = Depends(get_db)):
    GoalService(db).delete(goal_id)
