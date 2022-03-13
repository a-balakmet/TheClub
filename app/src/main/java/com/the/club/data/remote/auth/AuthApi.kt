package com.the.club.data.remote.auth

import com.the.club.data.remote.auth.dto.TokensDto
import com.the.club.data.remote.profile.dto.DeviceInfo
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {

    @POST("v3/auth/checkPhone")
    suspend fun checkPhone(@Body body: HashMap<String, String>)

    @POST("v3/auth/registration")
    suspend fun checkOTP(@Body body: HashMap<String, Any>) : Response<TokensDto>

    @POST("v3/profile/devices/check")
    suspend fun checkDevice(@Body deviceInfo: DeviceInfo): Response<DeviceInfo>
}