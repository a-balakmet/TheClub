package com.the.club.ui.presentation.notifications

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIos
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.the.club.R
import com.the.club.common.CommonKeys.noNetwork
import com.the.club.ui.commonComponents.BackHandler
import com.the.club.ui.commonComponents.Logo
import com.the.club.ui.commonComponents.MainToolbar
import com.the.club.ui.commonComponents.NoDataMessage
import com.the.club.ui.presentation.notifications.components.NotificationsItem
import com.the.club.ui.theme.*

@ExperimentalFoundationApi
@Composable
fun NotificationsScreen(navController: NavController) {
    BackHandler { navController.popBackStack() }
    val viewModel = hiltViewModel<NotificationsViewModel>()
    val context = LocalContext.current
    Surface(color = MaterialTheme.colors.background) {
        Column {
            // toolbar
            MainToolbar(
                homeIcon = Icons.Filled.ArrowBackIos,
                onClickHome = { navController.popBackStack() },
                title = stringResource(id = R.string.notifications_title),
                menuIcon = null,
                onClickIcon = { }
            )
            // notifications
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = backgroundColor())
                    .padding(start = 24.dp, end = 24.dp, bottom = 16.dp)
            ) {
                // loading notifications
                if (viewModel.notificationsState.value.isLoading) {
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
                // list of notifications
                if (viewModel.notificationsState.value.notifications.isNotEmpty()) {
                    LazyColumn(modifier = Modifier.fillMaxSize()
                    ) {
                        val groupedList = viewModel.notificationsState.value.notifications.groupBy { it.date }
                        groupedList.forEach { (date, notifications) ->
                            stickyHeader {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(color = backgroundColor())
                                        .padding(vertical = 16.dp)
                                ) {
                                    Text(
                                        text = date,
                                        modifier = Modifier.align(alignment = Alignment.Center)
                                    )
                                }
                            }
                            items(notifications) { aNotification ->
                                NotificationsItem(
                                    notification = aNotification,
                                    onClick = {
                                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(aNotification.link))
                                        context.startActivity(intent)
                                    }
                                )
                            }
                        }
                    }
                }
                // errors during loading notifications
                if (viewModel.notificationsState.value.error.isNotBlank()) {
                    Column(modifier = Modifier.align(Alignment.Center)) {
                        Logo(
                            modifier = Modifier.align(Alignment.CenterHorizontally),
                            colors = GrayColorShades,
                            isAnimate = false,
                            text =
                            if (viewModel.notificationsState.value.error == noNetwork) stringResource(id = R.string.no_internet_connection)
                            else viewModel.notificationsState.value.error,
                            textColor = if (viewModel.notificationsState.value.error == noNetwork) red else textColor()
                        )
                    }
                }
                // no notifications
                if (viewModel.notificationsState.value.isEmptyList) {
                    NoDataMessage(
                        modifier = Modifier.align(alignment = Alignment.Center),
                        icon = R.drawable.ic_notification
                    )
                }
            }
        }
    }
}
