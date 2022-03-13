package com.the.club.data.remote.clubs

import com.the.club.data.remote.clubs.dto.ClubDto
import com.the.club.domain.model.clubs.MembershipType
import retrofit2.Response
import retrofit2.http.*

interface ClubsApi {

    @GET("v3/clubs/list")
    suspend fun getAllClubs(): Response<List<ClubDto>>

    @GET("v3/clubs/client/list")
    suspend fun getClientClubs(): Response<List<ClubDto>>

    @GET("v3/clubs/client/outList")
    suspend fun getNonClientClubs(): Response<List<ClubDto>>

    @POST("v3/clubs/{id}/membership")
    suspend fun enterClub(
        @Path ("id") clubId: Int,
        @Body body: MembershipType
    )

    @DELETE("v3/clubs/{id}/membership")
    suspend fun exitClub(
        @Path ("id") clubId: Int
    )
}