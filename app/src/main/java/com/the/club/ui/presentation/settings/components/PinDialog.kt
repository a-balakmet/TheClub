package com.the.club.ui.presentation.settings.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.the.club.R
import com.the.club.ui.commonComponents.BoarderButton
import com.the.club.ui.commonComponents.NumPad
import com.the.club.ui.commonComponents.PinGroup
import com.the.club.ui.presentation.settings.SettingsViewModel
import com.the.club.ui.theme.Typography
import com.the.club.ui.theme.backgroundColor
import com.the.club.ui.theme.gray

@Composable
fun PinDialog(
    viewModel: SettingsViewModel,
    showDialog: Boolean,
    setShowDialog: (Boolean) -> Unit,
) {
    var pins by remember { mutableStateOf("") }
    if (showDialog) {
        val pinMessage by remember { viewModel.pinMessage }
        Box(modifier = Modifier
            .fillMaxSize()
            .background(color = backgroundColor())) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.TopCenter)
            ) {
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
                            R.string.ok -> setShowDialog(false)
                        }
                    }
                )
                // cancel button
                BoarderButton(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(all = 16.dp),
                    text = stringResource(id = R.string.cancel)
                ) {
                    viewModel.updatePinState(false)
                    setShowDialog(false)
                }
            }
        }
    }
}