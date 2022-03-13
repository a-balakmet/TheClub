package com.the.club.domain.repository

import kotlinx.coroutines.flow.Flow
import com.the.club.common.model.Resource
import com.the.club.domain.model.clubs.Club
import com.the.club.domain.model.clubs.MembershipType

interface ClubsRepository {

    suspend fun getAllClubs(): Flow<Resource<List<Club>>>
    suspend fun getClientClubs(): Flow<Resource<List<Club>>>
    suspend fun getNonClientClubs(): Flow<Resource<List<Club>>>
    suspend fun enterClub(clubId: Int, membershipType: MembershipType) : Resource<Unit>
    suspend fun exitClub(clubId: Int) : Resource<Unit>

}