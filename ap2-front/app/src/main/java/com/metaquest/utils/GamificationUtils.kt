package com.metaquest.utils

object GamificationUtils {
    fun calcularNivel(xp: Int): Int = when {
        xp >= 200 -> 4
        xp >= 100 -> 3
        xp >= 50  -> 2
        else      -> 1
    }

    fun xpParaProximoNivel(xp: Int): Int = when {
        xp >= 200 -> 0
        xp >= 100 -> 200 - xp
        xp >= 50  -> 100 - xp
        else      -> 50 - xp
    }

    fun progressoNivel(xp: Int): Int {
        val (base, teto) = when {
            xp >= 200 -> 200 to 200
            xp >= 100 -> 100 to 200
            xp >= 50  -> 50 to 100
            else      -> 0 to 50
        }
        if (teto == base) return 100
        return ((xp - base) * 100 / (teto - base)).coerceIn(0, 100)
    }
}
