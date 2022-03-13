package com.the.club.ui.presentation.start

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.the.club.R
import com.the.club.ui.navigation.Screen

@Composable
fun StartScreen(navController: NavController) {
    val viewModel = hiltViewModel<StartViewModel>()
    Image(
        painter = painterResource(id = R.drawable.splash_screen),
        contentDescription = "splash",
        contentScale = ContentScale.FillBounds,
        modifier = Modifier.fillMaxSize())
    if (viewModel.isRegistered) {
        if (viewModel.isAuth) {
            if (viewModel.isFinger()) navController.navigate(Screen.MainScreen.route) { popUpTo(0) }
            else run { navController.navigate(Screen.PinScreen.route) { popUpTo(0) } }
        } else run {
            navController.navigate(Screen.MainScreen.route) { popUpTo(0) }
        }
    } else run {
        navController.navigate(Screen.StartAuthScreen.route) { popUpTo(0) }
    }
}