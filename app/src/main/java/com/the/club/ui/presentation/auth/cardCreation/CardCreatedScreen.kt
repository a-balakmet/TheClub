package com.the.club.ui.presentation.auth.cardCreation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.the.club.R
import com.the.club.common.ktx.mergeNumber
import com.the.club.common.ktx.toChunked
import com.the.club.data.remote.bonusCards.dto.toBitmap
import com.the.club.domain.model.BonusCard
import com.the.club.ui.commonComponents.StandardButton
import com.the.club.ui.commonComponents.StartToolbar
import com.the.club.ui.navigation.Screen
import com.the.club.ui.presentation.main.components.BonusCardItem
import com.the.club.ui.commonComponents.ButtonsDivider
import com.the.club.ui.theme.*

@Composable
fun CardCreatedScreen(navController: NavController, cardNumber: String) {
    var navigationListener by remember { mutableStateOf<Boolean?>(null) }
    val number = cardNumber.dropLast(3)
    val newCard = BonusCard(
        id = 1,
        cardNumber = ("$number***").toChunked(4),
        cvs = "777",
        balance = 0,
        barcode = mergeNumber(cardNumber, "").toBitmap(),
        type = 1 ,
        isActive = true,
        status = 1
    )
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = backgroundColor())
    ) {
        StartToolbar(modifier = Modifier.fillMaxWidth())
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            // slogan
            Text(
                text = stringResource(id = R.string.your_card_is_ready),
                style = Typography.h1,
                textAlign = TextAlign.Center,
                color = textColor(),
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(start = 64.dp, top = 24.dp, end = 64.dp)
            )
            // card scan on cash desk
            Text(
                text = stringResource(id = R.string.scan_for_getting_bonuses),
                style = Typography.body1,
                color = textColor(),
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(top = 16.dp, bottom = 64.dp)
            )
            Column(modifier = Modifier.padding(all = 16.dp)) {
                // card layout
                BonusCardItem(modifier = Modifier.fillMaxWidth(), bonusCard = newCard)
                // text about profile
                Text(
                    text = stringResource(id = R.string.fill_profile),
                    style = Typography.h1,
                    color = textColor(),
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 36.dp, top = 24.dp, end = 36.dp)
                )
                Text(
                    text = stringResource(id = R.string.fill_profile_descr),
                    style = Typography.body1,
                    color = textColor(),
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp)
                )
                // buttons
                Row(modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 24.dp)
                ) {
                    StandardButton(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1F),
                        text = stringResource(id = R.string.fill),
                        buttonColor = pink,
                        onClick = { navigationListener = true }
                    )
                    ButtonsDivider(modifier = Modifier.width(10.dp))
                    StandardButton(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1F),
                        text = stringResource(id = R.string.later),
                        buttonColor = lightGray,
                        onClick = { navigationListener = false }
                    )
                }
            }
        }
    }
    // navigator
    LaunchedEffect(navigationListener) {
        navigationListener?.let{
            if (it) navController.navigate(Screen.CreateProfileScreen.route)
            else navController.navigate(Screen.MainScreen.route) { popUpTo(0) }
        }
    }
}