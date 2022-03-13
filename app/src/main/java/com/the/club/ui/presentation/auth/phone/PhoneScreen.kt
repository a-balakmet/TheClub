package com.the.club.ui.presentation.auth.phone

import androidx.compose.foundation.background
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
import com.the.club.common.ktx.toClearPhone
import com.the.club.common.ktx.toPhoneFormat
import com.the.club.ui.commonComponents.NumPad
import com.the.club.ui.commonComponents.OneButtonDialog
import com.the.club.ui.commonComponents.RightBorder
import com.the.club.ui.commonComponents.StandardEnablingButton
import com.the.club.ui.navigation.Screen
import com.the.club.ui.theme.*

@Composable
fun PhoneScreen(navController: NavController) {
    val viewModel = hiltViewModel<PhoneScreenViewModel>()
    var phone by remember { mutableStateOf("") }
    var inputPhone by remember { mutableStateOf("".toPhoneFormat()) }
    var isCompletePhone by remember { mutableStateOf(false) }
    val phoneState = viewModel.checkState.value
    Box(modifier = Modifier.fillMaxSize()) {
        // header
        RightBorder(
            modifier = Modifier
                .fillMaxHeight()
                .align(Alignment.CenterEnd)
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 30.dp, top = 72.dp, end = 30.dp)
        ) {
            Text(
                text = stringResource(id = R.string.enter_your_number),
                style = Typography.h1,
                color = textColor()
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 30.dp)
                    .background(color = lightGray, shape = Shapes.large)
            ) {
                Text(
                    text = phone.ifEmpty { inputPhone },
                    style = Typography.h2,
                    color = textColor(),
                    modifier = Modifier.padding(all = 16.dp)
                )
            }
            Text(
                text = stringResource(id = R.string.number_confirmation_description),
                style = Typography.body1,
                color = gray,
                modifier = Modifier.padding(top = 8.dp)
            )

        }
        Column(modifier = Modifier
            .fillMaxWidth()
            .align(alignment = Alignment.BottomCenter)) {
            // confirm button
            StandardEnablingButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 30.dp, vertical = 16.dp),
                text = stringResource(id = R.string.confirm),
                isEnabled = isCompletePhone
            ) {
                viewModel.checkPhone(phone = inputPhone.toClearPhone())
            }
            // num pad
            NumPad(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    inputPhone = if (it != "-") "$inputPhone$it"
                    else inputPhone.dropLast(1)
                    phone = inputPhone.toPhoneFormat()
                    isCompletePhone = inputPhone.toClearPhone().length >= 11
                }
            )
        }
        if (phoneState.isLoading) {
            OneButtonDialog(text = stringResource(id = R.string.loading)) {}
        }
        if (phoneState.error.isNotBlank()) {
            val error = if (phoneState.error == noNetwork) stringResource(id = R.string.no_internet_connection) else phoneState.error
            OneButtonDialog(text = error) {
                navController.navigate(Screen.StartAuthScreen.route) { popUpTo(0) }
            }
        }
        LaunchedEffect(phoneState) {
            if (phoneState.emptyBody != null) {
                navController.navigate(Screen.OTPScreen.route) { popUpTo(0) }
            }
        }
    }
}