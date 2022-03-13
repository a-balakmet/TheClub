package com.the.club.data.repository

import com.google.gson.Gson
import com.the.club.common.CommonKeys.noNetwork
import com.the.club.common.model.Resource
import com.the.club.data.NetworkCallResponseAdapter
import com.the.club.data.remote.push.PushApi
import com.the.club.domain.repository.PushRepository
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class PushRepositoryImpl @Inject constructor(
    override val gson: Gson,
    private val api: PushApi,
) : PushRepository, NetworkCallResponseAdapter {

    override suspend fun notificationRead(uuid: String, body: HashMap<String, String>): Resource<Unit> {
        return try {
            Resource.Success(api.notificationRead(uuid, body))
        } catch (e: HttpException) {
            Resource.Error(message = noNetwork)
        } catch (e: IOException) {
            Resource.Error(message = noNetwork)
        }
    }
}