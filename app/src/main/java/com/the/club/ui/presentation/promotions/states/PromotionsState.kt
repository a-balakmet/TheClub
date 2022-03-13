package com.the.club.ui.presentation.promotions.states

import com.the.club.domain.model.promos.Promotion

data class PromotionsState(
    val isLoading: Boolean = false,
    val isLoadingMore: Boolean = false,
    val promotions: List<Promotion> = emptyList(),
    val isEmptyList: Boolean = false,
    val error: String = ""
)
