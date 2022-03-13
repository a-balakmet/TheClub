package com.the.club.domain.repository

import kotlinx.coroutines.flow.Flow
import com.the.club.common.model.Resource
import com.the.club.domain.model.Banner

interface BannersRepository {

    suspend fun getBanners(): Flow<Resource<List<Banner>>>
}