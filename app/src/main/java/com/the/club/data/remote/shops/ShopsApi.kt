package com.the.club.data.remote.shops

import com.the.club.common.model.CityDto
import com.the.club.data.remote.shops.dto.ProductPrice
import com.the.club.data.remote.shops.dto.ShopDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ShopsApi {

    @GET("v3/directory/cities")
    suspend fun getCities() : Response<List<CityDto>>

    @GET("v3/directory/actors")
    suspend fun getShops() : Response<List<ShopDto>>

    @GET("v3/directory/actors/nearest")
    suspend fun getNearestShop(
        @Query("latitude") latitude : Double,
        @Query("longitude") longitude  : Double
    ) : Response<ShopDto>

    @GET("v3/directory/actors/kNearest")
    suspend fun getNearestShops(
        @Query("k") quantity: Int,
        @Query("latitude") latitude : Double,
        @Query("longitude") longitude  : Double
    ) : Response<List<ShopDto>>

    @GET("v3/directory/actors/byQRCode")
    suspend fun getShopByQR(@Query("qr_code") code: String): Response<ShopDto>

    @GET("v3/priceChecker/check")
    suspend fun getPriceInShop(
        @Query("actor_id") shopId: Int,
        @Query("barcode") barcode: String
    ): Response<ProductPrice>

    @GET("v3/directory/actors/{id}")
    suspend fun getShopById(
        @Path("id") shopId: Long,
    ): Response<ShopDto>
}