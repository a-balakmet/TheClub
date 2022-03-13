package com.the.club.data.remote.transactions.dto

import com.google.gson.annotations.SerializedName
import com.the.club.common.ktx.formatWithThousand
import com.the.club.domain.model.transactions.TransactionProduct

data class TransactionProductDto(
    @SerializedName("name")
    val name: String,
    @SerializedName("quantity")
    val quantity: Double,
    @SerializedName("reference")
    val reference: String,
    @SerializedName("total_price")
    val totalPrice: Int
)

fun TransactionProductDto.toTransactionProducts() = TransactionProduct(
    name = this.name,
    quantity = this.quantity,
    sum = "${this.totalPrice.formatWithThousand()} тг"
)