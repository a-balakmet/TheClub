package com.the.club.data.remote.transactions.dto

import com.google.gson.annotations.SerializedName
import com.the.club.common.ktx.DD_MM_YYYY
import com.the.club.common.ktx.HH_MM_SS
import com.the.club.common.ktx.toString
import com.the.club.domain.model.transactions.Transaction
import java.util.*

data class TransactionDto(
    @SerializedName("accrued_bonuses")
    val accruedBonuses: Int,
    @SerializedName("actor_id")
    val actorId: Long,
    @SerializedName("card_id")
    val cardId: Long,
    @SerializedName("check_amount")
    val checkAmount: Long,
    @SerializedName("description")
    val description: String,
    @SerializedName("dt_created")
    val dateCreated: Date,
    @SerializedName("id")
    val id: Long,
    @SerializedName("parent")
    val parent: Int,
    @SerializedName("reference")
    val reference: String,
    @SerializedName("type")
    val type: String,
    @SerializedName("withdrawal_bonuses")
    val withdrawalBonuses: Int
)

fun TransactionDto.toTransaction() = Transaction(
    id = this.id,
    accruedBonuses = this.accruedBonuses,
    actorId = this.actorId,
    cardId = this.cardId,
    checkAmount = this.checkAmount,
    date = this.dateCreated.toString(pattern = DD_MM_YYYY),
    time = this.dateCreated.toString(pattern = HH_MM_SS),
    type = this.type,
    withdrawalBonuses = this.withdrawalBonuses
)