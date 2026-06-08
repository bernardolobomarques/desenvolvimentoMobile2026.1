"""initial schema

Revision ID: 001
Revises:
Create Date: 2026-06-08
"""
from typing import Sequence, Union

from alembic import op
import sqlalchemy as sa

revision: str = "001"
down_revision: Union[str, None] = None
branch_labels: Union[str, Sequence[str], None] = None
depends_on: Union[str, Sequence[str], None] = None


def upgrade() -> None:
    op.create_table(
        "goals",
        sa.Column("id", sa.Integer(), autoincrement=True, nullable=False),
        sa.Column("titulo", sa.String(150), nullable=False),
        sa.Column("descricao", sa.Text(), nullable=True),
        sa.Column("categoria", sa.String(50), nullable=True),
        sa.Column("prioridade", sa.String(20), nullable=True),
        sa.Column("prazo", sa.Date(), nullable=True),
        sa.Column("concluida", sa.Boolean(), server_default="false", nullable=False),
        sa.Column("progresso", sa.SmallInteger(), server_default="0", nullable=False),
        sa.Column("xp", sa.SmallInteger(), server_default="10", nullable=True),
        sa.Column("origem", sa.String(30), nullable=True),
        sa.Column("criada_em", sa.DateTime(), server_default=sa.func.now(), nullable=True),
        sa.CheckConstraint("progresso BETWEEN 0 AND 100", name="ck_goals_progresso"),
        sa.PrimaryKeyConstraint("id"),
    )
    op.create_table(
        "gamification",
        sa.Column("id", sa.Integer(), autoincrement=True, nullable=False),
        sa.Column("xp_total", sa.Integer(), server_default="0", nullable=False),
        sa.Column("nivel", sa.SmallInteger(), server_default="1", nullable=False),
        sa.Column("streak", sa.SmallInteger(), server_default="0", nullable=False),
        sa.Column("ultimo_dia", sa.Date(), nullable=True),
        sa.Column("badges", sa.Text(), server_default="'[]'", nullable=False),
        sa.PrimaryKeyConstraint("id"),
    )


def downgrade() -> None:
    op.drop_table("gamification")
    op.drop_table("goals")
