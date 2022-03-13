package com.the.club.data.repository

import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import com.the.club.common.CommonKeys
import com.the.club.common.model.Pageable
import com.the.club.common.model.Resource
import com.the.club.common.model.map
import com.the.club.data.NetworkCallResponseAdapter
import com.the.club.data.remote.promotions.PromotionsApi
import com.the.club.data.remote.promotions.dto.PromotionDetailsDto
import com.the.club.data.remote.promotions.dto.PromotionDto
import com.the.club.data.remote.promotions.dto.toPromotion
import com.the.club.data.remote.promotions.dto.toPromotionDetails
import com.the.club.domain.model.promos.Promotion
import com.the.club.domain.model.promos.PromotionDetails
import com.the.club.domain.model.promos.PromotionRequest
import com.the.club.domain.repository.PromotionsRepository
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class PromotionsRepositoryImpl @Inject constructor(
    override val gson: Gson,
    private val promotionsApi: PromotionsApi
) : PromotionsRepository, NetworkCallResponseAdapter {

    override suspend fun getPromotions(request: PromotionRequest): Flow<Resource<Pageable<Promotion>>> = flow {
        if (request.page > 1) emit(Resource.LoadingMore)
        else emit(Resource.Loading)
        try {
            val response = promotionsApi.getPromotions(
                request.page,
                request.size,
                request.sort
            )
            val resource = mapResponse(response) { it.map(PromotionDto::toPromotion) }
            emit(resource)
        } catch (e: HttpException) {
            emit(Resource.Error(message = CommonKeys.noNetwork))
        } catch(e: IOException) {
            emit(Resource.Error(message = CommonKeys.noNetwork))
        }
    }

    override suspend fun getPromotion(promotionId: Long): Flow<Resource<PromotionDetails>> = flow {
        emit(Resource.Loading)
        try {
            val response = promotionsApi.getPromotion(promotionId)
            val resource = mapResponse(response, PromotionDetailsDto::toPromotionDetails)
            emit(resource)
        } catch (e: HttpException) {
            emit(Resource.Error(message = CommonKeys.noNetwork))
        } catch(e: IOException) {
            emit(Resource.Error(message = CommonKeys.noNetwork))
        }
    }
}