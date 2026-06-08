import json
from datetime import date
from sqlalchemy.orm import Session
from app.models.gamification import Gamification
from app.repositories.gamification_repository import GamificationRepository
from app.schemas.gamification import GamificationSummary


XP_CRIAR = 10
XP_CONCLUIR = 20
XP_PROGRESSO = 5
XP_STREAK_BONUS = 10

NIVEL_THRESHOLDS = [(200, 4), (100, 3), (50, 2), (0, 1)]


def _calcular_nivel(xp: int) -> int:
    for threshold, nivel in NIVEL_THRESHOLDS:
        if xp >= threshold:
            return nivel
    return 1


def _get_badges(profile: Gamification) -> list[str]:
    return json.loads(profile.badges)


def _set_badges(profile: Gamification, badges: list[str]) -> None:
    profile.badges = json.dumps(badges)


def _add_badge(profile: Gamification, badge: str) -> None:
    badges = _get_badges(profile)
    if badge not in badges:
        badges.append(badge)
        _set_badges(profile, badges)


class GamificationService:
    def __init__(self, db: Session):
        self.repo = GamificationRepository(db)

    def _get_or_create(self) -> Gamification:
        profile = self.repo.get()
        if not profile:
            profile = self.repo.create_default()
        return profile

    def inicializar(self) -> Gamification:
        return self._get_or_create()

    def get_summary(self) -> GamificationSummary:
        profile = self._get_or_create()
        return GamificationSummary(
            xp_total=profile.xp_total,
            nivel=profile.nivel,
            streak=profile.streak,
            badges=_get_badges(profile),
        )

    def on_goal_created(self, prioridade: str, active_count: int) -> None:
        profile = self._get_or_create()
        profile.xp_total += XP_CRIAR
        _add_badge(profile, "primeira_meta")
        if active_count >= 3:
            _add_badge(profile, "tres_metas_ativas")
        self._update_streak(profile)
        profile.nivel = _calcular_nivel(profile.xp_total)
        self.repo.save(profile)

    def on_goal_concluded(self, prioridade: str) -> None:
        profile = self._get_or_create()
        profile.xp_total += XP_CONCLUIR
        _add_badge(profile, "primeira_conclusao")
        if prioridade == "alta":
            _add_badge(profile, "meta_alta_prioridade")
        self._update_streak(profile)
        profile.nivel = _calcular_nivel(profile.xp_total)
        self.repo.save(profile)

    def on_progress_updated(self) -> None:
        profile = self._get_or_create()
        profile.xp_total += XP_PROGRESSO
        self._update_streak(profile)
        profile.nivel = _calcular_nivel(profile.xp_total)
        self.repo.save(profile)

    def _update_streak(self, profile: Gamification) -> None:
        today = date.today()
        if profile.ultimo_dia is None:
            profile.streak = 1
        elif profile.ultimo_dia == today:
            return
        else:
            diff = (today - profile.ultimo_dia).days
            if diff == 1:
                profile.streak += 1
                profile.xp_total += XP_STREAK_BONUS
                if profile.streak >= 3:
                    _add_badge(profile, "streak_3_dias")
            else:
                profile.streak = 1
        profile.ultimo_dia = today
