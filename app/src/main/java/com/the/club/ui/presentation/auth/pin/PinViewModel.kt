package com.the.club.ui.presentation.auth.pin

import android.content.Context
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import com.the.club.R
import com.the.club.common.sharedPreferences.PreferenceRepository
import com.the.club.common.sharedPreferences.PreferencesKeys.Companion.PIN_CODE
import javax.inject.Inject
import kotlin.concurrent.thread

@HiltViewModel
class PinViewModel @Inject constructor(
    private val prefsRepository: PreferenceRepository,
) : ViewModel() {

    var savedPin = prefsRepository.getStringValue(PIN_CODE)
    var pinMessage: MutableState<Int> = mutableStateOf(0)
    private var newPin = ""
    private var repeatedPin = ""

    init {
        isValidPin("")
    }

    fun isValidPin(enteredPin: String) {
        if (savedPin == "") {
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
        } else {
            if (enteredPin.length != 4) {
                pinMessage.value = R.string.input_pin_code
            } else {
                if (enteredPin == savedPin) {
                    pinMessage.value = R.string.ok
                } else {
                    pinMessage.value = R.string.wrong_pin
                }
            }
        }
    }


    fun clearData(context: Context){
        thread {
            while (true) {
                Thread.sleep(100)
                viewModelScope.launch {
                    deleteAppData(context)
                }
            }
        }
    }

    private fun deleteAppData(ctx: Context) {
        try {
            val packageName = ctx.packageName
            val runtime = Runtime.getRuntime()
            runtime.exec("pm clear $packageName")
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}