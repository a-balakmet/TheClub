package com.the.club.ui.presentation.auth.phone

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import com.the.club.common.model.CommonState
import com.the.club.common.model.EmptyBody
import com.the.club.common.model.Resource
import com.the.club.common.sharedPreferences.PreferenceRepository
import com.the.club.common.sharedPreferences.PreferencesKeys
import com.the.club.domain.use_case.auth.CheckPhoneUseCase
import javax.inject.Inject

@HiltViewModel
class PhoneScreenViewModel @Inject constructor(
    private val prefsRepository: PreferenceRepository,
    private val checkPhoneUseCase: CheckPhoneUseCase,
) : ViewModel() {

    private val _checkState = mutableStateOf(CommonState())
    val checkState: State<CommonState> = _checkState

    fun checkPhone(phone: String) {
        viewModelScope.launch {
            val checkFlow = checkPhoneUseCase.invoke(phone)
            checkFlow.collect {
                when (it) {
                    is Resource.Loading -> {
                        _checkState.value = CommonState(isLoading = true)
                    }
                    is Resource.Success -> {
                        _checkState.value = CommonState(emptyBody = EmptyBody())
                        prefsRepository.setValue(PreferencesKeys.PHONE_NUMBER, phone)
                    }
                    is Resource.Error -> {
                        val error = it.error?.errorMessage
                            ?: it.exception?.localizedMessage
                            ?: it.message
                        _checkState.value = CommonState(error = error)
                    }
                    else -> Unit
                }
            }
        }
    }
}