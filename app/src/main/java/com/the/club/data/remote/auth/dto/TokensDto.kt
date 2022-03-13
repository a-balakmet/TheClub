package com.the.club.data.remote.auth.dto

import com.google.gson.annotations.SerializedName

data class TokensDto(
    @SerializedName("auth_token")
    val authToken: String,
    @SerializedName("refresh_token")
    val refreshToken: String
)