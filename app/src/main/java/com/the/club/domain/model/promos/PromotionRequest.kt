package com.the.club.domain.model.promos

data class PromotionRequest(
    val page: Int,
    val size: Int,
    val sort: String
)
