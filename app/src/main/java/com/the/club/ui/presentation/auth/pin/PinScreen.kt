package com.the.club.ui.presentation.auth.pin

import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.the.club.R
import com.the.club.ui.commonComponents.NonBoarderButton
import com.the.club.ui.commonComponents.NumPad
import com.the.club.ui.commonComponents.PinGroup
import com.the.club.ui.commonComponents.StartToolbar
import com.the.club.ui.navigation.Screen
import com.the.club.ui.theme.Typography
import com.the.club.ui.theme.gray

@Composable
fun PinScreen(navController: NavController) {
    // vars
    val viewModel = hiltViewModel<PinViewModel>()
    val pinMessage by remember { viewModel.pinMessage }
    var pins by remember { mutableStateOf("") }
    val context = LocalContext.current
    // screen
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopCenter)
        ) {
            // toolbar
            StartToolbar(modifier = Modifier.fillMaxWidth())
            // pin group
            PinGroup(
                modifier = Modifier
                    .padding(vertical = 36.dp)
                    .align(Alignment.CenterHorizontally),
                length = pins.length
            )
            Text(
                text = stringResource(id = pinMessage),
                style = Typography.body2,
                color = gray,
                modifier = Modifier
                    .padding(top = 24.dp)
                    .align(Alignment.CenterHorizontally)
            )
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(alignment = Alignment.BottomCenter)
        ) {
            // num pad
            NumPad(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    pins = if (it != "-") "$pins$it" else pins.dropLast(1)
                    viewModel.isValidPin(pins)
                    when (pinMessage) {
                        R.string.wrong_pin_repeat, R.string.repeat_pin1 -> pins = ""
                    }
                }
            )
            // bottom button if pin is forgotten
            if (viewModel.savedPin != "") {
                NonBoarderButton(
                    modifier = Modifier
                        .align(alignment = Alignment.CenterHorizontally)
                        .padding(vertical = 16.dp),
                    text = stringResource(id = R.string.forget_pin_code)
                ) {
                    viewModel.clearData(context)
                }
            }
        }

    }
    // listener to navigate next screen
    LaunchedEffect(viewModel.pinMessage.value) {
        when (viewModel.pinMessage.value) {
            R.string.ok -> {
                navController.navigate(Screen.MainScreen.route) { popUpTo(0) }
            }
        }
    }

}