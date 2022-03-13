package com.the.club.data.remote.clubs.dto

import com.google.gson.annotations.SerializedName
import com.the.club.domain.model.clubs.Club

data class ClubDto(
    @SerializedName("description_ml")
    val description: String,
    @SerializedName("document_url_ml")
    val documentUrl: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("image_url_ml")
    val imageUrl: String,
    @SerializedName("is_adult")
    val isAdult: Boolean,
    @SerializedName("merchant_id")
    val merchantId: Int, //
    @SerializedName("name_ml")
    val name: String,
    @SerializedName("reference")
    val reference: String //
)

fun ClubDto.toClientClub() = Club(
    description = this.description,
    documentUrl = this.documentUrl,
    id = this.id,
    imageUrl = this.imageUrl,
    isAdult = this.isAdult,
    name = this.name,
    isMember = true
)

fun ClubDto.toNonClientClub() = Club(
    description = this.description,
    documentUrl = this.documentUrl,
    id = this.id,
    imageUrl = this.imageUrl,
    isAdult = this.isAdult,
    name = this.name,
    isMember = false
)