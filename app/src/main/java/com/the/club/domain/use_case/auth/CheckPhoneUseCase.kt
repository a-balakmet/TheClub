package com.the.club.domain.use_case.auth

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import com.the.club.common.model.Resource
import com.the.club.domain.repository.AuthRepository
import javax.inject.Inject

class CheckPhoneUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    operator fun invoke(phone: String): Flow<Resource<Unit>> = flow {
        emit(Resource.Loading)
        emit(repository.checkPhone(phone))
    }
}