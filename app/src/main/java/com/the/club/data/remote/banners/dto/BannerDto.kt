package com.the.club.data.remote.banners.dto

import com.google.gson.annotations.SerializedName
import com.the.club.domain.model.Banner

data class BannerDto(
    @SerializedName("category_id")
    val categoryId: Int,
    @SerializedName("description")
    val description: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("img_link")
    val imgLink: String?,
    @SerializedName("img_link_vertical")
    val imgLinkVertical: String,
    @SerializedName("link")
    val link: String?,
    @SerializedName("title")
    val title: String
)

fun BannerDto.toBanner() = Banner(
    imageUrl = this.imgLink,
    bannerLink = this.link
)