from typing import List
from pydantic import BaseModel


class GamificationSummary(BaseModel):
    xp_total: int
    nivel: int
    streak: int
    badges: List[str]

    model_config = {"from_attributes": True}
