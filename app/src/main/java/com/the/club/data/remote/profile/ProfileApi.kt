package com.the.club.data.remote.profile

import com.the.club.data.remote.profile.dto.DeviceInfo
import com.the.club.data.remote.profile.dto.ProfileDto
import com.the.club.domain.model.Profile
import retrofit2.Response
import retrofit2.http.*

interface ProfileApi {

    @GET("v3/profile")
    suspend fun getUserProfile(): Response<ProfileDto>

    @POST("v3/profile/devices/check")
    suspend fun checkDevice(@Body deviceInfo: DeviceInfo): Response<DeviceInfo>

    @PATCH("v3/profile")
    suspend fun updateProfile(@Body newProfile: Profile): Response<ProfileDto>

    @POST("v3/accounting/cards/{cardId}/block")
    suspend fun block(@Path("cardId") cardId: Long): Response<Unit>

    @POST("v3/accounting/cards/{cardId}/unblock")
    suspend fun unblock(@Path("cardId") cardId: Long): Response<Unit>
}