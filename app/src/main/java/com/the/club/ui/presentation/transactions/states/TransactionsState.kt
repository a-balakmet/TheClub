package com.the.club.ui.presentation.transactions.states

import com.the.club.domain.model.transactions.Transaction

data class TransactionsState(
    val isLoading: Boolean = false,
    val isLoadingMore: Boolean = false,
    val transactions: List<Transaction> = emptyList(),
    val isEmptyList: Boolean = false,
    val error: String = ""
)
