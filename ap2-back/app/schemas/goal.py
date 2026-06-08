from datetime import date, datetime
from typing import Optional
from pydantic import BaseModel, Field


class GoalCreate(BaseModel):
    titulo: str = Field(..., max_length=150)
    descricao: Optional[str] = None
    categoria: Optional[str] = None
    prioridade: Optional[str] = None
    prazo: Optional[date] = None
    progresso: int = Field(0, ge=0, le=100)
    xp: int = Field(10, ge=0)
    origem: Optional[str] = None


class GoalUpdate(BaseModel):
    titulo: Optional[str] = Field(None, max_length=150)
    descricao: Optional[str] = None
    categoria: Optional[str] = None
    prioridade: Optional[str] = None
    prazo: Optional[date] = None
    progresso: Optional[int] = Field(None, ge=0, le=100)
    xp: Optional[int] = None
    origem: Optional[str] = None


class GoalProgressUpdate(BaseModel):
    progresso: int = Field(..., ge=0, le=100)


class GoalResponse(BaseModel):
    id: int
    titulo: str
    descricao: Optional[str]
    categoria: Optional[str]
    prioridade: Optional[str]
    prazo: Optional[date]
    concluida: bool
    progresso: int
    xp: int
    origem: Optional[str]
    criada_em: Optional[datetime]

    model_config = {"from_attributes": True}
