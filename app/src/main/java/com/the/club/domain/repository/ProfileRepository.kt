package com.the.club.domain.repository

import kotlinx.coroutines.flow.Flow
import com.the.club.common.model.Resource
import com.the.club.data.remote.profile.dto.DeviceInfo
import com.the.club.data.remote.profile.dto.ProfileDto
import com.the.club.domain.model.Profile

interface ProfileRepository {

    suspend fun getProfile() : Flow<Resource<ProfileDto>>

    suspend fun checkDevice(deviceInfo: DeviceInfo): Resource<DeviceInfo>

    suspend fun updateProfile(newProfile: Profile) : Flow<Resource<ProfileDto>>

    suspend fun blockCard(cardId: Long) : Flow<Resource<Unit>>

    suspend fun unblockCard(cardId: Long) : Flow<Resource<Unit>>
}