package com.the.club.domain.use_case.clubs

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import com.the.club.common.model.Resource
import com.the.club.domain.model.clubs.MembershipType
import com.the.club.domain.repository.ClubsRepository
import javax.inject.Inject

class ClubsParticipationUseCase @Inject constructor(
    private val repository: ClubsRepository
) {

    operator fun invoke(clubId: Int, membershipType: MembershipType): Flow<Resource<Unit>> = flow {
        emit(Resource.Loading)
        emit(repository.enterClub(clubId, membershipType))
    }

    operator fun invoke(clubId: Int): Flow<Resource<Unit>> = flow {
        emit(Resource.Loading)
        emit(repository.exitClub(clubId))
    }

}