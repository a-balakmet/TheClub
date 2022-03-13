package com.the.club.ui.presentation.surveys

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import com.the.club.common.model.Resource
import com.the.club.domain.repository.SurveysRepository
import com.the.club.ui.presentation.surveys.states.SurveysState
import javax.inject.Inject

@HiltViewModel
class SurveysViewModel @Inject constructor(
    private val surveysRepository: SurveysRepository
): ViewModel() {

    private val _surveysState = mutableStateOf(SurveysState())
    val surveysState: State<SurveysState> = _surveysState

    fun getSurveys(){
        viewModelScope.launch {
            val surveysFlow = surveysRepository.getAvailableSurveys()
            surveysFlow.collect {
                when (it) {
                    is Resource.Loading -> _surveysState.value = SurveysState(isLoading = true)
                    is Resource.Success -> {
                        if (it.data.isNotEmpty())
                            _surveysState.value = SurveysState(surveys = it.data)
                        else
                            _surveysState.value = SurveysState(isEmptyList = true)
                    }
                    is Resource.Error -> {
                        val error = it.error?.errorMessage
                            ?: it.exception?.localizedMessage
                            ?: it.message
                        _surveysState.value = SurveysState(error = error)
                    }
                    else -> Unit
                }
            }
        }
    }

}