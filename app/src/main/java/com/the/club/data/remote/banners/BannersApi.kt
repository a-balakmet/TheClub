package com.the.club.data.remote.banners

import com.the.club.data.remote.banners.dto.BannerDto
import retrofit2.Response
import retrofit2.http.GET

interface BannersApi {
    @GET("v3/banners")
    suspend fun getBanners(): Response<List<BannerDto>>
}