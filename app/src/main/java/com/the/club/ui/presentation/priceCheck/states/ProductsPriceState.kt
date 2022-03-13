package com.the.club.ui.presentation.priceCheck.states

import com.the.club.data.remote.shops.dto.ProductPrice

class ProductsPriceState (
    val isLoading: Boolean = false,
    var productPrice: ProductPrice? = null,
    var error: String = ""
)