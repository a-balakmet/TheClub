package com.the.club.ui.presentation.main.states

import com.the.club.domain.model.Banner

data class BannersState(
    val isLoading: Boolean = false,
    val banners: List<Banner> = emptyList(),
    val error: String = ""
)
