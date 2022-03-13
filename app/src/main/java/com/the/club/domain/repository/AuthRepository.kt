package com.the.club.domain.repository

import kotlinx.coroutines.flow.Flow
import com.the.club.common.model.Resource
import com.the.club.data.remote.auth.dto.TokensDto
import com.the.club.data.remote.profile.dto.DeviceInfo

interface AuthRepository {

    suspend fun checkPhone(phone: String) : Resource<Unit>

    suspend fun checkOTP(otp: String, phone: String) : Flow<Resource<TokensDto>>

    suspend fun checkDevice(fbToken: String, deviceId: String) : Flow<Resource<DeviceInfo>>
}