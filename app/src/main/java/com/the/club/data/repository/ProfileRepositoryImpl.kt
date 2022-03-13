package com.the.club.data.repository

import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import com.the.club.common.CommonKeys.noNetwork
import com.the.club.common.model.Resource
import com.the.club.data.NetworkCallResponseAdapter
import com.the.club.data.remote.profile.ProfileApi
import com.the.club.data.remote.profile.dto.DeviceInfo
import com.the.club.data.remote.profile.dto.ProfileDto
import com.the.club.domain.model.Profile
import com.the.club.domain.repository.ProfileRepository
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class ProfileRepositoryImpl @Inject constructor(
    override val gson: Gson,
    private val profileApi: ProfileApi
) : ProfileRepository, NetworkCallResponseAdapter {

    override suspend fun getProfile(): Flow<Resource<ProfileDto>> = flow {
        emit(Resource.Loading)
        try {
            val response = profileApi.getUserProfile()
            val resource = handleResponse(response)
            emit(resource)
        } catch (e: HttpException) {
            emit(Resource.Error(message = noNetwork))
        } catch(e: IOException) {
            emit(Resource.Error(message = noNetwork))
        }
    }

    override suspend fun checkDevice(deviceInfo: DeviceInfo): Resource<DeviceInfo> {
        val response = profileApi.checkDevice(deviceInfo)
        return handleResponse(response)
    }

    override suspend fun updateProfile(newProfile: Profile): Flow<Resource<ProfileDto>> = flow {
        emit(Resource.Loading)
        try {
            val response = profileApi.updateProfile(newProfile)
            val resource = handleResponse(response)
            emit(resource)
        } catch (e: HttpException) {
            emit(Resource.Error(message = noNetwork))
        } catch(e: IOException) {
            emit(Resource.Error(message = noNetwork))
        }
    }

    override suspend fun blockCard(cardId: Long): Flow<Resource<Unit>> = flow {
        emit(Resource.Loading)
        try {
            val response = profileApi.block(cardId)
            val resource = mapResponse(response) {}
            emit(resource)
        } catch (e: HttpException) {
            emit(Resource.Error(message = noNetwork))
        } catch(e: IOException) {
            emit(Resource.Error(message = noNetwork))
        }
    }


    override suspend fun unblockCard(cardId: Long): Flow<Resource<Unit>> = flow {
        emit(Resource.Loading)
        try {
            val response = profileApi.unblock(cardId)
            val resource = mapResponse(response) {}
            emit(resource)
        } catch (e: HttpException) {
            emit(Resource.Error(message = noNetwork))
        } catch(e: IOException) {
            emit(Resource.Error(message = noNetwork))
        }
    }
}