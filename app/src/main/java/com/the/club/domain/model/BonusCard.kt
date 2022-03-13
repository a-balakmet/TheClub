package com.the.club.domain.model

import android.graphics.Bitmap

data class BonusCard(
    val id: Long,
    val cardNumber: String,
    val cvs: String,
    val balance: Long,
    val barcode: Bitmap?,
    val type: Int,
    val isActive: Boolean,
    val status: Int
)


