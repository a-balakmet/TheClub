package com.the.club.ui.presentation.auth.otp

import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.the.club.R
import com.the.club.common.CommonKeys.noNetwork
import com.the.club.common.ktx.toSmsLength
import com.the.club.ui.commonComponents.BackHandler
import com.the.club.ui.commonComponents.NumPad
import com.the.club.ui.commonComponents.OneButtonDialog
import com.the.club.ui.commonComponents.RightBorder
import com.the.club.ui.navigation.Screen
import com.the.club.ui.presentation.auth.otp.components.SmsGroup
import com.the.club.ui.presentation.auth.otp.components.SmsRetrieverUserConsentBroadcast
import com.the.club.ui.theme.Typography
import com.the.club.ui.theme.gray
import com.the.club.ui.theme.textColor

@Composable
fun OTPScreen(navController: NavController) {
    val viewModel = hiltViewModel<OtpViewModel>()
    var sms by remember { mutableStateOf("----") }
    var inputSms by remember { mutableStateOf("") }
    var navigationListener by remember { mutableStateOf<Boolean?>(null) }
    SmsRetrieverUserConsentBroadcast { _, code ->
        sms = code
        viewModel.checkOtp(code)
    }
    BackHandler(onBack = { navController.navigate(Screen.PhoneScreen.route) { popUpTo(0) } })
    Box(modifier = Modifier.fillMaxSize()) {
        RightBorder(
            modifier = Modifier
                .fillMaxHeight()
                .align(Alignment.CenterEnd)
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 30.dp, top = 96.dp, end = 30.dp)
        ) {
            Text(
                text = stringResource(id = R.string.otp_title),
                style = Typography.h2,
                color = textColor()
            )
            Text(
                text = stringResource(id = R.string.number_to_send, "+${viewModel.phone}"),
                style = Typography.body1,
                color = gray,
                modifier = Modifier.padding(top = 16.dp)
            )
            // sms input holder
            SmsGroup(modifier = Modifier.padding(top = 24.dp), sms = sms)
        }
        // num pad
        NumPad(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter),
            onClick = {
                inputSms = if (it != "-") "$inputSms$it" else inputSms.dropLast(1)
                sms = inputSms.toSmsLength()
                if (!sms.contains("-")) {
                    viewModel.checkOtp(sms)
                }
            }
        )
        // observe result of registration
        viewModel.registrationState.value.let {
            if (it.isLoading) OneButtonDialog(text = stringResource(id = R.string.loading)) {}
            if (it.error.isNotEmpty()) {
                val error = if (it.error == noNetwork) stringResource(id = R.string.no_internet_connection) else it.error
                OneButtonDialog(text = error) { navController.navigate(Screen.StartAuthScreen.route) { popUpTo(0) } }
            }
            if (it.cards != null && it.cards.isEmpty()) {
                navigationListener = false
                //navController.navigate(Screen.CardCreationScreen.route) { popUpTo(0) }
            }
            if (it.cards != null && it.cards.isNotEmpty()) {
                navigationListener = true
            }
        }
    }
    // navigator
    LaunchedEffect(navigationListener) {
        navigationListener?.let {
            if (it) navController.navigate(Screen.MainScreen.route) { popUpTo(0) }
            else navController.navigate(Screen.CardCreationScreen.route) { popUpTo(0) }
        }
    }
}
