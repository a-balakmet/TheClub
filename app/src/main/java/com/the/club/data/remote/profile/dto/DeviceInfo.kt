package com.the.club.data.remote.profile.dto

import com.google.gson.annotations.SerializedName

data class DeviceInfo(
    @SerializedName("app_build")
    val appBuild: String,
    @SerializedName("app_version")
    val appVersion: String,
    @SerializedName("device_id")
    val deviceId: String,
    @SerializedName("manufacturer")
    val manufacturer: String,
    @SerializedName("model")
    val model: String,
    @SerializedName("os_version")
    val osVersion: String,
    @SerializedName("platform")
    val platform: String,
    @SerializedName("push_token")
    val pushToken: String
)
