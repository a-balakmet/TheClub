package com.the.club.common.ktx

import ru.tinkoff.decoro.Mask
import ru.tinkoff.decoro.MaskImpl
import ru.tinkoff.decoro.slots.PredefinedSlots

private val phoneRegex = ("[\\s ()-/+]").toRegex()

fun String.toSmsLength(): String = when (this.length) {
    0 -> "----"
    1 -> "$this---"
    2 -> "$this--"
    3 -> "$this-"
    else -> this
}

fun mergeNumber(cardNumber: String, cvs: String): String = StringBuilder()
    .append(cardNumber)
    .append(cvs)
    .toString()

fun String.toChunked(divider: Int) =
    this.chunked(divider).toString()
        .replace("[", "")
        .replace(",", "")
        .replace("]", "")

fun String.toPhoneFormat(): String {
    val mask: Mask = MaskImpl(PredefinedSlots.RUS_PHONE_NUMBER, false)
    mask.insertFront(this)
    return mask.toString()
}

fun String.toClearPhone(): String {
    return this.replace(phoneRegex, "")
}

fun Int.formatWithThousand(): String {
    val result = StringBuilder()
    val size = this.toString().length
    return if (size > 3) {
        for (i in size - 1 downTo 0) {
            result.insert(0, this.toString()[i])
            if ((i != size - 1) && i != 0 && (size - i) % 3 == 0)
                result.insert(0, " ")
        }
        result.toString()
    } else
        this.toString()
}

fun Long.formatWithThousand(): String {
    val result = StringBuilder()
    val size = this.toString().length
    return if (size > 3) {
        for (i in size - 1 downTo 0) {
            result.insert(0, this.toString()[i])
            if ((i != size - 1) && i != 0 && (size - i) % 3 == 0)
                result.insert(0, " ")
        }
        result.toString()
    } else
        this.toString()
}

fun Int.toHours(closeHour: Int): String {
    val start = if (this < 10) "0$this:00" else "$this:00"
    val end = if (closeHour < 10) "0$closeHour:00" else "$closeHour:00"
    return "$start - $end"
}
