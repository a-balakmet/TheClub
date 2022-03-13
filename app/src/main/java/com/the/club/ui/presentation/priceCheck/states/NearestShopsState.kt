package com.the.club.ui.presentation.priceCheck.states

import com.the.club.domain.model.shops.Shop

class NearestShopsState (
    val isLoading: Boolean = false,
    var shops: List<Shop> = emptyList(),
    var error: String = ""
)