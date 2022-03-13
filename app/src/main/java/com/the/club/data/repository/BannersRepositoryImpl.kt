package com.the.club.data.repository

import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import com.the.club.common.CommonKeys.noNetwork
import com.the.club.common.model.Resource
import com.the.club.data.NetworkCallResponseAdapter
import com.the.club.data.remote.banners.BannersApi
import com.the.club.data.remote.banners.dto.BannerDto
import com.the.club.data.remote.banners.dto.toBanner
import com.the.club.domain.model.Banner
import com.the.club.domain.repository.BannersRepository
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class BannersRepositoryImpl @Inject constructor(
    override val gson: Gson,
    private val bannersApi: BannersApi
) : BannersRepository, NetworkCallResponseAdapter {

    override suspend fun getBanners(): Flow<Resource<List<Banner>>> = flow {
        emit(Resource.Loading)
        try {
            val response = bannersApi.getBanners()
            val resource = mapResponse(response) { banners -> banners.map(BannerDto::toBanner) }
            emit(resource)
        } catch (e: HttpException) {
            emit(Resource.Error(message = noNetwork))
        } catch(e: IOException) {
            emit(Resource.Error(message = noNetwork))
        }
    }
}