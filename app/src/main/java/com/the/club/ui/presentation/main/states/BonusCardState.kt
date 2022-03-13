package com.the.club.ui.presentation.main.states

import com.the.club.domain.model.BonusCard

data class BonusCardState(
    val isLoading: Boolean = false,
    val cards: List<BonusCard> = emptyList(),
    val error: String = ""
)
