from datetime import datetime, date
from sqlalchemy import Boolean, CheckConstraint, Date, DateTime, Integer, SmallInteger, String, Text, func
from sqlalchemy.orm import Mapped, mapped_column
from app.database import Base


class Goal(Base):
    __tablename__ = "goals"

    id: Mapped[int] = mapped_column(Integer, primary_key=True, autoincrement=True)
    titulo: Mapped[str] = mapped_column(String(150), nullable=False)
    descricao: Mapped[str | None] = mapped_column(Text)
    categoria: Mapped[str | None] = mapped_column(String(50))
    prioridade: Mapped[str | None] = mapped_column(String(20))
    prazo: Mapped[date | None] = mapped_column(Date)
    concluida: Mapped[bool] = mapped_column(Boolean, default=False, server_default="false")
    progresso: Mapped[int] = mapped_column(
        SmallInteger,
        default=0,
        server_default="0",
        nullable=False,
    )
    xp: Mapped[int] = mapped_column(SmallInteger, default=10, server_default="10")
    origem: Mapped[str | None] = mapped_column(String(30))
    criada_em: Mapped[datetime] = mapped_column(DateTime, server_default=func.now())

    __table_args__ = (
        CheckConstraint("progresso BETWEEN 0 AND 100", name="ck_goals_progresso"),
    )
