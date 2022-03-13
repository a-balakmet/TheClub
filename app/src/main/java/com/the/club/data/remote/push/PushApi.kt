package com.the.club.data.remote.push

import retrofit2.http.Body
import retrofit2.http.PATCH
import retrofit2.http.Path

interface PushApi {

    @PATCH("v3/notifications/{uuid}/status")
    suspend fun notificationRead(@Path("uuid") uuid: String, @Body body: HashMap<String, String>)
}