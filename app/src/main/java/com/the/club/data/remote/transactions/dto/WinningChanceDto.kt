package com.the.club.data.remote.transactions.dto

import com.google.gson.annotations.SerializedName
import com.the.club.domain.model.transactions.WinningChance

data class WinningChanceDto(
    @SerializedName("campaign_id")
    val campaignId: Int,
    @SerializedName("campaign_name")
    val campaignName: String,
    @SerializedName("client_id")
    val clientId: Int,
    @SerializedName("id")
    val id: Int,
    @SerializedName("reference")
    val reference: Int,
    @SerializedName("reference_type")
    val referenceType: Int,
    @SerializedName("value")
    val value: Int
)

fun WinningChanceDto.toWinningChance() = WinningChance(
    campaignId = this.campaignId,
    campaignName = this.campaignName,
    chance = this.value
)