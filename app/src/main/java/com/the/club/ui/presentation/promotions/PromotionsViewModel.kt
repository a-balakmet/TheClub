package com.the.club.ui.presentation.promotions

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import com.the.club.common.model.Resource
import com.the.club.domain.model.promos.Promotion
import com.the.club.domain.model.promos.PromotionRequest
import com.the.club.domain.repository.PromotionsRepository
import com.the.club.ui.presentation.promotions.states.PromotionsState
import javax.inject.Inject

@HiltViewModel
class PromotionsViewModel @Inject constructor(
    private val promotionsRepository: PromotionsRepository
): ViewModel() {

    private val _promotionsState = mutableStateOf(PromotionsState())
    val promotionsState: State<PromotionsState> = _promotionsState
    private var pages = 1
    private var pagesLoaded = 0
    private var promotionsList: ArrayList<Promotion> = ArrayList()

    init {
        loadPromotions(true)
    }

    fun loadPromotions(isNewRequest: Boolean) {
        if (isNewRequest) {
            promotionsList.clear()
            pages = 1
            pagesLoaded = 0
        }
        if (pages != pagesLoaded) {
            viewModelScope.launch {
                val request = PromotionRequest(
                    page = pages - pagesLoaded,
                    size = 20,
                    sort = "id",
                )
                val promotionsFlow = promotionsRepository.getPromotions(request)
                promotionsFlow.collect {
                    when (it) {
                        is Resource.Loading -> _promotionsState.value = PromotionsState(isLoading = true)
                        is Resource.LoadingMore -> _promotionsState.value = PromotionsState(isLoadingMore = true)
                        is Resource.Success -> {
                            if (it.data.content.isNotEmpty()) {
                                it.data.content.map { transaction->
                                    if (!promotionsList.contains(transaction))
                                        promotionsList.add(transaction)
                                }
                                _promotionsState.value = PromotionsState(promotions = promotionsList)
                                pages = it.data.totalPages
                                pagesLoaded++
                            } else {
                                _promotionsState.value = PromotionsState(isEmptyList = true)
                            }
                        }
                        is Resource.Error -> {
                            val error = it.error?.errorMessage
                                ?: it.exception?.localizedMessage
                                ?: it.message
                            _promotionsState.value = PromotionsState(error = error)
                        }
                    }
                }
            }
        }
    }
}