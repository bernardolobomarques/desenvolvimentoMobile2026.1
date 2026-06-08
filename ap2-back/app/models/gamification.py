from datetime import date
from sqlalchemy import Date, Integer, SmallInteger, Text
from sqlalchemy.orm import Mapped, mapped_column
from app.database import Base


class Gamification(Base):
    __tablename__ = "gamification"

    id: Mapped[int] = mapped_column(Integer, primary_key=True, autoincrement=True)
    xp_total: Mapped[int] = mapped_column(Integer, default=0, server_default="0")
    nivel: Mapped[int] = mapped_column(SmallInteger, default=1, server_default="1")
    streak: Mapped[int] = mapped_column(SmallInteger, default=0, server_default="0")
    ultimo_dia: Mapped[date | None] = mapped_column(Date, nullable=True)
    badges: Mapped[str] = mapped_column(Text, default="[]", server_default="'[]'")
