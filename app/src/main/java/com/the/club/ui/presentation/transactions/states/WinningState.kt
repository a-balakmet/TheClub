package com.the.club.ui.presentation.transactions.states

import com.the.club.domain.model.transactions.WinningChance

data class WinningState(
    val chances: List<WinningChance> = emptyList(),
    val isEmptyList: Boolean = false,
)
