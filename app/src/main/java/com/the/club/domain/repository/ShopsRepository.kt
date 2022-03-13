package com.the.club.domain.repository

import kotlinx.coroutines.flow.Flow
import com.the.club.common.model.Geo
import com.the.club.common.model.Resource
import com.the.club.data.remote.shops.dto.ProductPrice
import com.the.club.domain.model.City
import com.the.club.domain.model.shops.NearestShopsRequest
import com.the.club.domain.model.shops.ProductPriceRequest
import com.the.club.domain.model.shops.Shop

interface ShopsRepository {

    suspend fun getCities(): Flow<Resource<List<City>>>
    suspend fun getShops(): Flow<Resource<List<Shop>>>
    suspend fun getNearestShop(geo: Geo): Flow<Resource<Shop>>
    suspend fun getNearestShops(request: NearestShopsRequest): Flow<Resource<List<Shop>>>
    suspend fun getShopByQr(qr: String): Flow<Resource<Shop>>
    suspend fun getProductPrice(request: ProductPriceRequest): Flow<Resource<ProductPrice>>
    suspend fun getShopById(id: Long): Flow<Resource<Shop>>

}