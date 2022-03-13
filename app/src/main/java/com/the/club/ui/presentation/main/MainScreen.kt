package com.the.club.ui.presentation.main

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavController
import com.google.gson.Gson
import kotlinx.coroutines.launch
import com.the.club.R
import com.the.club.common.CommonKeys.noNetwork
import com.the.club.common.CommonKeys.ok
import com.the.club.common.CommonKeys.tokenExpired
import com.the.club.common.ktx.getActivity
import com.the.club.common.model.PushContent
import com.the.club.ui.commonComponents.Logo
import com.the.club.ui.commonComponents.OnLifecycleEvent
import com.the.club.ui.commonComponents.StartScreenToolbar
import com.the.club.ui.commonComponents.YesNoDialog
import com.the.club.ui.navigation.Screen
import com.the.club.ui.presentation.main.components.*
import com.the.club.ui.theme.*

@Composable
fun MainScreen(navController: NavController) {
    val viewModel = hiltViewModel<MainScreenViewModel>()
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val (showExitDialog, setShowExitDialog) = remember { mutableStateOf(false) }
    val bannerListState = rememberLazyListState()
    val noNetMessage = stringResource(id = R.string.no_internet_connection_short)
    OnLifecycleEvent { _, event ->
        when (event) {
            Lifecycle.Event.ON_RESUME -> {
                viewModel.totalCounter.value = 0
                viewModel.loadCards(true)
            }
            Lifecycle.Event.ON_PAUSE -> {
                viewModel.counter4banner2show(false)
            }
            else -> {}
        }
    }
    // screen
    Surface(color = MaterialTheme.colors.background) {
        // vars for drawer menu
        val drawerState = rememberDrawerState(DrawerValue.Closed)
        val openDrawer = {
            coroutineScope.launch {
                drawerState.open()
            }
        }
        // left-side drawer
        ModalDrawer(
            drawerState = drawerState,
            gesturesEnabled = viewModel.cardsState.value.error.isEmpty(),
            drawerShape = Shapes.small,
            drawerContent = {
                Drawer(
                    modifier = Modifier.background(color = backgroundColor(), shape = Shapes.small),
                    userName = viewModel.profileState.value.profile?.firstName,
                    unread = viewModel.notificationState.value.number.toString(),
                    surveys = viewModel.surveysState.value.number.toString(),
                    onDestinationClicked = { route ->
                        coroutineScope.launch {
                            drawerState.close()
                        }
                        if (route != Screen.MainScreen.route) {
                            when (route) {
                                Screen.UserProfileScreen.route -> {
                                    val profile = Gson().toJson(viewModel.profileState.value.profile)
                                    navController.navigate(Screen.UserProfileScreen.route + "/$profile")
                                }
                                Screen.TransactionsScreen.route -> navController.navigate(
                                    route = route +
                                            "/${viewModel.mainCard?.balance ?: 0L}" +
                                            "/${viewModel.mainCard?.cardNumber?.replace(("[ *]").toRegex(), "")}${viewModel.mainCard?.cvs}" +
                                            "/${viewModel.mainCard?.id}"
                                )
                                Screen.MapScreen.route -> {
                                    val cityId = viewModel.profileState.value.profile?.city?.id ?: 1
                                    navController.navigate(route = "$route/$cityId")
                                }
                                Screen.SettingsScreen.route -> navController.navigate(route = "$route/main")
                                Screen.ExitScreen.route -> setShowExitDialog(true)
                                else -> navController.navigate(route)
                            }
                        }
                    }
                )
            }
        ) {
            // screen content
            Column(modifier = Modifier.fillMaxSize()) {
                // toolbar
                StartScreenToolbar(
                    homeIcon = Icons.Filled.List,
                    onClickHome = {
                        if (viewModel.cardsState.value.error.isNotEmpty()) Toast.makeText(context, noNetMessage, Toast.LENGTH_SHORT).show()
                        else openDrawer()
                    },
                    title = stringResource(id = R.string.main_title),
                    menuIcon = null,
                    counter = viewModel.totalCounter.value,
                    onClickIcon = {}
                )
                // screen content
                Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                    // banners container
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 26.dp)
                            .defaultMinSize(minHeight = 103.dp)
                            //.height(103.dp)
                    ) {
                        viewModel.bannersState.value.let {
                            // loading banners icon
                            if (it.isLoading) {
                                Column(modifier = Modifier.align(Alignment.Center)) {
                                    Logo(
                                        modifier = Modifier.align(Alignment.CenterHorizontally),
                                        colors = MainCardColors,
                                        isAnimate = true,
                                        text = stringResource(id = R.string.loading),
                                        textColor = textColor()
                                    )
                                }
                            }
                            // list of bonus cards
                            if (it.banners.isNotEmpty()) {
                                val configuration = LocalConfiguration.current
                                val screenWidth = configuration.screenWidthDp
                                LazyRow(
                                    modifier = Modifier.align(alignment = Alignment.Center),
                                    state = bannerListState
                                ) {
                                    itemsIndexed(it.banners) { index, item ->
                                        BannerItem(
                                            banner = item,
                                            index = index,
                                            maxIndex = it.banners.size - 1,
                                            onClick = {bannerLink ->
                                                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(bannerLink))
                                                context.startActivity(intent)
                                            },
                                        )
                                    }
                                }
                                LaunchedEffect (viewModel.bannerIndex2Show.value){
                                    bannerListState.animateScrollToItem(
                                        index = viewModel.bannerIndex2Show.value,
                                        scrollOffset = -screenWidth + (260.dp).value.toInt() // 284
                                    )
                                }
                            }
                            // errors during loading banners
                            if (it.error.isNotBlank()) {
                                Column(modifier = Modifier.align(Alignment.Center)) {
                                    Logo(
                                        modifier = Modifier.align(Alignment.CenterHorizontally),
                                        colors = GrayColorShades,
                                        isAnimate = false,
                                        text =
                                        if (viewModel.cardsState.value.error == noNetwork) stringResource(id = R.string.no_internet_connection)
                                        else viewModel.cardsState.value.error,
                                        textColor = if (viewModel.cardsState.value.error == noNetwork) red else textColor()
                                    )
                                }
                            }
                        }

                    }
                    // main slogan
                    Text(
                        text = stringResource(id = R.string.get_bonuses),
                        style = Typography.h1,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .padding(top = 24.dp)
                    )
                    // cards container
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp)
                            .height(200.dp)
                    ) {
                        viewModel.cardsState.value.let {
                            // loading cards icon
                            if (it.isLoading) {
                                Column(modifier = Modifier.align(Alignment.Center)) {
                                    Logo(
                                        modifier = Modifier.align(Alignment.CenterHorizontally),
                                        colors = MainCardColors,
                                        isAnimate = true,
                                        text = stringResource(id = R.string.loading),
                                        textColor = textColor()
                                    )
                                }
                            }
                            // list of bonus cards
                            if (it.cards.isNotEmpty()) {
                                viewModel.mainCard = it.cards[0]
                                LazyRow(modifier = Modifier.align(Alignment.Center)) {
                                    items(it.cards) { theCard -> BonusCardItem(modifier = Modifier.fillParentMaxWidth(), bonusCard = theCard) }
                                }
                            }
                            // errors during loading cards
                            if (it.error.isNotBlank()) {
                                // show saved cards in shared prefs bonus card for internet absence
                                if (it.error == noNetwork) {
                                    viewModel.savedBonusCard().apply {
                                        viewModel.mainCard = this
                                        NoNetBonusCard(bonusCard = this)
                                    }
                                    // show error during loading
                                } else {
                                    Column(modifier = Modifier.align(Alignment.Center)) {
                                        Logo(
                                            modifier = Modifier.align(Alignment.CenterHorizontally),
                                            colors = GrayColorShades,
                                            isAnimate = false,
                                            text = it.error,
                                            textColor = if (it.error == noNetwork) red else textColor()
                                        )
                                    }
                                }
                            }
                        }
                    }
                    // main navigation buttons
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp, vertical = 8.dp)
                    ) {
                        // button fot price checker
                        SquareButton(
                            modifier = Modifier.weight(2F),
                            text = R.string.price_check,
                            icon = R.drawable.ic_price_check,
                            state = viewModel.cardsState.value
                        ) {
                            if (viewModel.cardsState.value.cards.isNotEmpty()) {
                                navController.navigate(Screen.PriceCheckScreen.route)
                            }
                        }
                        Spacer(modifier = Modifier.weight(1F))
                        // button for promo
                        SquareButton(
                            modifier = Modifier.weight(2F),
                            text = R.string.promo_actions_title,
                            icon = R.drawable.ic_promo,
                            state = viewModel.cardsState.value
                        ) {
                            if (viewModel.cardsState.value.cards.isNotEmpty()) {
                                navController.navigate(Screen.PromotionsScreen.route)
                            }
                        }
                        Spacer(modifier = Modifier.weight(1F))
                        // button for transactions
                        SquareButton(
                            modifier = Modifier.weight(2F),
                            text = R.string.transactions,
                            icon = R.drawable.ic_transactions,
                            state = viewModel.cardsState.value
                        ) {
                            if (viewModel.cardsState.value.cards.isNotEmpty()) {
                                navController.navigate(
                                    Screen.TransactionsScreen.route +
                                            "/${viewModel.mainCard?.balance ?: 0L}" +
                                            "/${viewModel.mainCard?.cardNumber?.replace(("[ *]").toRegex(), "")}${viewModel.mainCard?.cvs}" +
                                            "/${viewModel.mainCard?.id}"
                                )
                            }
                        }
                        Spacer(modifier = Modifier.weight(1F))
                        // button for map and list with shops
                        SquareButton(
                            modifier = Modifier.weight(2F),
                            text = R.string.shops_addresses,
                            icon = R.drawable.ic_simple_map_pin,
                            state = viewModel.cardsState.value
                        ) {
                            if (viewModel.cardsState.value.cards.isNotEmpty()) {
                                val cityId = viewModel.profileState.value.profile?.city?.id ?: 1
                                navController.navigate(Screen.MapScreen.route + "/${cityId}")
                            }
                        }
                    }
                }
            }

        }
    }
    // listener for some errors
    LaunchedEffect(viewModel.errorMessage) {
        when (viewModel.errorMessage) {
            ok -> navController.navigate(Screen.OTPScreen.route) { popUpTo(0) }
            tokenExpired -> viewModel.checkPhoneNumber()
        }
    }
    // listener for navigation from push
    LaunchedEffect(viewModel.mainCard) {
        if (viewModel.mainCard != null) {
            val intent = (context.getActivity())!!.intent
            intent.getStringExtra("payloadExtraData")?.let {
                val pushContent = Gson().fromJson(it, PushContent::class.java)
                val pushType = pushContent.ref_id.type
                val extraId = pushContent.ref_id.value // this additional ID will be used to navigate to exact transaction, survey, etc.
                viewModel.sendReadPush(pushContent.uuid)
                intent.removeExtra("payloadExtraData")
                if (pushType.contains("http")) {
                    val webIntent = Intent(Intent.ACTION_VIEW)
                    webIntent.setDataAndType(Uri.parse(it), "text/html")
                    context.startActivity(webIntent)
                } else {
                    when (pushType) {
                        "transaction" -> {
                            navController.navigate(
                                route = Screen.TransactionsScreen.route +
                                        "/${viewModel.mainCard?.balance ?: 0L}" +
                                        "/${viewModel.mainCard?.cardNumber?.replace(("[ *]").toRegex(), "")}${viewModel.mainCard?.cvs}" +
                                        "/${viewModel.mainCard?.id}"
                            )
                        }
                        "message" -> navController.navigate(Screen.NotificationsScreen.route)
                        "survey" -> navController.navigate(Screen.SurveysScreen.route)
                        "map" -> {
                            val cityId = viewModel.profileState.value.profile?.city?.id ?: 1
                            navController.navigate(Screen.MapScreen.route + "/${cityId}")
                        }
                        else -> navController.navigate(Screen.PromotionsScreen.route)
                    }
                }
            }
        }
    }
    // exit dialog
    AnimatedVisibility(
        visible = showExitDialog,
        enter = slideInVertically(initialOffsetY = { it }),
        exit = slideOutVertically(targetOffsetY = { it })
    ) {
        YesNoDialog(
            showDialog = showExitDialog,
            setShowDialog = setShowExitDialog,
            text = stringResource(id = R.string.logout),
            onClickYes = { viewModel.exitApp(context) },
            onClickNo = {}
        )
    }
}
