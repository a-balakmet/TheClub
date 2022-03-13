package com.the.club.data.remote.shops.dto

import com.google.android.gms.maps.model.LatLng
import com.google.gson.annotations.SerializedName
import com.the.club.common.ktx.getHour
import com.the.club.common.ktx.toLatLng
import com.the.club.common.model.CityDto
import com.the.club.common.model.Geo
import com.the.club.common.model.toCity
import com.the.club.domain.model.shops.Shop

data class ShopDto(
    @SerializedName("address")
    val address: String?,
    @SerializedName("city")
    val city: CityDto?,
    @SerializedName("city_id")
    val cityId: Int,
    @SerializedName("close_hour")
    val closeHour: String,
    @SerializedName("geo")
    val geo: Geo?,
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("open_hour")
    val openHour: String,
    @SerializedName("qr_code")
    val qrCode: String
)

fun ShopDto.toShop() = Shop(
    address = this.address,
    city = this.city?.toCity(),
    cityId = this.cityId,
    geo = this.geo.toLatLng(),
    id = this.id,
    name = this.name,
    openHour = this.openHour.getHour(),
    closeHour = this.closeHour.getHour()
)

fun LatLng.toShop(list: List<Shop>): Shop {
    val latShops: ArrayList<Shop> = ArrayList()
    for (shop in list) {
        if (shop.geo?.latitude == this.latitude) {
            latShops.add(shop)
        }
    }
    val lngShops: ArrayList<Shop> = ArrayList()
    for (shop in latShops) {
        if (shop.geo?.longitude == this.longitude) {
            lngShops.add(shop)
        }
    }
    return lngShops[0]
}
