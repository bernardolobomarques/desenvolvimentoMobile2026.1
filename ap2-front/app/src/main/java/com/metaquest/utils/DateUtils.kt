package com.metaquest.utils

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.concurrent.TimeUnit

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

    fun fromCalendar(year: Int, month: Int, day: Int): String {
        val cal = Calendar.getInstance()
        cal.set(year, month, day)
        return isoFormat.format(cal.time)
    }

    fun toDisplay(isoDate: String?): String {
        if (isoDate == null) return "Sem prazo"
        return runCatching { displayFormat.format(isoFormat.parse(isoDate)!!) }.getOrDefault(isoDate)
    }

    /** Returns a human-readable deadline label with urgency context. */
    fun prazoLabel(isoDate: String?): String {
        if (isoDate == null) return "Sem prazo"
        val deadline = runCatching { isoFormat.parse(isoDate)!! }.getOrNull() ?: return toDisplay(isoDate)
        val todayCal = Calendar.getInstance().apply { set(Calendar.HOUR_OF_DAY, 0); set(Calendar.MINUTE, 0); set(Calendar.SECOND, 0); set(Calendar.MILLISECOND, 0) }
        val deadlineCal = Calendar.getInstance().apply { time = deadline; set(Calendar.HOUR_OF_DAY, 0); set(Calendar.MINUTE, 0); set(Calendar.SECOND, 0); set(Calendar.MILLISECOND, 0) }
        val diff = TimeUnit.MILLISECONDS.toDays(deadlineCal.timeInMillis - todayCal.timeInMillis)
        return when {
            diff < 0  -> "Vencida há ${-diff} ${if (-diff == 1L) "dia" else "dias"}"
            diff == 0L -> "Vence hoje!"
            diff == 1L -> "Vence amanhã"
            diff <= 7  -> "$diff dias restantes"
            else       -> toDisplay(isoDate)
        }
    }

    /** Returns negative if overdue, 0 if today, positive for days remaining. */
    fun daysRemaining(isoDate: String?): Long? {
        if (isoDate == null) return null
        val deadline = runCatching { isoFormat.parse(isoDate)!! }.getOrNull() ?: return null
        val todayCal = Calendar.getInstance().apply { set(Calendar.HOUR_OF_DAY, 0); set(Calendar.MINUTE, 0); set(Calendar.SECOND, 0); set(Calendar.MILLISECOND, 0) }
        val deadlineCal = Calendar.getInstance().apply { time = deadline; set(Calendar.HOUR_OF_DAY, 0); set(Calendar.MINUTE, 0); set(Calendar.SECOND, 0); set(Calendar.MILLISECOND, 0) }
        return TimeUnit.MILLISECONDS.toDays(deadlineCal.timeInMillis - todayCal.timeInMillis)
    }
}
