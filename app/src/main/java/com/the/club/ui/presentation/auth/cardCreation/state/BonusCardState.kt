package com.the.club.ui.presentation.auth.cardCreation.state

import com.the.club.domain.model.BonusCard

data class CardState(
    val isLoading: Boolean = false,
    val card: BonusCard? = null,
    val error: String = ""
)
