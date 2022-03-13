package com.the.club.common

import com.the.club.common.sharedPreferences.PreferenceRepository
import com.the.club.common.sharedPreferences.PreferencesKeys.Companion.ACCESS_TOKEN
import com.the.club.common.sharedPreferences.PreferencesKeys.Companion.LOCALE
import com.the.club.common.sharedPreferences.PreferencesKeys.Companion.REFRESH_TOKEN
import com.the.club.data.remote.refreshToken.RefreshTokenApi
import com.the.club.data.remote.refreshToken.dto.RefreshTokenBody
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import java.net.HttpURLConnection

class AccessTokenInterceptor(
    private val refreshApi: RefreshTokenApi,
    private val prefs: PreferenceRepository
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()
        val request = buildNewRequest(original)
        val response = chain.proceed(request)
        if (response.code == HttpURLConnection.HTTP_UNAUTHORIZED) {
            val refreshTokenBody =
                RefreshTokenBody(refreshToken = prefs.getStringValue(tag = REFRESH_TOKEN))
            val refreshTokenResponse = refreshApi.refreshToken(refreshTokenBody).execute()

            if (refreshTokenResponse.isSuccessful) {
                val refreshedToken = refreshTokenResponse.body()
                if (refreshedToken != null) {
                    prefs.setValue(tag = ACCESS_TOKEN, value = refreshedToken.authToken)
                    prefs.setValue(tag = REFRESH_TOKEN, value = refreshedToken.refreshToken)
                    val newRequest = buildNewRequest(original)
                    response.close()
                    return chain.proceed(newRequest)
                }
            } else {
                return response
            }
        }
        return response
    }

    private fun buildNewRequest(request: Request): Request {
        var locale = prefs.getStringValue(tag = LOCALE)
        if (locale.isBlank()) locale = "ru"
        val token = prefs.getStringValue(tag = ACCESS_TOKEN)
        return request.newBuilder()
            .header("Accept", "application/json")
            .header("Content-type", "application/json")
            .header("locale", locale)
            .header("Authorization", "Bearer $token")
            .build()
    }
}