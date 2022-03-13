package com.the.club.domain.repository

import kotlinx.coroutines.flow.Flow
import com.the.club.common.model.Resource
import com.the.club.domain.model.BonusCard

interface BonusCardsRepository {

    suspend fun getBonusCards(): Flow<Resource<List<BonusCard>>>
    suspend fun bindBonusCards(cardNo: Long): Flow<Resource<BonusCard>>
    suspend fun createBonusCards(): Flow<Resource<BonusCard>>

}