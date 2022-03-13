package com.the.club.ui.presentation.promotions

import android.os.CountDownTimer
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import com.the.club.common.model.Resource
import com.the.club.domain.repository.PromotionsRepository
import com.the.club.ui.presentation.promotions.states.PromotionDetailsState
import javax.inject.Inject

@HiltViewModel
class PromotionViewModel @Inject constructor(
    private val promotionsRepository: PromotionsRepository,
    savedStateHandle: SavedStateHandle
): ViewModel() {

    private val promotionId: Long = savedStateHandle["promoId"] ?: 1
    private val _promotionDetailsState = mutableStateOf(PromotionDetailsState())
    val promotionDetailsState: State<PromotionDetailsState> = _promotionDetailsState

    val counter = mutableStateOf(listOf<String>())

    init {
        loadPromotion()
    }

    private fun loadPromotion() {
        viewModelScope.launch {
            val promotionFlow = promotionsRepository.getPromotion(promotionId = promotionId)
            promotionFlow.collect {
                when(it) {
                    is Resource.Loading -> _promotionDetailsState.value = PromotionDetailsState(isLoading = true)
                    is Resource.Success -> {
                        _promotionDetailsState.value = PromotionDetailsState(promotionDetails = it.data)
                        countdownPromoEnd(it.data.endDate)
                    }
                    is Resource.Error -> {
                        val error = it.error?.errorMessage
                            ?: it.exception?.localizedMessage
                            ?: it.message
                        _promotionDetailsState.value = PromotionDetailsState(error = error)
                    }
                    else -> Unit
                }
            }
        }
    }

    private fun countdownPromoEnd(endTime: Long){
        var currentTime = System.currentTimeMillis()
        val timer = object : CountDownTimer(endTime, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                currentTime += 1
                val serverUptimeSeconds: Long = (millisUntilFinished - currentTime) / 1000
                val daysLeft = serverUptimeSeconds / 86400
                val hoursLeft = serverUptimeSeconds % 86400 / 3600
                val minutesLeft = serverUptimeSeconds % 86400 % 3600 / 60
                val secondsLeft = serverUptimeSeconds % 86400 % 3600 % 60
                val daysForCounter = when {
                    daysLeft < 10 -> "0$daysLeft"
                    daysLeft < 99 -> "$daysLeft"
                    else -> "99"
                }
                val hoursFoCounter = when {
                    hoursLeft > 9 -> "$hoursLeft"
                    else -> "0$hoursLeft"
                }
                val minutesFoCounter = when {
                    minutesLeft > 9 -> "$minutesLeft"
                    else -> "0$minutesLeft"
                }
                val secondsFoCounter = when {
                    secondsLeft > 9 -> "$secondsLeft"
                    else -> "0$secondsLeft"
                }
                counter.value = "$daysForCounter$hoursFoCounter$minutesFoCounter$secondsFoCounter".chunked(1)
            }
            override fun onFinish() {}
        }
        timer.start()
    }
}