package com.the.club.ui.presentation.auth.otp

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import com.the.club.common.model.Resource
import com.the.club.common.sharedPreferences.PreferenceRepository
import com.the.club.common.sharedPreferences.PreferencesKeys
import com.the.club.common.sharedPreferences.PreferencesKeys.Companion.ACCESS_TOKEN
import com.the.club.common.sharedPreferences.PreferencesKeys.Companion.DEVICE_REGISTRATION
import com.the.club.common.sharedPreferences.PreferencesKeys.Companion.REFRESH_TOKEN
import com.the.club.data.remote.auth.dto.TokensDto
import com.the.club.domain.repository.AuthRepository
import com.the.club.domain.repository.BonusCardsRepository
import com.the.club.ui.presentation.auth.otp.states.RegistrationState
import javax.inject.Inject

@HiltViewModel
class OtpViewModel @Inject constructor(
    private val prefsRepository: PreferenceRepository,
    private val authRepository: AuthRepository,
    private val bonusCardsRepository: BonusCardsRepository,
) : ViewModel() {

    val phone = prefsRepository.getStringValue(PreferencesKeys.PHONE_NUMBER)

    private val _registrationState = mutableStateOf(RegistrationState())
    val registrationState: State<RegistrationState> = _registrationState

    fun checkOtp(sms: String) {
        viewModelScope.launch {
            val otpFlow = authRepository.checkOTP(otp = sms, phone = phone)
            otpFlow.collect {
                when (it) {
                    is Resource.Loading -> _registrationState.value = RegistrationState(isLoading = true)
                    is Resource.Success -> firebaseRegistration(it.data)
                    is Resource.Error -> {
                        val error = it.error?.errorMessage
                            ?: it.exception?.localizedMessage
                            ?: it.message
                        _registrationState.value = RegistrationState(error = error)
                    }
                    else -> Unit
                }
            }
        }
    }

    private fun firebaseRegistration(tokens: TokensDto) {
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task->
            if (!task.isSuccessful) {
                _registrationState.value = RegistrationState(error = "Ошибка регистрации в FireBase ${task.exception}")
            }
            if (task.isSuccessful) {
                prefsRepository.setValue(ACCESS_TOKEN, tokens.authToken)
                prefsRepository.setValue(REFRESH_TOKEN, tokens.refreshToken)
                val fbToken: String? = task.result
                if (fbToken != null) {
                    val deviceId = prefsRepository.getStringValue("androidID")
                    checkDeviceInfo(fbToken = task.result, deviceId = deviceId)
                    //checkDeviceInfo(fbToken = task.result, deviceId = FirebaseInstallations.getInstance().id.result)
                }
            } else {
                _registrationState.value = RegistrationState(error = "Ошибка регистрации в FireBase")
            }
        }
    }

    private fun checkDeviceInfo(fbToken: String, deviceId: String) {
        viewModelScope.launch {
            val deviceFlow = authRepository.checkDevice(fbToken = fbToken, deviceId = deviceId)
            deviceFlow.collect {
                when (it) {
                    is Resource.Loading -> {
                        _registrationState.value = RegistrationState(isLoading = true)
                    }
                    is Resource.Success -> {
                        prefsRepository.setValue(DEVICE_REGISTRATION, true)
                        getCards()
                    }
                    is Resource.Error -> {
                        val error = it.error?.errorMessage
                            ?: it.exception?.localizedMessage
                            ?: it.message
                        _registrationState.value = RegistrationState(error = error)
                    }
                    else -> Unit
                }
            }
        }
    }

    private fun getCards() {
        viewModelScope.launch {
            val cardsFlow = bonusCardsRepository.getBonusCards()
            cardsFlow.collect {
                when (it) {
                    is Resource.Loading -> _registrationState.value = RegistrationState(isLoading = true)
                    is Resource.Success -> {
                        _registrationState.value = RegistrationState(cards = it.data)
                    }
                    is Resource.Error -> {
                        val error = it.error?.errorMessage
                            ?: it.exception?.localizedMessage
                            ?: it.message
                        _registrationState.value = RegistrationState(error = error)
                    }
                    else -> Unit
                }
            }
        }
    }
}