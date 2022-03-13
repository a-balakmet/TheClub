package com.the.club.ui.presentation.auth.cardCreation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.the.club.R
import com.the.club.common.CommonKeys.cardAlreadyBind
import com.the.club.common.CommonKeys.cardNotFound
import com.the.club.common.CommonKeys.noNetwork
import com.the.club.ui.commonComponents.*
import com.the.club.ui.navigation.Screen
import com.the.club.ui.presentation.auth.cardCreation.components.BarcodeScanner
import com.the.club.ui.commonComponents.ButtonsDivider
import com.the.club.ui.theme.*

@ExperimentalPermissionsApi
@ExperimentalComposeUiApi
@Composable
fun CardCreationScreen(navController: NavController) {
    val viewModel = hiltViewModel<CardCreationViewModel>()
    var navigationListener by remember { mutableStateOf(false) }
    val (showScanner, setShowScanner) = remember { mutableStateOf(false) }
    val (showErrorDialog, setShowErrorDialog) = remember { mutableStateOf(false) }
    val noNetError = stringResource(id = R.string.no_internet_connection)
    var exchangeError = ""
    BackHandler(onBack = {
        if (showScanner) {
            setShowScanner(false)
        } else {
            navController.navigate(Screen.StartAuthScreen.route) { popUpTo(0) }
        }
    })
    var cardNumber by remember { mutableStateOf("") }
    val keyboardController = LocalSoftwareKeyboardController.current
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = backgroundColor())
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            StartToolbar(modifier = Modifier.fillMaxWidth())
            Box(modifier = Modifier.fillMaxSize()) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                ) {
                    // slogan
                    Text(
                        text = stringResource(id = R.string.get_bonuses),
                        style = Typography.h1,
                        color = textColor(),
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .padding(top = 24.dp)
                    )
                    // enter card line
                    Text(
                        text = stringResource(id = R.string.enter_card_number),
                        style = Typography.h5,
                        color = textColor(),
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .padding(top = 48.dp)
                    )
                    Column(modifier = Modifier.padding(all = 16.dp)) {
                        // card layout
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(brush = MainCardColors.toMainCardBrush(), shape = Shapes.large)
                        ) {
                            // club in 2 lines
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 24.dp)
                            ) {
                                Column(
                                    modifier = Modifier
                                        .weight(1F)
                                        .padding(4.dp)
                                ) {
                                    Text(
                                        text = "The",
                                        color = lightPink,
                                        fontSize = 40.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = "Club",
                                        color = lightPink,
                                        fontSize = 40.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                                // scan image
                                Image(
                                    painter = painterResource(id = R.drawable.ic_card_scan),
                                    contentDescription = "barcode",
                                    modifier = Modifier
                                        .padding(top = 16.dp)
                                        .clickable { setShowScanner(true) }
                                )
                            }
                            // row to input card number
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(start = 16.dp, end = 16.dp, bottom = 32.dp)
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(36.dp)
                                        .clickable { if (cardNumber.length == 16) cardNumber = "" },
                                    horizontalArrangement = Arrangement.Center
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .weight(2F)
                                            .height(36.dp)
                                            .background(color = backgroundColor(), shape = Shapes.medium)
                                    ) {
                                        Text(
                                            text =
                                            if (cardNumber.isEmpty()) ""
                                            else if (cardNumber.isNotEmpty() && cardNumber.length == 4) cardNumber
                                            else cardNumber.chunked(4)[0],
                                            style = Typography.h5,
                                            color = textColor(),
                                            textAlign = TextAlign.Center,
                                            modifier = Modifier.align(alignment = Alignment.Center),
                                        )
                                    }
                                    ButtonsDivider(modifier = Modifier.width(10.dp))
                                    Box(
                                        modifier = Modifier
                                            .weight(2F)
                                            .height(36.dp)
                                            .background(color = backgroundColor(), shape = Shapes.medium)
                                    ) {
                                        Text(
                                            text =
                                            if (cardNumber.isEmpty()) ""
                                            else if (cardNumber.isNotEmpty() && cardNumber.length > 4) cardNumber.chunked(4)[1]
                                            else "",
                                            style = Typography.h5,
                                            color = textColor(),
                                            textAlign = TextAlign.Center,
                                            modifier = Modifier.align(alignment = Alignment.Center)
                                        )
                                    }
                                    ButtonsDivider(modifier = Modifier.width(10.dp))
                                    Box(
                                        modifier = Modifier
                                            .weight(2F)
                                            .height(36.dp)
                                            .background(color = backgroundColor(), shape = Shapes.medium)
                                    ) {
                                        Text(
                                            text =
                                            if (cardNumber.isEmpty()) ""
                                            else if (cardNumber.isNotEmpty() && cardNumber.length > 8) cardNumber.chunked(4)[2]
                                            else "",
                                            style = Typography.h5,
                                            color = textColor(),
                                            textAlign = TextAlign.Center,
                                            modifier = Modifier.align(alignment = Alignment.Center)
                                        )
                                    }
                                    ButtonsDivider(modifier = Modifier.width(10.dp))
                                    Box(
                                        modifier = Modifier
                                            .weight(2F)
                                            .height(36.dp)
                                            .background(color = backgroundColor(), shape = Shapes.medium)
                                    ) {
                                        Text(
                                            text =
                                            if (cardNumber.isEmpty()) ""
                                            else if (cardNumber.isNotEmpty() && cardNumber.length > 12) cardNumber.chunked(4)[3]
                                            else "",
                                            style = Typography.h5,
                                            color = textColor(),
                                            textAlign = TextAlign.Center,
                                            modifier = Modifier.align(alignment = Alignment.Center)
                                        )
                                    }
                                }
                                TextField(
                                    value = cardNumber,
                                    onValueChange = {
                                        cardNumber = it
                                        if (cardNumber.length == 16) keyboardController?.hide()
                                    },
                                    textStyle = Typography.h4,
                                    enabled = true,
                                    singleLine = true,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(36.dp)
                                        .background(color = Color.Transparent, shape = Shapes.medium),
                                    keyboardOptions = KeyboardOptions(
                                        keyboardType = KeyboardType.Number,
                                    ),
                                    colors = TextFieldDefaults.textFieldColors(
                                        cursorColor = Color.Transparent,
                                        textColor = Color.Transparent,
                                        backgroundColor = Color.Transparent,
                                        focusedLabelColor = Color.Transparent,
                                        unfocusedLabelColor = Color.Transparent,
                                        focusedIndicatorColor = Color.Transparent,
                                        unfocusedIndicatorColor = Color.Transparent
                                    )
                                )
                            }
                        }
                        // buttons
                        StandardEnablingButton(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 24.dp),
                            text = stringResource(id = R.string.confirm),
                            isEnabled = cardNumber.length == 16,
                            onClick = { viewModel.bindCard(cardNo = cardNumber) }
                        )
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 24.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .weight(2F)
                                    .height(1.dp)
                                    .background(color = lightGray, shape = Shapes.small)
                                    .align(alignment = Alignment.CenterVertically)
                            )
                            Text(
                                text = stringResource(id = R.string.or),
                                style = Typography.h2,
                                color = textColor(),
                                modifier = Modifier.padding(horizontal = 16.dp)
                            )
                            Box(
                                modifier = Modifier
                                    .weight(2F)
                                    .height(1.dp)
                                    .background(color = lightGray, shape = Shapes.small)
                                    .align(alignment = Alignment.CenterVertically)
                            )
                        }
                        BoarderButton(modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 24.dp),
                            text = stringResource(id = R.string.create_new),
                            onClick = { viewModel.createNewCard() })
                        Text(
                            text = stringResource(id = R.string.issue_card),
                            color = gray,
                            style = Typography.body1,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 24.dp)
                        )
                    }
                }
                // observe results of loading bonus cards
                viewModel.cardState.value.let {
                    if (it.isLoading) {
                        OneButtonDialog(text = stringResource(id = R.string.loading)) {}
                    }
                    if (it.error.isNotBlank()) {
                        exchangeError =
                            when (it.error) {
                                noNetwork -> stringResource(id = R.string.no_internet_connection)
                                cardNotFound -> stringResource(id = R.string.card_not_found)
                                cardAlreadyBind -> stringResource(id = R.string.card_already_registered)
                                else -> it.error
                            }
                        setShowErrorDialog(true)
                    }
                    if (it.card != null) {
                        cardNumber = "${it.card.cardNumber}${it.card.cvs}"
                        navigationListener = true
                    }
                }
            }
        }
    }
    // camera to scan barcode
    AnimatedVisibility(
        visible = showScanner,
        enter = slideInVertically(initialOffsetY = { it }),
        exit = slideOutVertically(targetOffsetY = { it })
    ) {
        BarcodeScanner(showDialog = showScanner, setShowDialog = setShowScanner, onScanned = { cardNumber = it }, isBarcode = false)
    }
    // error dialog
    AnimatedVisibility(
        visible = showErrorDialog,
        enter = slideInVertically(initialOffsetY = { it }),
        exit = slideOutVertically(targetOffsetY = { it })
    ) {
        ErrorDialog(showDialog = showErrorDialog, setShowDialog = setShowErrorDialog, text = exchangeError) {
            viewModel.dropError()
            setShowErrorDialog(false)
            if (exchangeError == noNetError) navController.navigate(Screen.StartAuthScreen.route) { popUpTo(0) }
            else cardNumber = ""
        }
    }
    // navigator
    LaunchedEffect(navigationListener) {
        if (navigationListener) {
            navController.navigate(Screen.CardCreatedScreen.route + "/$cardNumber") { popUpTo(0) }
        }
    }
}
