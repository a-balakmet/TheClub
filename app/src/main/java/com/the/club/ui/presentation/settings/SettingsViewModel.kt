package com.the.club.ui.presentation.settings

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import com.the.club.R
import com.the.club.common.model.Resource
import com.the.club.common.sharedPreferences.PreferenceRepository
import com.the.club.common.sharedPreferences.PreferencesKeys
import com.the.club.common.sharedPreferences.PreferencesKeys.Companion.FINGERPRINT
import com.the.club.common.sharedPreferences.PreferencesKeys.Companion.IS_AUTH
import com.the.club.common.sharedPreferences.PreferencesKeys.Companion.LOCALE
import com.the.club.common.sharedPreferences.PreferencesKeys.Companion.PIN_CODE
import com.the.club.domain.model.BonusCard
import com.the.club.domain.repository.ProfileRepository
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val prefsRepository: PreferenceRepository,
    private val profileRepository: ProfileRepository
) : ViewModel() {

    var biometricState by mutableStateOf(prefsRepository.getBooleanValue(FINGERPRINT))
    var authState by mutableStateOf(prefsRepository.getBooleanValue(IS_AUTH))
    var localeState by mutableStateOf(prefsRepository.getStringValue(LOCALE))
    private val mainCard: BonusCard = Gson().fromJson(prefsRepository.getStringValue(tag = PreferencesKeys.CARD), BonusCard::class.java)
    var pinMessage: MutableState<Int> = mutableStateOf(0)
    private var newPin = ""
    private var repeatedPin = ""
    var cardActivityState by mutableStateOf(!mainCard.isActive)
    var cardUpdateResult: MutableState<Int?> = mutableStateOf(null)
    var cardUpdateError: MutableState<String> = mutableStateOf("")

    fun updateBiometricState(value: Boolean) {
        prefsRepository.setValue(FINGERPRINT, value)
        if (value) {
            prefsRepository.setValue(IS_AUTH, true)
        }
    }

    fun updatePinState(value: Boolean) {
        prefsRepository.setValue(IS_AUTH, value)
        if (!value) {
            biometricState = false
            authState = false
            prefsRepository.setValue(PIN_CODE, "")
            prefsRepository.setValue(FINGERPRINT, false)
            pinInitValues()
        }
    }

    fun pinInitValues() {
        pinMessage.value = R.string.create_pin
        newPin = ""
        repeatedPin = ""
    }

    fun updateLocale(value: String) {
        localeState = value
        prefsRepository.setValue(LOCALE, value)
    }

    fun blockCard(value: Boolean) {
        viewModelScope.launch {
            val cardFlow =
                if (value) profileRepository.blockCard(cardId = mainCard.id)
                else profileRepository.unblockCard(cardId = mainCard.id)
            cardFlow.collect {
                when (it) {
                    is Resource.Loading -> {
                        cardUpdateResult.value = R.string.loading
                    }
                    is Resource.Success -> {
                        if (value) {
                            cardUpdateResult.value = R.string.card_is_blocked
                            cardActivityState = true
                        }
                        else {
                            cardUpdateResult.value = R.string.card_is_unblocked
                            cardActivityState = false
                        }
                    }
                    is Resource.Error -> {
                        cardUpdateResult.value = null
                        val error = it.error?.errorMessage
                            ?: it.exception?.localizedMessage
                            ?: it.message
                        cardUpdateError.value = error
                    }
                    else -> Unit
                }
            }
        }
    }

    fun isValidPin(enteredPin: String) {
        if (newPin == "") {
            if (enteredPin.length != 4) {
                pinMessage.value = R.string.create_pin
            } else {
                pinMessage.value = R.string.repeat_pin1
                newPin = enteredPin
            }
        } else {
            if (repeatedPin == "") {
                if (enteredPin.length != 4) {
                    pinMessage.value = R.string.repeat_pin2
                } else {
                    repeatedPin = enteredPin
                    if (newPin == repeatedPin) {
                        prefsRepository.setValue(PIN_CODE, repeatedPin)
                        pinMessage.value = R.string.ok
                    } else {
                        pinMessage.value = R.string.wrong_pin_repeat
                        newPin = ""
                        repeatedPin = ""
                    }
                }
            }
        }
    }
}

