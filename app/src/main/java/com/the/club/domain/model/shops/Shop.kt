package com.the.club.domain.model.shops

import com.google.android.gms.maps.model.LatLng
import com.the.club.domain.model.City

data class Shop(
    val address: String?,
    val city: City?,
    val cityId: Int,
    val geo: LatLng?,
    val id: Int,
    val name: String,
    val openHour: Int,
    val closeHour: Int
)
