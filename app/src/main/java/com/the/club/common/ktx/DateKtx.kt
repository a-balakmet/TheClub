package com.the.club.common.ktx

import java.text.SimpleDateFormat
import java.util.*

const val HH_MM_SS = "HH:mm:ss"
const val DD_MM_YYYY = "dd.MM.yyyy"
const val FULL_DATE = "yyyy-MM-dd'T'HH:mm:ss'Z'"

fun Date.toString(pattern: String): String =
    SimpleDateFormat(pattern, Locale.getDefault()).format(this)

fun String.toDate(pattern: String) : Date? =
    SimpleDateFormat(pattern, Locale.getDefault()).parse(this)


fun Date.toFullDate(months: Array<String>): String {
    val date = this.toString(pattern = DD_MM_YYYY).split(".")
    val monthIndex = date[1].toInt()-1
    val monthName = months[monthIndex]
    return "${date[0]} $monthName ${date[2]}"
}
fun String.toFullDate(months: Array<String>): String {
    val date = this.split(".")
    val monthIndex = date[1].toInt()-1
    val monthName = months[monthIndex]
    return "${date[0]} $monthName ${date[2]}"
}

fun String.getHour(): Int {
    val date: Date = this.toDate(FULL_DATE)!!
    val tz = TimeZone.getDefault()
    val calendar = Calendar.getInstance(tz)
    calendar.time = date
    return calendar.get(Calendar.HOUR_OF_DAY)
}

fun getLastDayOfMonth(date: Date): Int {
    val cal = Calendar.getInstance()
    cal.time = date
    return cal.getActualMaximum(Calendar.DAY_OF_MONTH)
}

fun minBirthday(): String {
    val calendar = Calendar.getInstance()
    calendar.time = Date()
    calendar.add(Calendar.YEAR, -14)
    return calendar.time.toString(DD_MM_YYYY)
}

fun minBirthdayFull(): String {
    val calendar = Calendar.getInstance()
    calendar.time = Date()
    calendar.add(Calendar.YEAR, -14)
    return calendar.time.toString(FULL_DATE)
}

fun String.isEnoughYears(): Boolean {
    val birthday = this.toDate(DD_MM_YYYY)!!
    val difference = Date().time - birthday.time
    val years = ((difference / (1000 * 60 * 60 * 24)).toFloat()) / 365
    return years >= 14
}