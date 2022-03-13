package com.the.club.data.remote.transactions.dto

import com.google.gson.annotations.SerializedName
import com.the.club.common.ktx.DD_MM_YYYY
import com.the.club.common.ktx.toString
import com.the.club.domain.model.transactions.Burning
import java.util.*

data class BurningDto(
    @SerializedName("date_burn")
    val burningDate: Date,
    @SerializedName("remaining_amount_sum")
    val burningSum: Int
)

fun BurningDto.toBurning() = Burning(
    burningDate = burningDate.toString(pattern = DD_MM_YYYY),
    burningSum = burningSum
)