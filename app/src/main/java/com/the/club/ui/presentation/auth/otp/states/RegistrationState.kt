package com.the.club.ui.presentation.auth.otp.states

import com.the.club.domain.model.BonusCard

data class RegistrationState(
    val isLoading: Boolean = false,
    val error: String = "",
    val cards: List<BonusCard>? = null,
)
