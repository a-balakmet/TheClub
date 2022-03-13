package com.the.club.ui.presentation.auth.otp.components

import android.app.Activity
import android.content.*
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import com.google.android.gms.auth.api.phone.SmsRetriever
import com.google.android.gms.common.api.CommonStatusCodes
import com.google.android.gms.common.api.Status

@Composable
fun SmsRetrieverUserConsentBroadcast(
    smsCodeLength: Int = 4,
    onSmsReceived: (message: String, code: String) -> Unit,
) {
    val context = LocalContext.current
    var shouldRegisterReceiver by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        SmsRetriever.getClient(context)
            .startSmsUserConsent(null)
            .addOnSuccessListener {
                shouldRegisterReceiver = true
            }
    }
    val launcher =
        rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK && it.data != null) {
                val message: String? = it.data!!.getStringExtra(SmsRetriever.EXTRA_SMS_MESSAGE)
                message?.let {
                    val verificationCode = getVerificationCodeFromSms(message, smsCodeLength)

                    onSmsReceived(message, verificationCode)
                }
                shouldRegisterReceiver = false
            }
        }
    if (shouldRegisterReceiver) {
        SystemBroadcastReceiver(
            systemAction = SmsRetriever.SMS_RETRIEVED_ACTION,
            broadCastPermission = SmsRetriever.SEND_PERMISSION,
        ) { intent ->
            if (intent != null && SmsRetriever.SMS_RETRIEVED_ACTION == intent.action) {
                val extras = intent.extras

                val smsRetrieverStatus = extras?.get(SmsRetriever.EXTRA_STATUS) as Status
                when (smsRetrieverStatus.statusCode) {
                    CommonStatusCodes.SUCCESS -> {
                        val consentIntent = extras.getParcelable<Intent>(SmsRetriever.EXTRA_CONSENT_INTENT)
                        try {
                            launcher.launch(consentIntent)
                        } catch (e: ActivityNotFoundException) {
                            println(e)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SystemBroadcastReceiver(
    systemAction: String,
    broadCastPermission: String,
    onSystemEvent: (intent: Intent?) -> Unit
) {
    val context = LocalContext.current
    DisposableEffect(context, systemAction) {
        val intentFilter = IntentFilter(systemAction)
        val broadcast = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                onSystemEvent(intent)
            }
        }
        context.registerReceiver(broadcast, intentFilter)
        onDispose { context.unregisterReceiver(broadcast) }
    }
    DisposableEffect(context, broadCastPermission) {
        val intentFilter = IntentFilter(broadCastPermission)
        val broadcast = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                onSystemEvent(intent)
            }
        }
        context.registerReceiver(broadcast, intentFilter)
        onDispose { context.unregisterReceiver(broadcast) }
    }

}

internal fun getVerificationCodeFromSms(sms: String, smsCodeLength: Int): String =
    sms.filter { it.isDigit() }.substring(0 until smsCodeLength)