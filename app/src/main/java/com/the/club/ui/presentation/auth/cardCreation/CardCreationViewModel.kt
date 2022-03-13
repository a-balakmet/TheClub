package com.the.club.ui.presentation.auth.cardCreation

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import com.the.club.common.CommonKeys.cardAlreadyBind
import com.the.club.common.CommonKeys.cardNotFound
import com.the.club.common.model.Resource
import com.the.club.domain.repository.BonusCardsRepository
import com.the.club.ui.presentation.auth.cardCreation.state.CardState
import javax.inject.Inject

@HiltViewModel
class CardCreationViewModel @Inject constructor(
    private val cardsRepository: BonusCardsRepository,
) : ViewModel() {

    private val _cardState = mutableStateOf(CardState())
    val cardState: State<CardState> = _cardState

    fun createNewCard() {
        viewModelScope.launch {
            val cardFlow = cardsRepository.createBonusCards()
            cardFlow.collect {
                when (it) {
                    is Resource.Loading -> _cardState.value = CardState(isLoading = true)
                    is Resource.Success -> _cardState.value = CardState(card = it.data)
                    is Resource.Error -> {
                        val error = it.error?.errorMessage
                            ?: it.exception?.localizedMessage
                            ?: it.message
                        _cardState.value = CardState(error = error)
                    }
                    else -> Unit
                }
            }
        }
    }

    fun bindCard(cardNo: String) {
        viewModelScope.launch {
            val cardFlow = cardsRepository.bindBonusCards(cardNo.toLong())
            cardFlow.collect {
                when (it) {
                    is Resource.Loading -> _cardState.value = CardState(isLoading = true)
                    is Resource.Success -> _cardState.value = CardState(card = it.data)
                    is Resource.Error -> {
                        val error =
                            when (it.error?.errorCode) {
                                cardNotFound -> cardNotFound
                                cardAlreadyBind -> cardAlreadyBind
                                else -> {
                                    it.error?.errorMessage
                                        ?: it.exception?.localizedMessage
                                        ?: it.message
                                }
                            }
                        _cardState.value = CardState(error = error)
                    }
                    else -> Unit
                }
            }
        }
    }

    fun dropError() {
        _cardState.value = CardState(error = "")
    }
}