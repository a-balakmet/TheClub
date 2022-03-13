package com.the.club.domain.repository

import kotlinx.coroutines.flow.Flow
import com.the.club.common.model.Resource
import com.the.club.common.model.Counters
import com.the.club.domain.model.Notification

interface NotificationsRepository {

    suspend fun getNotifications(): Flow<Resource<List<Notification>>>

    suspend fun getUnreadNotifications(): Flow<Resource<Counters>>
}