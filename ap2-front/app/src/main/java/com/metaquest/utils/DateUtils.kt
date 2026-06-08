package com.metaquest.utils

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

object DateUtils {
    private val isoFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    private val displayFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

    fun today(): String = isoFormat.format(Calendar.getInstance().time)

    fun addDays(isoDate: String, days: Int): String {
        val cal = Calendar.getInstance()
        cal.time = isoFormat.parse(isoDate) ?: cal.time
        cal.add(Calendar.DAY_OF_YEAR, days)
        return isoFormat.format(cal.time)
    }

    fun toDisplay(isoDate: String?): String {
        if (isoDate == null) return "Sem prazo"
        return runCatching { displayFormat.format(isoFormat.parse(isoDate)!!) }.getOrDefault(isoDate)
    }
}
