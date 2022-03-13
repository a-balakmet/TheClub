package com.the.club.domain.model

import java.util.*

data class Survey(
    val id: Int,
    val title: String,
    val dateStart: Date,
    val dateEnd: Date
)
