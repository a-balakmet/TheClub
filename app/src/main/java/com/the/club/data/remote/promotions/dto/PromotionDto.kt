package com.the.club.data.remote.promotions.dto

import com.google.gson.annotations.SerializedName
import com.the.club.domain.model.promos.Promotion

data class PromotionDto(
    @SerializedName("id")
    val id: Long,
    @SerializedName("image_url")
    val imageUrl: String,
    @SerializedName("short_description")
    val shortDescription: String,
    @SerializedName("start")
    val start: String,
    @SerializedName("stop")
    val stop: String,
    @SerializedName("title")
    val title: String
)

fun PromotionDto.toPromotion() = Promotion(id = this.id, imageUrl = this.imageUrl)