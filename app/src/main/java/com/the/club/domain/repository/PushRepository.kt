package com.the.club.domain.repository

import com.the.club.common.model.Resource

interface PushRepository {

    suspend fun notificationRead(uuid: String, body: HashMap<String, String>) : Resource<Unit>
}