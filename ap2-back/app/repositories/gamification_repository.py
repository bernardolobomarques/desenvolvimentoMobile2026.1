from typing import Optional
from sqlalchemy.orm import Session
from app.models.gamification import Gamification


class GamificationRepository:
    def __init__(self, db: Session):
        self.db = db

    def get(self) -> Optional[Gamification]:
        return self.db.query(Gamification).first()

    def create_default(self) -> Gamification:
        profile = Gamification()
        self.db.add(profile)
        self.db.commit()
        self.db.refresh(profile)
        return profile

    def save(self, profile: Gamification) -> Gamification:
        self.db.commit()
        self.db.refresh(profile)
        return profile
