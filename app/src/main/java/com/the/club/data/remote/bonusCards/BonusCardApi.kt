package com.the.club.data.remote.bonusCards

import com.the.club.data.remote.bonusCards.dto.BonusCardDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface BonusCardApi {

    @GET("v3/accounting/cards")
    suspend fun getBonusCard(): Response<List<BonusCardDto>>

    @POST("v3/accounting/cards/bind")
    suspend fun bindBonusCard(@Body body: HashMap<String, Long>): Response<BonusCardDto>

    @POST("v3/accounting/cards/generation")
    suspend fun createBonusCard(): Response<BonusCardDto>
}