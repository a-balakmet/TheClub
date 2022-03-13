package com.the.club.ui.presentation.auth.startAuth

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import com.the.club.common.sharedPreferences.PreferenceRepository
import com.the.club.common.sharedPreferences.PreferencesKeys.Companion.LOCALE
import javax.inject.Inject

@HiltViewModel
class StartAuthViewModel @Inject constructor(private val prefsRepository: PreferenceRepository) : ViewModel() {

    val locale = prefsRepository.getStringValue(LOCALE)

    fun setLocale(locale: String) {
        prefsRepository.setValue(LOCALE, locale)
    }
}