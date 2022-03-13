package com.the.club.ui.presentation.transactions

import androidx.compose.runtime.*
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import com.the.club.common.model.Resource
import com.the.club.domain.repository.ShopsRepository
import com.the.club.domain.repository.TransactionsRepository
import com.the.club.ui.presentation.transactions.states.TransactionProductsState
import com.the.club.ui.presentation.transactions.states.WinningState
import javax.inject.Inject

@HiltViewModel
class TransactionProductsViewModel @Inject constructor(
    private val transactionsRepository: TransactionsRepository,
    private val shopsRepository: ShopsRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val transactionId: Long = savedStateHandle["transactionId"] ?: 1
    private val shopId: Long = savedStateHandle["shopId"] ?: 1

    private val _productsState = mutableStateOf(TransactionProductsState())
    val productsState: State<TransactionProductsState> = _productsState

    private val _winningState = mutableStateOf(WinningState())
    val winningState: State<WinningState> = _winningState

    var shopAddress by mutableStateOf("")

    init {
        getProductsOfTransaction()
    }

    private fun getProductsOfTransaction() {
        viewModelScope.launch {
            val productsFlow = transactionsRepository.getProducts(transactionId)
            productsFlow.collect {
                when (it) {
                    is Resource.Loading -> _productsState.value = TransactionProductsState(isLoading = true)
                    is Resource.Success -> {
                        if (it.data.isNotEmpty()) {
                            _productsState.value = TransactionProductsState(products = it.data)
                            getShopOfTransaction()
                            getWinningChances()
                        } else {
                            _productsState.value = TransactionProductsState(isEmptyList = true)
                        }
                    }
                    is Resource.Error -> {
                        val error = it.error?.errorMessage
                            ?: it.exception?.localizedMessage
                            ?: it.message
                        _productsState.value = TransactionProductsState(error = error)
                    }
                    else -> Unit
                }
            }
        }
    }

    private fun getShopOfTransaction(){
        viewModelScope.launch {
            val shopFlow = shopsRepository.getShopById(id = shopId)
            shopFlow.collect {
                when (it) {
                    is Resource.Success -> shopAddress = it.data.address ?: ""
                    else -> Unit
                }
            }
        }
    }

    private fun getWinningChances() {
        viewModelScope.launch {
            val productsFlow = transactionsRepository.getWinChance(transactionId)
            productsFlow.collect {
                when (it) {
                    is Resource.Success -> {
                        if (it.data.isNotEmpty()) {
                            _winningState.value = WinningState(chances = it.data)
                        } else {
                            _winningState.value = WinningState(isEmptyList = true)
                        }
                    }
                    else -> Unit
                }
            }
        }
    }
}