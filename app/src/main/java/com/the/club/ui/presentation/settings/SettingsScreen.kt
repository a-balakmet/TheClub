package com.the.club.ui.presentation.settings

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIos
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.the.club.R
import com.the.club.common.CommonKeys.mainBackKey
import com.the.club.common.CommonKeys.noNetwork
import com.the.club.common.CommonKeys.settingsBackKey
import com.the.club.common.DeviceHardware.hasBiometricCapability
import com.the.club.ui.commonComponents.*
import com.the.club.ui.presentation.settings.components.BlockCardDialog
import com.the.club.ui.presentation.settings.components.LocaleChoiceDialog
import com.the.club.ui.presentation.settings.components.PinDialog
import com.the.club.ui.theme.*

@Composable
fun SettingsScreen(navController: NavController, backScreen: String) {
    val viewModel = hiltViewModel<SettingsViewModel>()
    val context = LocalContext.current
    val isBiometric = hasBiometricCapability(context)
    val (showLocaleDialog, setShowLocaleDialog) = remember { mutableStateOf(false) }
    val (showPinDialog, setShowPinDialog) = remember { mutableStateOf(false) }
    val (showBlockDialog, setShowBlockDialog) = remember { mutableStateOf(false) }
    var updateCard = false
    LanguageSwitch(viewModel.localeState)
    BackHandler {
        if (updateCard) {
            when (backScreen) {
                "main" -> onBack(navController, mapOf(mainBackKey to "updateCards"))
                "settings" -> onBack(navController, mapOf(settingsBackKey to true))
            }
        } else navController.popBackStack()
    }
    Surface(color = MaterialTheme.colors.background) {
        Column {
            // toolbar
            MainToolbar(
                homeIcon = Icons.Filled.ArrowBackIos,
                onClickHome = {
                    if (updateCard) {
                        when (backScreen) {
                            "main" -> onBack(navController, mapOf(mainBackKey to "updateCards"))
                            "settings" -> onBack(navController, mapOf(settingsBackKey to true))
                        }
                    } else navController.popBackStack()
                },
                title = stringResource(id = R.string.settings),
                menuIcon = null,
                onClickIcon = { }
            )
            // settings switchers 
            Column(modifier = Modifier.padding(top = 48.dp, start = 24.dp, end = 24.dp)) {
                // switcher for fingerprint if exists
                if (isBiometric) {
                    Row() {
                        Column(modifier = Modifier.weight(1F)) {
                            Text(
                                text = stringResource(id = R.string.fingerprint_auth),
                                style = Typography.h2,
                                color = textColor(),
                            )
                            Text(
                                text = stringResource(id = R.string.fingerprint_auth_desc),
                                style = Typography.body1,
                                color = gray,
                            )
                        }
                        Switch(
                            checked = viewModel.biometricState,
                            onCheckedChange = {
                                viewModel.biometricState = it
                                viewModel.updateBiometricState(it)
                            },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = green,
                                checkedTrackAlpha = 0.5F,
                                uncheckedThumbColor = gray,
                                uncheckedTrackColor = gray.copy(alpha = 0.8F)
                            )
                        )
                    }
                    Divider(
                        modifier = Modifier.padding(vertical = 16.dp)
                    )
                }
                // switcher for authentication globally and for pin
                Row {
                    Text(
                        text = stringResource(id = R.string.pin_authorization),
                        style = Typography.h2,
                        color = textColor(),
                        modifier = Modifier
                            .weight(1F)
                            .align(alignment = Alignment.CenterVertically)
                    )
                    Switch(
                        checked = viewModel.authState,
                        onCheckedChange = {
                            viewModel.authState = it
                            viewModel.updatePinState(it)
                            if (it) {
                                viewModel.pinInitValues()
                                setShowPinDialog(true)
                            }
                        },
                        modifier = Modifier.align(alignment = Alignment.CenterVertically),
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = green,
                            checkedTrackAlpha = 0.5F,
                            uncheckedThumbColor = gray,
                            uncheckedTrackColor = gray.copy(alpha = 0.8F)
                        )
                    )
                }
                Divider(modifier = Modifier.padding(vertical = 16.dp))
                // change pin
                if (viewModel.authState) {
                    Row(modifier = Modifier
                        .clickable {
                            viewModel.pinInitValues()
                            setShowPinDialog(true)
                        }
                    ) {
                        Text(
                            text = stringResource(id = R.string.pin_change),
                            style = Typography.h2,
                            color = textColor(),
                            modifier = Modifier.weight(1F)
                        )
                        Icon(imageVector = Icons.Filled.ArrowForwardIos, contentDescription = "right", tint = gray)
                    }
                    Divider(
                        modifier = Modifier.padding(vertical = 16.dp)
                    )
                }
                // change locale
                Row(modifier = Modifier
                    //.padding(horizontal = 24.dp)
                    .clickable { setShowLocaleDialog(true) }
                ) {
                    Column(modifier = Modifier.weight(1F)) {
                        Row(modifier = Modifier.fillMaxWidth()) {
                            Text(
                                text = stringResource(id = R.string.locale),
                                style = Typography.h2,
                                color = textColor()
                            )
                            Spacer(modifier = Modifier.weight(1F))
                            Icon(
                                imageVector = Icons.Filled.ArrowForwardIos,
                                contentDescription = "right",
                                tint = gray,
                                modifier = Modifier.align(alignment = Alignment.CenterVertically)
                            )
                        }
                        Text(
                            text =
                            if (viewModel.localeState == "ru") stringResource(id = R.string.russian)
                            else stringResource(id = R.string.kazakh),
                            style = Typography.body1,
                            color = pink
                        )
                    }
                }
                Divider(
                    modifier = Modifier.padding(vertical = 16.dp)
                )
                // block / unblock bonus card
                Row(modifier = Modifier.padding(bottom = 16.dp)
                ) {
                    Text(
                        text = stringResource(id = R.string.block_card),
                        style = Typography.h2,
                        color = textColor(),
                        modifier = Modifier
                            .weight(1F)
                            .align(alignment = Alignment.CenterVertically)
                    )
                    Switch(
                        checked = viewModel.cardActivityState,
                        onCheckedChange = {
                            if (it) setShowBlockDialog(true) else viewModel.blockCard(false)
                        },
                        modifier = Modifier.align(alignment = Alignment.CenterVertically),
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = green,
                            checkedTrackAlpha = 0.5F,
                            uncheckedThumbColor = gray,
                            uncheckedTrackColor = gray.copy(alpha = 0.8F)
                        )
                    )
                }
            }
            viewModel.cardUpdateResult.value?.let {
                if (it == R.string.loading) {
                    OneButtonDialog(text = stringResource(id = R.string.loading)) {}
                } else {
                    val message = stringResource(id = it)
                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                    if (it == R.string.card_is_blocked || it == R.string.card_is_unblocked) {
                        updateCard = true
                    }
                }
            }

            if (viewModel.cardUpdateError.value.isNotBlank()) {
                val message = when (viewModel.cardUpdateError.value) {
                    noNetwork -> stringResource(id = R.string.no_internet_connection)
                    else -> viewModel.cardUpdateError.value
                }
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
            }
        }
        // locale choice dialog
        AnimatedVisibility(
            visible = showLocaleDialog,
            enter = slideInVertically(initialOffsetY = { it }),
            exit = slideOutVertically(targetOffsetY = { it })
        ) {
            LocaleChoiceDialog(
                showDialog = showLocaleDialog,
                setShowDialog = setShowLocaleDialog,
                locale = viewModel.localeState,
                onClick = { viewModel.updateLocale(it) }
            )
        }
        // dialog to set pin
        AnimatedVisibility(
            visible = showPinDialog,
            enter = slideInVertically(initialOffsetY = { it }),
            exit = slideOutVertically(targetOffsetY = { it })
        ) {
            PinDialog(viewModel = viewModel, showDialog = showPinDialog, setShowDialog = setShowPinDialog)
        }
        // dialog to confirm card blocking
        AnimatedVisibility(
            visible = showBlockDialog,
            enter = slideInVertically(initialOffsetY = { it }),
            exit = slideOutVertically(targetOffsetY = { it })
        ) {
            BlockCardDialog(
                showDialog = showBlockDialog,
                setShowDialog = setShowBlockDialog) {
                viewModel.blockCard(true)
            }
        }
    }
}