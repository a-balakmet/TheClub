package com.the.club.data.repository

import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import com.the.club.common.model.Resource
import com.the.club.data.NetworkCallResponseAdapter
import com.the.club.data.remote.notifications.dto.NotificationDto
import com.the.club.data.remote.notifications.NotificationsApi
import com.the.club.common.model.Counters
import com.the.club.data.remote.notifications.dto.toNotification
import com.the.club.domain.model.Notification
import com.the.club.domain.repository.NotificationsRepository
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class NotificationsRepositoryImpl @Inject constructor(
    override val gson: Gson,
    private val notificationsApi: NotificationsApi
) : NotificationsRepository, NetworkCallResponseAdapter {

    override suspend fun getNotifications(): Flow<Resource<List<Notification>>> = flow {
        emit(Resource.Loading)
        try {
            val response = notificationsApi.getNotifications()
            val resource = mapResponse(response) { notifications -> notifications.map(NotificationDto::toNotification) }
            emit(resource)
        } catch (e: HttpException) {
            emit(Resource.Error(message = "no network"))
        } catch(e: IOException) {
            emit(Resource.Error(message = "no network"))
        }
    }

    override suspend fun getUnreadNotifications(): Flow<Resource<Counters>> = flow {
        emit(Resource.Loading)
        try {
            val response = notificationsApi.getUnreadNotifications()
            val resource = handleResponse(response)
            emit(resource)
        } catch (e: HttpException) {
            emit(Resource.Error(message = "no network"))
        } catch(e: IOException) {
            emit(Resource.Error(message = "no network"))
        }
    }
}