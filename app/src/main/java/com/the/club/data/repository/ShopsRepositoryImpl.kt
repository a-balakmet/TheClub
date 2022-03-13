package com.the.club.data.repository

import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import com.the.club.common.CommonKeys.noNetwork
import com.the.club.common.model.CityDto
import com.the.club.common.model.Geo
import com.the.club.common.model.Resource
import com.the.club.common.model.toCity
import com.the.club.data.NetworkCallResponseAdapter
import com.the.club.data.remote.shops.ShopsApi
import com.the.club.data.remote.shops.dto.ShopDto
import com.the.club.data.remote.shops.dto.toShop
import com.the.club.domain.model.City
import com.the.club.domain.model.shops.NearestShopsRequest
import com.the.club.domain.model.shops.ProductPriceRequest
import com.the.club.domain.model.shops.Shop
import com.the.club.domain.repository.ShopsRepository
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class ShopsRepositoryImpl @Inject constructor(
    override val gson: Gson,
    private val shopsApi: ShopsApi
) : ShopsRepository, NetworkCallResponseAdapter {

    override suspend fun getCities(): Flow<Resource<List<City>>> = flow {
        emit(Resource.Loading)
        try {
            val response = shopsApi.getCities()
            val resource = mapResponse(response) { it.map(CityDto::toCity) }
            emit(resource)
        } catch (e: HttpException) {
            emit(Resource.Error(message = noNetwork))
        } catch(e: IOException) {
            emit(Resource.Error(message = noNetwork))
        }
    }

    override suspend fun getShops(): Flow<Resource<List<Shop>>> = flow  {
        emit(Resource.Loading)
        try {
            val response = shopsApi.getShops()
            val resource = mapResponse(response) { it.map(ShopDto::toShop) }
            emit(resource)
        } catch (e: HttpException) {
            emit(Resource.Error(message = noNetwork))
        } catch(e: IOException) {
            emit(Resource.Error(message = noNetwork))
        }
    }

    override suspend fun getNearestShop(geo: Geo) = flow  {
        emit(Resource.Loading)
        try {
            val response = shopsApi.getNearestShop(latitude = geo.latitude!!, longitude = geo.longitude!!)
            val resource = mapResponse(response) { it.toShop() }
            emit(resource)
        } catch (e: HttpException) {
            emit(Resource.Error(message = noNetwork))
        } catch(e: IOException) {
            emit(Resource.Error(message = noNetwork))
        }
    }

    override suspend fun getNearestShops(request: NearestShopsRequest) = flow {
        emit(Resource.Loading)
        try {
            val response = shopsApi.getNearestShops(quantity = request.k, latitude = request.latitude, longitude = request.longitude)
            val resource = mapResponse(response) { it.map(ShopDto::toShop) }
            emit(resource)
        } catch (e: HttpException) {
            emit(Resource.Error(message = noNetwork))
        } catch(e: IOException) {
            emit(Resource.Error(message = noNetwork))
        }
    }

    override suspend fun getShopByQr(qr: String) = flow {
        emit(Resource.Loading)
        try {
            val response = shopsApi.getShopByQR(qr)
            val resource = mapResponse(response) { it.toShop() }
            emit(resource)
        } catch (e: HttpException) {
            emit(Resource.Error(message = noNetwork))
        } catch(e: IOException) {
            emit(Resource.Error(message = noNetwork))
        }
    }

    override suspend fun getProductPrice(request: ProductPriceRequest) = flow  {
        emit(Resource.Loading)
        try {
            val response = shopsApi.getPriceInShop(shopId = request.actor_id, barcode = request.barcode)
            val resource = handleResponse(response)
            emit(resource)
        } catch (e: HttpException) {
            emit(Resource.Error(message = noNetwork))
        } catch(e: IOException) {
            emit(Resource.Error(message = noNetwork))
        }
    }

    override suspend fun getShopById(id: Long) = flow {
        emit(Resource.Loading)
        try {
            val response = shopsApi.getShopById(shopId = id)
            val resource = mapResponse(response) { it.toShop() }
            emit(resource)
        } catch (e: HttpException) {
            emit(Resource.Error(message = noNetwork))
        } catch(e: IOException) {
            emit(Resource.Error(message = noNetwork))
        }
    }
}