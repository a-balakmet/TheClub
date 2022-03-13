package com.the.club.domain.model

import com.google.android.gms.maps.model.LatLng

data class City(
    val geo: LatLng?,
    val id: Int,
    val name: String
)
