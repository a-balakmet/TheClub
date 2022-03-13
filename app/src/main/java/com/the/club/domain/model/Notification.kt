package com.the.club.domain.model

data class Notification(
    val date: String,
    val time: String,
    val message: String,
    val title: String,
    val link: String?
)
