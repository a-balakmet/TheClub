package com.the.club.ui.presentation.transactions.states

import com.the.club.domain.model.transactions.Burning

data class BurningsState(
    val isLoading: Boolean = false,
    val burnings: List<Burning> = emptyList(),
    val isEmptyList: Boolean = false,
    val error: String = ""
)
