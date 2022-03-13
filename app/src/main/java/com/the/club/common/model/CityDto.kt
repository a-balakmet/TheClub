package com.the.club.common.model

import com.the.club.common.ktx.toLatLng
import com.the.club.domain.model.City

data class CityDto(
    val geo: Geo?,
    val id: Int,
    val name: String
)

fun CityDto.toCity() = City(
    geo = this.geo.toLatLng(),
    id = this.id,
    name = this.name
)

