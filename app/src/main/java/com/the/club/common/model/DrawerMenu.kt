package com.the.club.common.model

import com.the.club.R
import com.the.club.ui.navigation.Screen

data class DrawerMenuItem(
    val icon: Int,
    val text: Int,
    val rout: Screen
)

val MainDrawerList = arrayListOf(
    DrawerMenuItem(R.drawable.ic_home, R.string.main_title, Screen.MainScreen),
    DrawerMenuItem(R.drawable.ic_promo, R.string.promo_actions_title, Screen.PromotionsScreen),
    DrawerMenuItem(R.drawable.ic_price_check, R.string.price_check_title, Screen.PriceCheckScreen),
    DrawerMenuItem(R.drawable.ic_transactions, R.string.transactions_title, Screen.TransactionsScreen),
    DrawerMenuItem(R.drawable.ic_simple_map_pin, R.string.shops_addresses_title, Screen.MapScreen),
    DrawerMenuItem(R.drawable.ic_notification, R.string.notifications_title, Screen.NotificationsScreen),
    DrawerMenuItem(R.drawable.ic_questionnaire, R.string.questionnaire_title, Screen.SurveysScreen)
)

val AdditionalDrawerList = arrayListOf(
    DrawerMenuItem(R.drawable.ic_settings, R.string.settings, Screen.SettingsScreen),
    DrawerMenuItem(R.drawable.ic_exit, R.string.exit, Screen.ExitScreen)
)
