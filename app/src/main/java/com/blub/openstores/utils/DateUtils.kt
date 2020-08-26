package com.blub.openstores.utils

import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

open class DateUtils @Inject constructor() {
    fun withinHours(nextDay: Boolean, start: String, end: String): Boolean {
        val now = getCurrentTime()

        return if (nextDay) {
            end != "24:00" &&
                    start > end &&
                    now < end
        } else {
            now > start && now < end
        }
    }

    open fun getCurrentTime(): String {
        return SimpleDateFormat("HH:mm", Locale.US).format(Date())
    }

    open fun getDayOfWeek(): Int {
        return Calendar.getInstance().get(Calendar.DAY_OF_WEEK)
    }
}