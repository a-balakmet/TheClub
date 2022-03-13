package com.the.club

import android.annotation.SuppressLint
import android.content.Context
import android.content.ContextWrapper
import android.content.pm.ActivityInfo
import android.hardware.biometrics.BiometricPrompt
import android.os.Build
import android.os.Bundle
import android.os.CancellationSignal
import android.provider.Settings
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.ui.ExperimentalComposeUiApi
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import dagger.hilt.android.AndroidEntryPoint
import com.the.club.common.ContextUtils
import com.the.club.common.sharedPreferences.PreferenceHelper
import com.the.club.common.sharedPreferences.PreferenceHelper.set
import com.the.club.common.sharedPreferences.PreferencesKeys.Companion.FINGERPRINT
import com.the.club.common.sharedPreferences.PreferencesKeys.Companion.LOCALE
import com.the.club.ui.navigation.Navigation
import com.the.club.ui.theme.TheClubTheme
import java.util.*
import kotlin.system.exitProcess

@AndroidEntryPoint
@ExperimentalFoundationApi
@ExperimentalComposeUiApi
@ExperimentalPermissionsApi
@ExperimentalMaterialApi
class MainActivity : AppCompatActivity() {

    private var cancellationSignal: CancellationSignal? = null

    @RequiresApi(Build.VERSION_CODES.P)
    @SuppressLint("SourceLockedOrientationActivity", "MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_TheClub)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        val prefs = PreferenceHelper.customPrefs(this, "club_prefs")
        val isFinger = prefs.getBoolean(FINGERPRINT, false)
        if(prefs.getString("androidID", null) == null) {
            val mId = Settings.Secure.getString(contentResolver, Settings.Secure.ANDROID_ID)
            prefs["androidID"] = mId
        }
        if (isFinger) {
            val authenticationCallback: BiometricPrompt.AuthenticationCallback =
                @RequiresApi(Build.VERSION_CODES.P)
                object : BiometricPrompt.AuthenticationCallback() {
                    override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult?) {
                        super.onAuthenticationSucceeded(result)
                        setContent {
                            TheClubTheme {
                                Navigation()
                            }
                        }
                    }
                    override fun onAuthenticationError(errorCode: Int, errString: CharSequence?) {
                        super.onAuthenticationError(errorCode, errString)
                        Toast.makeText(this@MainActivity, "${getString(R.string.error)}: $errorCode", Toast.LENGTH_SHORT).show()
                    }
                    /*override fun onAuthenticationFailed() {
                        super.onAuthenticationFailed()
                    }*/
                    override fun onAuthenticationHelp(helpCode: Int, helpString: CharSequence?) {
                        super.onAuthenticationHelp(helpCode, helpString)
                    }
                }
            val biometricPrompt = BiometricPrompt.Builder(this)
                .apply {
                    setTitle(getString(R.string.login_put_finger_scanner))
                    setNegativeButton(getString(R.string.cancel), mainExecutor) { _, _ ->
                        exitProcess(0)
                    }
                }.build()

            biometricPrompt.authenticate(getCancellationSignal(), mainExecutor, authenticationCallback)

        } else {
            setContent {
                TheClubTheme {
                    Navigation()
                }
            }
        }
    }

    private fun getCancellationSignal(): CancellationSignal {
        cancellationSignal = CancellationSignal()
        cancellationSignal?.setOnCancelListener {
            Toast.makeText(this, "Authentication Cancelled Signal", Toast.LENGTH_SHORT).show()
        }

        return cancellationSignal as CancellationSignal
    }

    override fun attachBaseContext(newBase: Context) {
        val prefs = PreferenceHelper.customPrefs(context = newBase, name = "club_prefs")
        val locale = prefs.getString(LOCALE, "ru")!!
        val localeToSwitchTo = Locale(locale)
        val localeUpdatedContext: ContextWrapper = ContextUtils.updateLocale(newBase, localeToSwitchTo)
        super.attachBaseContext(localeUpdatedContext)
    }
}