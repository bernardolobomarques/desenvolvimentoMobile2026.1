from fastapi import APIRouter, Depends, status
from sqlalchemy.orm import Session
from app.database import get_db
from app.schemas.gamification import GamificationSummary
from app.services.gamification_service import GamificationService

router = APIRouter(prefix="/gamificacao", tags=["Gamificacao"])


@router.get("/resumo", response_model=GamificationSummary)
def resumo(db: Session = Depends(get_db)):
    return GamificationService(db).get_summary()


@router.post("/inicializar", response_model=GamificationSummary, status_code=status.HTTP_201_CREATED)
def inicializar(db: Session = Depends(get_db)):
    svc = GamificationService(db)
    svc.inicializar()
    return svc.get_summary()
