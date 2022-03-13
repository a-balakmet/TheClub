package com.the.club.data.remote.refreshToken.dto

import com.google.gson.annotations.SerializedName

data class AuthTokens(
    @SerializedName("auth_token")
    val authToken: String,
    @SerializedName("refresh_token")
    val refreshToken: String
)