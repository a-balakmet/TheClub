package com.the.club.ui.navigation

sealed class Screen(val route: String) {
    // start
    object StartScreen : Screen(route = "Start")
    object StartAuthScreen : Screen(route = "StartAuth")
    // login
    object PhoneScreen : Screen(route = "PhoneScreen")
    object OTPScreen : Screen(route = "OTPScreen")
    // card creation
    object CardCreationScreen: Screen(route = "CardCreationScreen")
    object CardCreatedScreen: Screen(route = "CardCreatedScreen")
    object CreateProfileScreen: Screen(route = "CreateProfileScreen")
    // entrance
    object PinScreen : Screen(route = "PinScreen")
    // application
    object MainScreen : Screen(route = "MainScreen")
    object PromotionsScreen: Screen(route = "PromotionsScreen")
    object PromotionScreen: Screen(route = "PromotionScreen")
    object ClubsScreen: Screen(route = "ClubsScreen")
    object ClubScreen: Screen(route = "ClubScreen")
    object PriceCheckScreen: Screen(route = "PriceCheckScreen")
    //object FeedbackScreen: Screen(route = "FeedbackScreen")
    object TransactionsScreen: Screen(route = "TransactionsScreen")
    object TransactionProductsScreen: Screen(route = "TransactionsProductsScreen")
    object MapScreen: Screen(route = "MapScreen")
    object NotificationsScreen: Screen(route = "NotificationsScreen")
    object SurveysScreen: Screen(route = "SurveysScreen")
    object SurveyScreen: Screen(route = "SurveyScreen")
    object ExitScreen: Screen(route = "ExitScreen")
    // info and settings
    object UserProfileScreen : Screen(route = "UserProfileScreen")
    object EditProfileScreen : Screen(route = "EditProfileScreen")
    object SettingsScreen: Screen(route = "SettingsScreen")
}
