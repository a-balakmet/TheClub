package com.the.club.common.model

data class CommonState(
    val isLoading: Boolean = false,
    val emptyBody: EmptyBody? = null,
    val error: String = "")

data class EmptyBody(val result: String = "success")
