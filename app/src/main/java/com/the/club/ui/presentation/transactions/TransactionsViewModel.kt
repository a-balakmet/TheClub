package com.the.club.ui.presentation.transactions

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import com.the.club.common.ktx.DD_MM_YYYY
import com.the.club.common.ktx.FULL_DATE
import com.the.club.common.ktx.toDate
import com.the.club.common.ktx.toString
import com.the.club.common.model.Resource
import com.the.club.domain.model.transactions.Transaction
import com.the.club.domain.model.transactions.TransactionRequest
import com.the.club.domain.repository.TransactionsRepository
import com.the.club.ui.presentation.transactions.states.BurningsState
import com.the.club.ui.presentation.transactions.states.TransactionsState
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

@HiltViewModel
class TransactionsViewModel @Inject constructor(
    private val transactionsRepository: TransactionsRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val cardNo: Long = savedStateHandle["cardNo"] ?: 1
    private val cardId: Long = savedStateHandle["cardId"] ?: 1

    var dateFrom = mutableStateOf(getMinDateFrom())
    var dateTo = mutableStateOf(getMaxDate())

    private val _transactionState = mutableStateOf(TransactionsState())
    val transactionsState: State<TransactionsState> = _transactionState
    private var pages = 1
    private var pagesLoaded = 0
    private var transactionsList: ArrayList<Transaction> = ArrayList()

    private val _burningState = mutableStateOf(BurningsState())
    val burningsState: State<BurningsState> = _burningState

    init {
        setDefaultDates()
    }

    fun updateDateFrom(newDate: String) {
        val aDate = newDate.toDate(pattern = DD_MM_YYYY)
        val aDateTo = dateTo.value.toDate(pattern = DD_MM_YYYY)
        if (aDate != null) {
            if (aDate.before(Date())) {
                if (aDateTo != null) {
                    dateFrom.value = newDate
                } else dateFrom.value = getMinDateFrom()
            } else dateFrom.value = getMinDateFrom()
        } else dateFrom.value = getMinDateFrom()
        loadTransactions(true)
    }

    fun updateDateTo(newDate: String) {
        val aDate = newDate.toDate(pattern = DD_MM_YYYY)
        val aDateFrom = dateFrom.value.toDate(pattern = DD_MM_YYYY)
        if (aDate != null) {
            if (aDate.before(Date())) {
                if (aDateFrom != null) {
                    if (aDate.after(aDateFrom)) {
                        dateTo.value = newDate
                    } else dateTo.value = getMaxDate()
                } else dateTo.value = getMaxDate()
            } else dateTo.value = getMaxDate()
        } else dateTo.value = getMaxDate()
        loadTransactions(true)
    }

    private fun setDefaultDates() {
        dateFrom.value = getMinDateFrom()
        dateTo.value = getMaxDate()
        loadTransactions(true)
    }

    private fun getMinDateFrom(): String {
        val cal = Calendar.getInstance()
        cal.add(Calendar.MONTH, -1)
        cal.add(Calendar.DAY_OF_MONTH, -1)
        return cal.time.toString(pattern = DD_MM_YYYY)
    }

    private fun getMaxDate(): String = Date().toString(pattern = DD_MM_YYYY)

    fun loadTransactions(isNewRequest: Boolean) {
        if (isNewRequest) {
            transactionsList.clear()
            pages = 1
            pagesLoaded = 0
        }
        if (pages != pagesLoaded) {
            viewModelScope.launch {
                val startDate = dateFrom.value.toDate(pattern = DD_MM_YYYY)?.toString(pattern = FULL_DATE) ?: ""
                val finishDate = dateTo.value.toDate(pattern = DD_MM_YYYY)?.toString(pattern = FULL_DATE) ?: ""
                val request = TransactionRequest(
                    cardId = cardNo,
                    dateFrom = startDate.replace("00:00", "23:59"),
                    dateTo = finishDate.replace("00:00", "23:59"),
                    sort = "dt_created, desc",
                    page = pages - pagesLoaded,
                    size = 20
                )
                val transactionsFlow = transactionsRepository.getTransactions(request)
                transactionsFlow.collect {
                    when (it) {
                        is Resource.Loading -> _transactionState.value = TransactionsState(isLoading = true)
                        is Resource.LoadingMore -> _transactionState.value = TransactionsState(isLoadingMore = true)
                        is Resource.Success -> {
                            if (it.data.content.isNotEmpty()) {
                                it.data.content.map { transaction->
                                    if (!transactionsList.contains(transaction))
                                        transactionsList.add(transaction)
                                }
                                _transactionState.value = TransactionsState(transactions = transactionsList)
                                pages = it.data.totalPages
                                pagesLoaded++
                            } else {
                                _transactionState.value = TransactionsState(isEmptyList = true)
                            }
                        }
                        is Resource.Error -> {
                            val error = it.error?.errorMessage
                                ?: it.exception?.localizedMessage
                                ?: it.message
                            _transactionState.value = TransactionsState(error = error)
                        }
                    }
                }
            }
        }
    }

    fun loadBurnings() {
        viewModelScope.launch {
            val burningsFlow = transactionsRepository.getBurnings(cardId = cardId, itemsNo = 12)
            burningsFlow.collect {
                when (it) {
                    is Resource.Loading -> _burningState.value = BurningsState(isLoading = true)
                    is Resource.Success -> {
                        if (it.data.isNotEmpty()) _burningState.value = BurningsState(burnings = it.data)
                        else _burningState.value = BurningsState(isEmptyList = true)
                    }
                    is Resource.Error -> {
                        val error = it.error?.errorMessage
                            ?: it.exception?.localizedMessage
                            ?: it.message
                        _burningState.value = BurningsState(error = error)
                    }
                    else -> Unit
                 }
            }
        }
    }
}