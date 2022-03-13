package com.the.club.ui.navigation

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.gson.Gson
import com.the.club.data.remote.profile.dto.ProfileDto
import com.the.club.domain.model.transactions.Transaction
import com.the.club.ui.presentation.addresses.MapScreen
import com.the.club.ui.presentation.auth.cardCreation.CardCreatedScreen
import com.the.club.ui.presentation.auth.cardCreation.CardCreationScreen
import com.the.club.ui.presentation.auth.otp.OTPScreen
import com.the.club.ui.presentation.auth.phone.PhoneScreen
import com.the.club.ui.presentation.auth.pin.PinScreen
import com.the.club.ui.presentation.auth.startAuth.StartAuthScreen
import com.the.club.ui.presentation.clubs.ClubScreen
import com.the.club.ui.presentation.clubs.ClubsScreen
import com.the.club.ui.presentation.main.MainScreen
import com.the.club.ui.presentation.notifications.NotificationsScreen
import com.the.club.ui.presentation.priceCheck.PriceCheckScreen
import com.the.club.ui.presentation.profile.CreateProfileScreen
import com.the.club.ui.presentation.profile.EditProfileScreen
import com.the.club.ui.presentation.profile.UserProfileScreen
import com.the.club.ui.presentation.promotions.PromotionScreen
import com.the.club.ui.presentation.promotions.PromotionsScreen
import com.the.club.ui.presentation.settings.SettingsScreen
import com.the.club.ui.presentation.start.StartScreen
import com.the.club.ui.presentation.surveys.SurveyScreen
import com.the.club.ui.presentation.surveys.SurveysScreen
import com.the.club.ui.presentation.transactions.TransactionProductsScreen
import com.the.club.ui.presentation.transactions.TransactionsScreen

@ExperimentalMaterialApi
@ExperimentalPermissionsApi
@ExperimentalComposeUiApi
@ExperimentalFoundationApi
@Composable
fun Navigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Screen.StartScreen.route) {
        // start
        composable(route = Screen.StartScreen.route) { StartScreen(navController = navController) }
        // login
        composable(route = Screen.StartAuthScreen.route) { StartAuthScreen(navController = navController) }
        composable(route = Screen.PhoneScreen.route) { PhoneScreen(navController = navController) }
        composable(route = Screen.OTPScreen.route) { OTPScreen(navController = navController) }
        // new card
        composable(route = Screen.CardCreationScreen.route) { CardCreationScreen(navController = navController) }
        composable(
            route = Screen.CardCreatedScreen.route + "/{cardNumber}",
            arguments = listOf(navArgument("cardNumber") { type = NavType.StringType })
        ) {
            it.arguments?.getString("cardNumber")?.let { cardNo ->
                CardCreatedScreen(navController = navController, cardNumber = cardNo)
            }
        }
        // profile creation
        composable(route = Screen.CreateProfileScreen.route) { CreateProfileScreen(navController = navController) }
        // pin screen to enter
        composable(route = Screen.PinScreen.route) { PinScreen(navController = navController) }
        // application
        composable(route = Screen.MainScreen.route) {
            MainScreen(navController = navController)
        }
        composable(route = Screen.PromotionsScreen.route) { PromotionsScreen(navController = navController) }
        composable(
            route = Screen.PromotionScreen.route + "/{promoId}",
            arguments = listOf(navArgument("promoId") { type = NavType.LongType })
        ) {
            PromotionScreen(navController = navController)
        }
        composable(route = Screen.ClubsScreen.route) {
            ClubsScreen(navController = navController, savedStateHandle = navController.currentBackStackEntry?.savedStateHandle)
        }
        composable(
            route = Screen.ClubScreen.route + "/{clubId}",
            arguments = listOf(navArgument("clubId") { type = NavType.IntType })
        ) {
            ClubScreen(navController = navController)
        }
        composable(route = Screen.PriceCheckScreen.route) {
            PriceCheckScreen(navController = navController)
        }
        composable(
            route = Screen.TransactionsScreen.route + "/{bonuses}/{cardNo}/{cardId}",
            arguments = listOf(
                navArgument("bonuses") { type = NavType.LongType },
                navArgument("cardNo") { type = NavType.LongType },
                navArgument("cardId") { type = NavType.LongType }
            )
        ) {
            it.arguments?.getLong("bonuses")?.let { bonuses ->
                TransactionsScreen(navController = navController, bonuses = bonuses)
            }
        }
        composable(
            route = Screen.TransactionProductsScreen.route + "/{transaction}/{transactionId}/{shopId}",
            arguments = listOf(
                navArgument("transaction") { type = NavType.StringType },
                navArgument("transactionId") { type = NavType.LongType },
                navArgument("shopId") { type = NavType.LongType }
            )
        ) {
            it.arguments?.getString("transaction")?.let { transaction ->
                val aTransaction = Gson().fromJson(transaction, Transaction::class.java)
                TransactionProductsScreen(navController = navController, transaction = aTransaction)
            }
        }
        composable(
            route = Screen.MapScreen.route + "/{cityId}",
            arguments = listOf(navArgument("cityId") {type = NavType.IntType})
        ) {
            MapScreen(navController = navController)
        }
        composable(route = Screen.NotificationsScreen.route) { NotificationsScreen(navController = navController) }
        composable(route = Screen.SurveysScreen.route) { SurveysScreen(navController = navController) }
        composable(
            route = Screen.SurveyScreen.route + "/{surveyId}/{surveyName}",
            arguments = listOf(
                navArgument("surveyId") { type = NavType.LongType },
                navArgument("surveyName") { type = NavType.StringType }
            )
        ) {
            it.arguments?.getString("surveyName")?.let { surveyName ->
                SurveyScreen(navController = navController, surveyName = surveyName)
            }
        }
        // info and settings
        composable(
            route = Screen.UserProfileScreen.route + "/{profile}",
            arguments = listOf(navArgument("profile") { type = NavType.StringType })
        ) {
            it.arguments?.getString("profile").let { jsonProfile ->
                val aProfile = Gson().fromJson(jsonProfile, ProfileDto::class.java)
                UserProfileScreen(navController = navController, profile = aProfile, savedStateHandle = navController.currentBackStackEntry?.savedStateHandle)
            }
        }
        composable(
            route = Screen.EditProfileScreen.route + "/{profile}",
            arguments = listOf(navArgument("profile") { type = NavType.StringType })
        ) {
            it.arguments?.getString("profile").let { jsonProfile ->
                val aProfile = Gson().fromJson(jsonProfile, ProfileDto::class.java)
                EditProfileScreen(navController = navController, profile = aProfile)
            }
        }
        composable(
            route = Screen.SettingsScreen.route + "/{backScreen}",
            arguments = listOf(navArgument("backScreen") { type = NavType.StringType })
        ) {
            it.arguments?.getString("backScreen")?.let { screen ->
                SettingsScreen(navController = navController, backScreen = screen)
            }
        }
    }
}