package com.the.club.data.remote.promotions

import com.the.club.common.model.Pageable
import com.the.club.data.remote.promotions.dto.PromotionDetailsDto
import com.the.club.data.remote.promotions.dto.PromotionDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PromotionsApi {

    @GET("v3/campaigns/page")
    suspend fun getPromotions(
        @Query("page") page: Int,
        @Query("size") size: Int,
        @Query("sort") sort: String
    ): Response<Pageable<PromotionDto>>

    @GET("v3/campaigns/{id}")
    suspend fun getPromotion(@Path("id") id: Long): Response<PromotionDetailsDto>
}