package com.the.club.data.remote.bonusCards.dto

import android.graphics.Bitmap
import com.google.gson.annotations.SerializedName
import com.the.club.common.BarcodeCreator
import com.the.club.common.ktx.mergeNumber
import com.the.club.common.ktx.toChunked
import com.the.club.domain.model.BonusCard

data class BonusCardDto(
    @SerializedName("balance")
    val balance: Balance,
    @SerializedName("card_number")
    val cardNumber: Long,
    @SerializedName("cvs")
    val cvs: String,
    @SerializedName("dt_created")
    val dateCreated: String,
    @SerializedName("dt_expired")
    val dateExpired: String,
    @SerializedName("dt_registered")
    val dateRegistered: String,
    @SerializedName("id")
    val id: Long,
    @SerializedName("is_virtual")
    val isVirtual: Boolean,
    @SerializedName("status")
    val status: String,
    @SerializedName("type")
    val type: Type?
)

fun BonusCardDto.toBonusCard() = BonusCard(
    id = id,
    cardNumber = ("$cardNumber***").toChunked(4),
    //cardNumber = cardNumber.toString(),
    cvs = cvs,
    balance = balance.bonuses,
    barcode = mergeNumber(cardNumber.toString(), cvs).toBitmap(),
    type = type?.id ?: 1,
    isActive = setBonusCardStatus(status),
    status = if (setBonusCardStatus(status)) 0 else 1
)

private fun setBonusCardStatus(status: String): Boolean =
    when (status) {
        "active" -> true
        "blocked_by_user" -> false
        "blocked_by_fraud" -> false
        "blocked_by_manager" -> false
        else -> true
    }

fun String.toBitmap(): Bitmap? = BarcodeCreator.generateBonusCardBarcode(this)