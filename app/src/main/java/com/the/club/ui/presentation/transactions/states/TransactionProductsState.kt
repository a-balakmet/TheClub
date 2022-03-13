package com.the.club.ui.presentation.transactions.states

import com.the.club.domain.model.transactions.TransactionProduct

data class TransactionProductsState(
    val isLoading: Boolean = false,
    val products: List<TransactionProduct> = emptyList(),
    val isEmptyList: Boolean = false,
    val error: String = ""
)
