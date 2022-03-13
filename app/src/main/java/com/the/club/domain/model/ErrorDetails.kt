package com.the.club.domain.model

import com.google.gson.annotations.SerializedName

data class ErrorDetails(
    @SerializedName("error_code")
    val errorCode: String,
    @SerializedName("error_message")
    val errorMessage: String
)
