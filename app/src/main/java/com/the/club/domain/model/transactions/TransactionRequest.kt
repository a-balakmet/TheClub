package com.the.club.domain.model.transactions


data class TransactionRequest(
    val cardId: Long,
    val dateFrom: String,
    val dateTo: String,
    val sort: String,
    val page: Int,
    val size: Int
)
