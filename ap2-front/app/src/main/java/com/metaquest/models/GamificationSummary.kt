package com.metaquest.models

data class GamificationSummary(
    val xp_total: Int = 0,
    val nivel: Int = 1,
    val streak: Int = 0,
    val badges: List<String> = emptyList()
)
