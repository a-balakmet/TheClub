package com.the.club.data.repository

import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import com.the.club.common.CommonKeys.noNetwork
import com.the.club.common.model.Resource
import com.the.club.data.NetworkCallResponseAdapter
import com.the.club.data.remote.clubs.ClubsApi
import com.the.club.data.remote.clubs.dto.ClubDto
import com.the.club.data.remote.clubs.dto.toClientClub
import com.the.club.data.remote.clubs.dto.toNonClientClub
import com.the.club.domain.model.clubs.Club
import com.the.club.domain.model.clubs.MembershipType
import com.the.club.domain.repository.ClubsRepository
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class ClubsRepositoryImpl @Inject constructor(
    override val gson: Gson,
    private val clubsApi: ClubsApi
) : ClubsRepository, NetworkCallResponseAdapter {
    
    override suspend fun getAllClubs(): Flow<Resource<List<Club>>> = flow {
        emit(Resource.Loading)
        try {
            val response = clubsApi.getAllClubs()
            val resource = mapResponse(response) { clubs -> clubs.map(ClubDto::toNonClientClub) }
            emit(resource)
        } catch (e: HttpException) {
            emit(Resource.Error(message = noNetwork))
        } catch(e: IOException) {
            emit(Resource.Error(message = noNetwork))
        }
    }

    override suspend fun getClientClubs(): Flow<Resource<List<Club>>> = flow {
        emit(Resource.Loading)
        try {
            val response = clubsApi.getClientClubs()
            val resource = mapResponse(response) { clubs -> clubs.map(ClubDto::toClientClub) }
            emit(resource)
        } catch (e: HttpException) {
            emit(Resource.Error(message = noNetwork))
        } catch(e: IOException) {
            emit(Resource.Error(message = noNetwork))
        }
    }

    override suspend fun getNonClientClubs(): Flow<Resource<List<Club>>> = flow {
        emit(Resource.Loading)
        try {
            val response = clubsApi.getNonClientClubs()
            val resource = mapResponse(response) { clubs -> clubs.map(ClubDto::toNonClientClub) }
            emit(resource)
        } catch (e: HttpException) {
            emit(Resource.Error(message = noNetwork))
        } catch(e: IOException) {
            emit(Resource.Error(message = noNetwork))
        }
    }

    override suspend fun enterClub(clubId: Int, membershipType: MembershipType): Resource<Unit> {
        return try {
            Resource.Success(clubsApi.enterClub(clubId, membershipType))
        } catch (e: HttpException) {
            Resource.Error(message = noNetwork)
        } catch (e: IOException) {
            Resource.Error(message = noNetwork)
        }
    }

    override suspend fun exitClub(clubId: Int): Resource<Unit> {
        return try {
            Resource.Success(clubsApi.exitClub(clubId))
        } catch (e: HttpException) {
            Resource.Error(message = noNetwork)
        } catch (e: IOException) {
            Resource.Error(message = noNetwork)
        }
    }

}