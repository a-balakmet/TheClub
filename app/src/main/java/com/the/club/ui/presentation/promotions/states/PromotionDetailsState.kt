package com.the.club.ui.presentation.promotions.states

import com.the.club.domain.model.promos.PromotionDetails

data class PromotionDetailsState(
    val isLoading: Boolean = false,
    val promotionDetails: PromotionDetails? = null,
    val error: String = ""
)
