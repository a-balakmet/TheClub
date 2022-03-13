package com.the.club.data.remote.transactions

import com.the.club.common.model.Pageable
import com.the.club.data.remote.transactions.dto.BurningDto
import com.the.club.data.remote.transactions.dto.TransactionDto
import com.the.club.data.remote.transactions.dto.TransactionProductDto
import com.the.club.data.remote.transactions.dto.WinningChanceDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TransactionsApi {

    @GET("v3/accounting/transactions/page")
    suspend fun getTransactions(
        @Query("cardId") cardId: Long?,
        @Query("dt_created_from") dateFrom: String?,
        @Query("dt_created_to") dateTo: String?,
        @Query("sort") sort: String,
        @Query("page") page: Int,
        @Query("size") size: Int
    ): Response<Pageable<TransactionDto>>

    @GET("v3/accounting/cards/{card_id}/burnSchedule")
    suspend fun getBurningBonuses(
        @Path("card_id") cardId: Long,
        @Query("size") items: Int
    ) : Response<List<BurningDto>>

    @GET("v3/accounting/transactions/{transaction_id}/items")
    suspend fun getTransactionGoods(
        @Path("transaction_id") transaction: Long,
    ) : Response<List<TransactionProductDto>>

    @GET("v3/promotionChance/list")
    suspend fun getWinChance(
        @Query("references") transaction: Long,
    ) : Response<List<WinningChanceDto>>
}