package com.the.club.domain.model.promos

data class PromotionDetails(
    val id: Long,
    val title: String,
    val start: String,
    val stop: String,
    val endDate: Long,
    val shortDescription: String,
    val fullDescription: String,
    val imageUrl: String,
    val conditionsShort: String,
    val conditionsFull: String,
    val downloadLink: String
)
