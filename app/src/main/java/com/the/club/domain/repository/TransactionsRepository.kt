package com.the.club.domain.repository

import kotlinx.coroutines.flow.Flow
import com.the.club.common.model.Pageable
import com.the.club.common.model.Resource
import com.the.club.domain.model.transactions.*

interface TransactionsRepository {

    suspend fun getTransactions(request: TransactionRequest): Flow<Resource<Pageable<Transaction>>>

    suspend fun getBurnings(cardId: Long, itemsNo: Int): Flow<Resource<List<Burning>>>

    suspend fun getProducts(transactionId: Long) : Flow<Resource<List<TransactionProduct>>>

    suspend fun getWinChance(transactionId: Long) : Flow<Resource<List<WinningChance>>>
}