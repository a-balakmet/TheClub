package com.the.club.ui.presentation.clubs

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIos
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavController
import com.the.club.R
import com.the.club.common.CommonKeys
import com.the.club.common.CommonKeys.clubsBackKey
import com.the.club.ui.commonComponents.*
import com.the.club.ui.navigation.Screen
import com.the.club.ui.presentation.clubs.components.ClubItem
import com.the.club.ui.theme.*

@Composable
fun ClubsScreen(navController: NavController, savedStateHandle: SavedStateHandle?) {
    val viewModel = hiltViewModel<ClubsViewModel>()
    BackHandler(onBack = navController::popBackStack)
    if (savedStateHandle != null) {
        val passedValue by savedStateHandle.getLiveData<Boolean>(clubsBackKey).observeAsState()
        LaunchedEffect(passedValue) {
            passedValue?.let {
                if (it) {
                    viewModel.runLoadingClubs()
                }
                savedStateHandle.remove<Boolean>(clubsBackKey)
            }
        }
    }
    Surface(color = MaterialTheme.colors.background) {
        Column {
            // toolbar
            MainToolbar(
                homeIcon = Icons.Filled.ArrowBackIos,
                onClickHome = { navController.popBackStack() },
                title = stringResource(id = R.string.clubs_title),
                menuIcon = null,
                onClickIcon = { }
            )
            // clubs list
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = backgroundColor())
                    .padding(horizontal = 24.dp, vertical = 16.dp)
            ) {
                viewModel.clubsState.value.let {
                    // loading notifications
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
                    // list of clubs
                    if (it.clubs.isNotEmpty()) {
                        LazyColumn(
                            modifier = Modifier
                                .padding(horizontal = 24.dp, vertical = 16.dp)
                                .fillMaxSize(),
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            items(it.clubs) { aClub ->
                                ClubItem(
                                    club = aClub,
                                    onClick = {
                                        navController.navigate(Screen.ClubScreen.route + "/${aClub.id}")
                                    }
                                )
                            }
                        }
                    } else {
                        // no clubs
                        NoDataMessage(
                            modifier = Modifier.align(alignment = Alignment.Center),
                            icon = R.drawable.ic_clubs
                        )
                    }
                    // errors during loading clubs
                    if (it.error.isNotBlank()) {
                        Column(modifier = Modifier.align(Alignment.Center)) {
                            Logo(
                                modifier = Modifier.align(Alignment.CenterHorizontally),
                                colors = GrayColorShades,
                                isAnimate = false,
                                text =
                                if (viewModel.clubsState.value.error == CommonKeys.noNetwork) stringResource(id = R.string.no_internet_connection)
                                else viewModel.clubsState.value.error,
                                textColor = if (viewModel.clubsState.value.error == CommonKeys.noNetwork) red else textColor()
                            )
                        }
                    }
                }
            }
        }
    }
}