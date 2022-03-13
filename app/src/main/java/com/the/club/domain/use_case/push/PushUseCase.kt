package com.the.club.domain.use_case.push

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import com.the.club.common.model.Resource
import com.the.club.domain.repository.PushRepository
import javax.inject.Inject

class PushUseCase @Inject constructor(private val repository: PushRepository) {

    operator fun invoke(uuid: String, body: HashMap<String, String>): Flow<Resource<Unit>> = flow {
        emit(Resource.Loading)
        emit(repository.notificationRead(uuid, body))
    }
}