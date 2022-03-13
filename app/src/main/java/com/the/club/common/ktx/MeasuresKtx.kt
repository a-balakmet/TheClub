package com.the.club.common.ktx

import android.content.res.Resources
import android.util.DisplayMetrics
import java.util.*
import kotlin.math.roundToInt

fun Int.toTimeToClose(closeHour: Int): List<Int> {
    val diffHour: Int
    val diffMin: Int
    var endHour = closeHour
    if (endHour < this) {
        endHour = closeHour + 24
    }
    if (endHour == 0) {
        endHour = 24
    }
    val now = Date()
    val nowCal = Calendar.getInstance()
    nowCal.time = now
    val nowHour = nowCal[Calendar.HOUR_OF_DAY]
    val nowMin = nowCal[Calendar.MINUTE]
    diffHour = endHour - nowHour-1
    diffMin = 60 - nowMin
    return listOf(diffHour, diffMin)
}