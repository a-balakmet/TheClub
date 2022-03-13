package com.the.club.domain.repository

import kotlinx.coroutines.flow.Flow
import com.the.club.common.model.Pageable
import com.the.club.common.model.Resource
import com.the.club.domain.model.promos.Promotion
import com.the.club.domain.model.promos.PromotionDetails
import com.the.club.domain.model.promos.PromotionRequest

interface PromotionsRepository {

    suspend fun getPromotions(request: PromotionRequest): Flow<Resource<Pageable<Promotion>>>
    suspend fun getPromotion(promotionId: Long): Flow<Resource<PromotionDetails>>
}