package com.the.club.domain.model.shops

data class NearestShopsRequest(
    val k: Int,
    val latitude: Double,
    val longitude: Double
)
