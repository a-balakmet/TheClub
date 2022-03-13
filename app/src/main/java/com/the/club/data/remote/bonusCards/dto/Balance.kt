package com.the.club.data.remote.bonusCards.dto

import com.google.gson.annotations.SerializedName

data class Balance(
    @SerializedName("accrued_blocked_bonuses")
    val accruedBlockedBonuses: Int,
    @SerializedName("bonuses")
    val bonuses: Long,
    @SerializedName("withdrawal_blocked_bonuses")
    val withdrawalBlockedBonuses: Int
)