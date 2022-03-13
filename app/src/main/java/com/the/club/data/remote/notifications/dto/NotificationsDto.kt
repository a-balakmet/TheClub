package com.the.club.data.remote.notifications.dto

import com.google.gson.annotations.SerializedName
import com.the.club.common.ktx.DD_MM_YYYY
import com.the.club.common.ktx.HH_MM_SS
import com.the.club.common.ktx.toString
import com.the.club.domain.model.Notification
import java.util.*

data class NotificationDto(
    @SerializedName("dt_created")
    val dateCreated: Date,
    @SerializedName("message")
    val message: String,
    @SerializedName("payload")
    val payload: Payload?,
    @SerializedName("title")
    val title: String
)

fun NotificationDto.toNotification() = Notification (
    date = dateCreated.toString(pattern = DD_MM_YYYY),
    time = dateCreated.toString(pattern = HH_MM_SS),
    message = message,
    title = title,
    link = payload.getLink()
)

private fun Payload?.getLink(): String? {
    return if (this != null) {
        androidLink?.ifEmpty { null }
    } else null
}
