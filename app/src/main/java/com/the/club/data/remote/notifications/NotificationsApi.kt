package com.the.club.data.remote.notifications

import com.the.club.common.model.Counters
import com.the.club.data.remote.notifications.dto.NotificationDto
import retrofit2.Response
import retrofit2.http.GET

interface NotificationsApi {

    @GET("v3/messages")
    suspend fun getNotifications(): Response<List<NotificationDto>>

    @GET("v3/messages/unreadCount")
    suspend fun getUnreadNotifications(): Response<Counters>
}