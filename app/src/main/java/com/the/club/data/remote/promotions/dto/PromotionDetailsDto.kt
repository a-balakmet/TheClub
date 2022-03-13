package com.the.club.data.remote.promotions.dto

import com.google.gson.annotations.SerializedName
import com.the.club.common.ktx.DD_MM_YYYY
import com.the.club.common.ktx.toString
import com.the.club.domain.model.promos.PromotionDetails
import java.util.*

data class PromotionDetailsDto(
    @SerializedName("conditions_full")
    val conditionsFull: String?,
    @SerializedName("conditions_short")
    val conditionsShort: String?,
    @SerializedName("download_link")
    val downloadLink: String?,
    @SerializedName("full_description")
    val fullDescription: String?,
    @SerializedName("id")
    val id: Long,
    @SerializedName("image_url")
    val imageUrl: String,
    @SerializedName("short_description")
    val shortDescription: String,
    @SerializedName("start")
    val start: Date,
    @SerializedName("stop")
    val stop: Date,
    @SerializedName("title")
    val title: String
)

fun PromotionDetailsDto.toPromotionDetails() = PromotionDetails(
    id = this.id,
    title = this.title,
    start = this.start.toString(pattern = DD_MM_YYYY),
    stop = this.stop.toString(pattern = DD_MM_YYYY),
    endDate = this.stop.time,
    shortDescription = this.shortDescription,
    fullDescription = this.fullDescription ?: "",
    imageUrl = this.imageUrl,
    conditionsShort = this.conditionsShort ?: "",
    conditionsFull = this.conditionsFull ?: "",
    downloadLink = this.downloadLink ?: ""
)