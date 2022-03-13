package com.the.club.ui.presentation.start

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import com.the.club.common.sharedPreferences.PreferenceRepository
import com.the.club.common.sharedPreferences.PreferencesKeys.Companion.ACCESS_TOKEN
import com.the.club.common.sharedPreferences.PreferencesKeys.Companion.FINGERPRINT
import com.the.club.common.sharedPreferences.PreferencesKeys.Companion.IS_AUTH
import com.the.club.common.sharedPreferences.PreferencesKeys.Companion.LOCALE
import javax.inject.Inject

@HiltViewModel
class StartViewModel @Inject constructor(private val prefsRepository: PreferenceRepository) : ViewModel() {
    val isRegistered = prefsRepository.getStringValue(ACCESS_TOKEN).isNotEmpty()
    val isAuth = prefsRepository.getBooleanValue(IS_AUTH)
    init {
        if (prefsRepository.getStringValue(LOCALE) == "") {
            prefsRepository.setValue(LOCALE, "ru")
        }
        if (!prefsRepository.isValueExists(FINGERPRINT)) {
            prefsRepository.setValue(FINGERPRINT, false)
        }
        if (!prefsRepository.isValueExists(IS_AUTH)) {
            prefsRepository.setValue(IS_AUTH, false)
        }
    }

    fun isFinger(): Boolean = prefsRepository.getBooleanValue(FINGERPRINT)
}