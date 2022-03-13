package com.the.club.ui.presentation.auth.startAuth

import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.the.club.R
import com.the.club.ui.commonComponents.*
import com.the.club.ui.navigation.Screen
import com.the.club.ui.presentation.auth.startAuth.components.LanguageButtons
import com.the.club.ui.theme.*

@Composable
fun StartAuthScreen(navController: NavController) {
    val viewModel = hiltViewModel<StartAuthViewModel>()
    val locale = remember { mutableStateOf(viewModel.locale) }
    LanguageSwitch(locale.value)
    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxWidth().align(alignment = Alignment.TopCenter)) {
            // header
            StartToolbar(modifier = Modifier.fillMaxWidth())
            Box(modifier = Modifier.align(alignment = CenterHorizontally)) {
                MainLogo(
                    modifier = Modifier.align(alignment = Center),
                    colors = LoadingColorShades,
                    isStandard = true,
                    text = null,
                    textColor = semiTransparent
                )
            }
            Text(
                text = stringResource(id = R.string.language_selection),
                style = Typography.h1,
                modifier = Modifier
                    .align(CenterHorizontally)
                    .padding(top = 24.dp),
                textAlign = TextAlign.Center,
                color = textColor()
            )
            Text(
                text = stringResource(id = R.string.setup_app_language),
                style = Typography.h2,
                modifier = Modifier
                    .align(CenterHorizontally)
                    .padding(start = 60.dp, top = 8.dp, end = 60.dp),
                textAlign = TextAlign.Center,
                color = textColor()
            )
        }
        LanguageButtons(
            locale = locale.value,
            modifier = Modifier
                .fillMaxWidth()
                .align(Center)
                .padding(top = 64.dp, start = 25.dp, end = 25.dp),
            onClickRU = {
                locale.value = "ru"
                viewModel.setLocale("ru")
            },
            onClickKZ = {
                locale.value = "kk"
                viewModel.setLocale("kk")
            }
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp, start = 25.dp, end = 25.dp)
                .align(alignment = Alignment.BottomCenter)
        ) {
            StandardButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp)
                    .align(CenterHorizontally),
                text = stringResource(id = R.string.login),
                buttonColor = pink
            ) { navController.navigate(Screen.PhoneScreen.route) { popUpTo(0) } }
            NonBoarderButton(
                modifier = Modifier.align(CenterHorizontally),
                text = stringResource(id = R.string.registration)
            ) { navController.navigate(Screen.PhoneScreen.route) { popUpTo(0) } }
        }
    }
}
