package com.the.club.domain.model.transactions

data class Transaction(
    val id: Long,
    val accruedBonuses: Int,
    val actorId: Long,
    val cardId: Long,
    val checkAmount: Long,
    val date: String,
    val time: String,
    val type: String,
    val withdrawalBonuses: Int
)
