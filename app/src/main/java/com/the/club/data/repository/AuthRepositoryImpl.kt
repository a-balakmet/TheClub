package com.the.club.data.repository

import android.os.Build
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import com.the.club.BuildConfig
import com.the.club.common.CommonKeys.noNetwork
import com.the.club.common.model.Resource
import com.the.club.data.NetworkCallResponseAdapter
import com.the.club.data.remote.auth.AuthApi
import com.the.club.data.remote.auth.dto.TokensDto
import com.the.club.data.remote.profile.dto.DeviceInfo
import com.the.club.domain.repository.AuthRepository
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    override val gson: Gson,
    private val api: AuthApi,
) : AuthRepository, NetworkCallResponseAdapter {

    override suspend fun checkPhone(phone: String): Resource<Unit> {
        return try {
            val body = HashMap<String, String>()
            body["phone"] = phone.substring(1)
            Resource.Success(api.checkPhone(body))
        } catch (e: HttpException) {
            Resource.Error(message = noNetwork)
        } catch (e: IOException) {
            Resource.Error(message = noNetwork)
        }
    }

    override suspend fun checkOTP(
        otp: String,
        phone: String
    ): Flow<Resource<TokensDto>> = flow {
        emit(Resource.Loading)
        try {
            val body = HashMap<String, Any>()
            body["code"] = otp.toInt()
            body["phone"] = phone.substring(1)
            val response = api.checkOTP(body)
            val resource = handleResponse(response)
            emit(resource)
        } catch (e: HttpException) {
            emit(Resource.Error(message = noNetwork))
        } catch(e: IOException) {
            emit(Resource.Error(message = noNetwork))
        }
    }

    override suspend fun checkDevice(fbToken: String, deviceId: String): Flow<Resource<DeviceInfo>> = flow {
        val deviceInfo = DeviceInfo(
            appBuild = BuildConfig.VERSION_CODE.toString(),
            appVersion = BuildConfig.VERSION_NAME,
            deviceId = deviceId,
            manufacturer = Build.MANUFACTURER,
            model = Build.MODEL,
            osVersion = Build.VERSION.SDK_INT.toString(),
            platform = "android",
            pushToken = fbToken
        )
        emit(Resource.Loading)
        try {
            val response = api.checkDevice(deviceInfo)
            val resource = handleResponse(response)
            emit(resource)
        } catch (e: HttpException) {
            emit(Resource.Error(message = noNetwork))
        } catch(e: IOException) {
            emit(Resource.Error(message = noNetwork))
        }
    }

}