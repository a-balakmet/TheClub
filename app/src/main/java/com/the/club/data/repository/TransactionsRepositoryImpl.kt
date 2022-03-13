package com.the.club.data.repository

import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import com.the.club.common.CommonKeys
import com.the.club.common.model.Pageable
import com.the.club.common.model.Resource
import com.the.club.common.model.map
import com.the.club.data.NetworkCallResponseAdapter
import com.the.club.data.remote.transactions.TransactionsApi
import com.the.club.data.remote.transactions.dto.*
import com.the.club.domain.model.transactions.*
import com.the.club.domain.repository.TransactionsRepository
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class TransactionsRepositoryImpl @Inject constructor(
    override val gson: Gson,
    private val transactionsApi: TransactionsApi
) : TransactionsRepository, NetworkCallResponseAdapter {

    override suspend fun getTransactions(request: TransactionRequest): Flow<Resource<Pageable<Transaction>>> = flow {
        if (request.page > 1) emit(Resource.LoadingMore)
        else emit(Resource.Loading)
        try {
            val response = transactionsApi.getTransactions(
                request.cardId,
                request.dateFrom,
                request.dateTo,
                request.sort,
                request.page,
                request.size
            )
            val resource = mapResponse(response) {
                it.map(TransactionDto::toTransaction)
            }
            emit(resource)
        } catch (e: HttpException) {
            emit(Resource.Error(message = CommonKeys.noNetwork))
        } catch(e: IOException) {
            emit(Resource.Error(message = CommonKeys.noNetwork))
        }
    }

    override suspend fun getBurnings(cardId: Long, itemsNo: Int): Flow<Resource<List<Burning>>> = flow {
        emit(Resource.Loading)
        try {
            val response = transactionsApi.getBurningBonuses(cardId, itemsNo)
            val resource = mapResponse(response) { it.map(BurningDto::toBurning)}
            emit(resource)
        } catch (e: HttpException) {
            emit(Resource.Error(message = CommonKeys.noNetwork))
        } catch(e: IOException) {
            emit(Resource.Error(message = CommonKeys.noNetwork))
        }
    }

    override suspend fun getProducts(transactionId: Long): Flow<Resource<List<TransactionProduct>>> = flow {
        emit(Resource.Loading)
        try {
            val response = transactionsApi.getTransactionGoods(transactionId)
            val resource = mapResponse(response) { it.map(TransactionProductDto::toTransactionProducts)}
            emit(resource)
        } catch (e: HttpException) {
            emit(Resource.Error(message = CommonKeys.noNetwork))
        } catch(e: IOException) {
            emit(Resource.Error(message = CommonKeys.noNetwork))
        }
    }

    override suspend fun getWinChance(transactionId: Long): Flow<Resource<List<WinningChance>>> = flow {
        emit(Resource.Loading)
        try {
            val response = transactionsApi.getWinChance(transactionId)
            val resource = mapResponse(response) { it.map(WinningChanceDto::toWinningChance)}
            emit(resource)
        } catch (e: HttpException) {
            emit(Resource.Error(message = CommonKeys.noNetwork))
        } catch(e: IOException) {
            emit(Resource.Error(message = CommonKeys.noNetwork))
        }
    }
}