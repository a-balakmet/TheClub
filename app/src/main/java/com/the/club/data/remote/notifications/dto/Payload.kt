package com.the.club.data.remote.notifications.dto

import com.google.gson.annotations.SerializedName

data class Payload(
    @SerializedName("android_link")
    val androidLink: String?,
    @SerializedName("img_link")
    val imageLink: String?,
    @SerializedName("ios_link")
    val iosLink: String?
)