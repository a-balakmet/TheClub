package com.the.club.data.repository

import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import com.the.club.common.CommonKeys.noNetwork
import com.the.club.common.model.Resource
import com.the.club.data.NetworkCallResponseAdapter
import com.the.club.data.remote.bonusCards.BonusCardApi
import com.the.club.data.remote.bonusCards.dto.BonusCardDto
import com.the.club.data.remote.bonusCards.dto.toBonusCard
import com.the.club.domain.model.BonusCard
import com.the.club.domain.repository.BonusCardsRepository
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class BonusCardsRepositoryImpl @Inject constructor(
    override val gson: Gson,
    private val bonusCardsApi: BonusCardApi
) : BonusCardsRepository, NetworkCallResponseAdapter {

    override suspend fun getBonusCards(): Flow<Resource<List<BonusCard>>> = flow {
        emit(Resource.Loading)
        try {
            val response = bonusCardsApi.getBonusCard()
            val resource = mapResponse(response) { bonusCards -> bonusCards.map(BonusCardDto::toBonusCard) }
            emit(resource)
        } catch (e: HttpException) {
            emit(Resource.Error(message = noNetwork))
        } catch(e: IOException) {
            emit(Resource.Error(message = noNetwork))
        }
    }

    override suspend fun bindBonusCards(cardNo: Long): Flow<Resource<BonusCard>> = flow {
        emit(Resource.Loading)
        try {
            val body = HashMap<String, Long>()
            body["card_number"] = cardNo
            val response = bonusCardsApi.bindBonusCard(body)
            val resource = mapResponse(response, BonusCardDto::toBonusCard)
            emit(resource)
        } catch (e: HttpException) {
            emit(Resource.Error(message = noNetwork))
        } catch(e: IOException) {
            emit(Resource.Error(message = noNetwork))
        }
    }

    override suspend fun createBonusCards(): Flow<Resource<BonusCard>> = flow {
        emit(Resource.Loading)
        try {
            val response = bonusCardsApi.createBonusCard()
            val resource = mapResponse(response, BonusCardDto::toBonusCard)
            emit(resource)
        } catch (e: HttpException) {
            emit(Resource.Error(message = noNetwork))
        } catch(e: IOException) {
            emit(Resource.Error(message = noNetwork))
        }
    }
}