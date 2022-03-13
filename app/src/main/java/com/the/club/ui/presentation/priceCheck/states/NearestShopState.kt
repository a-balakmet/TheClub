package com.the.club.ui.presentation.priceCheck.states

import com.the.club.domain.model.shops.Shop

class NearestShopState (
    val isLoading: Boolean = false,
    var shop: Shop? = null,
    var error: String = ""
)