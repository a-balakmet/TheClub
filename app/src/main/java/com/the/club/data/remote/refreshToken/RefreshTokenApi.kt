package com.the.club.data.remote.refreshToken

import com.the.club.data.remote.refreshToken.dto.AuthTokens
import com.the.club.data.remote.refreshToken.dto.RefreshTokenBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface RefreshTokenApi {

    @POST("v3/auth/refresh")
    fun refreshToken(@Body refreshTokenBody: RefreshTokenBody) : Call<AuthTokens>
}