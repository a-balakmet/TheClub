package com.the.club.ui.presentation.promotions

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIos
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.the.club.R
import com.the.club.common.CommonKeys
import com.the.club.ui.commonComponents.BackHandler
import com.the.club.ui.commonComponents.Logo
import com.the.club.ui.commonComponents.MainToolbar
import com.the.club.ui.commonComponents.NoDataMessage
import com.the.club.ui.navigation.Screen
import com.the.club.ui.presentation.promotions.components.PromotionItem
import com.the.club.ui.theme.*

@Composable
fun PromotionsScreen(navController: NavController) {
    val viewModel = hiltViewModel<PromotionsViewModel>()
    BackHandler(onBack = navController::popBackStack)
    val listState = rememberLazyListState()
    var isScrolled = false
    Surface(color = MaterialTheme.colors.background) {
        Column {
            // toolbar
            MainToolbar(
                homeIcon = Icons.Filled.ArrowBackIos,
                onClickHome = { navController.popBackStack() },
                title = stringResource(id = R.string.promo_actions_title),
                menuIcon = null,
                onClickIcon = { }
            )
            // container for list of promotions
            Box(modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp, vertical = 16.dp)
            ) {
                viewModel.promotionsState.value.let {
                    if (it.isLoading) {
                        Column(modifier = Modifier.align(alignment = Alignment.Center)) {
                            Logo(
                                modifier = Modifier.align(Alignment.CenterHorizontally),
                                colors = MainCardColors,
                                isAnimate = true,
                                text = stringResource(id = R.string.loading),
                                textColor = textColor()
                            )
                        }
                    }
                    if (it.isLoadingMore) {
                        Column(modifier = Modifier.align(alignment = Alignment.BottomCenter)) {
                            Logo(
                                modifier = Modifier.align(Alignment.CenterHorizontally),
                                colors = MainCardColors,
                                isAnimate = true,
                                text = stringResource(id = R.string.loading),
                                textColor = textColor()
                            )
                        }
                    }
                    if (it.promotions.isNotEmpty()) {
                        if (listState.firstVisibleItemIndex == it.promotions.size-1) {
                            isScrolled = true
                            viewModel.loadPromotions(false)
                        }
                        if (listState.firstVisibleItemIndex == 0 && isScrolled) {
                            isScrolled = false
                            viewModel.loadPromotions(true)
                        }
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.spacedBy(16.dp),
                            state = listState
                        ) {
                            items(it.promotions) { aPromotion ->
                                PromotionItem(
                                    promotion = aPromotion,
                                    onClick = { promotionId->
                                        navController.navigate(Screen.PromotionScreen.route + "/$promotionId")
                                    }
                                )
                            }
                        }
                    }
                    if (it.isEmptyList) {
                        Column(modifier = Modifier.align(alignment = Alignment.Center)) {
                            NoDataMessage(modifier = Modifier.align(Alignment.CenterHorizontally), icon = R.drawable.ic_promo)
                        }
                    }
                    if (it.error.isNotBlank()) {
                        Column(modifier = Modifier.align(alignment = Alignment.Center)) {
                            Logo(
                                modifier = Modifier.align(Alignment.CenterHorizontally),
                                colors = GrayColorShades,
                                isAnimate = false,
                                text = if (it.error == CommonKeys.noNetwork) stringResource(id = R.string.no_internet_connection) else it.error,
                                textColor = gray
                            )
                        }
                    }
                }
            }
        }
    }
}