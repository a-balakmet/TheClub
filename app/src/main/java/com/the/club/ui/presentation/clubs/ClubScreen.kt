package com.the.club.ui.presentation.clubs

import android.widget.TextView
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIos
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.text.HtmlCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.accompanist.glide.rememberGlidePainter
import com.the.club.R
import com.the.club.common.CommonKeys
import com.the.club.common.CommonKeys.clubsBackKey
import com.the.club.ui.commonComponents.*
import com.the.club.ui.theme.*

@Composable
fun ClubScreen(navController: NavController) {
    val viewModel = hiltViewModel<ClubViewModel>()
    val mainTextColor = textColor()
    val (show21Dialog, setShow21Dialog) = remember { mutableStateOf(false) }
    val (showExitDialog, setShowExitDialog) = remember { mutableStateOf(false) }
    BackHandler {
        onBack(navController, mapOf(clubsBackKey to true))
    }
    Surface(color = MaterialTheme.colors.background) {
        Column {
            // toolbar
            MainToolbar(
                homeIcon = Icons.Filled.ArrowBackIos,
                onClickHome = { onBack(navController, mapOf(clubsBackKey to true)) },
                title = stringResource(id = R.string.club_details),
                menuIcon = null,
                onClickIcon = { }
            )
            // club data
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = backgroundColor())
                    .padding()
            ) {
                viewModel.clubsState.value.let {
                    // loading notifications
                    if (it.isLoading) {
                        Column(modifier = Modifier.align(Alignment.Center)) {
                            Logo(
                                modifier = Modifier.align(Alignment.CenterHorizontally),
                                colors = LoadingColorShades,
                                isAnimate = true,
                                text = stringResource(id = R.string.loading),
                                textColor = textColor()
                            )
                        }
                    }
                    // the club
                    it.club?.let { theClub ->
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(color = backgroundColor())
                        ) {
                            Image(
                                painter = rememberGlidePainter(
                                    request = theClub.imageUrl,
                                    previewPlaceholder = R.drawable.ic_logo
                                ),
                                contentDescription = "image",
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(173.dp)
                                    .background(brush = GrayColorShades.toAnimatedBrush()),
                                contentScale = ContentScale.FillBounds
                            )
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(horizontal = 24.dp)
                            ) {
                                AndroidView(
                                    modifier = Modifier
                                        .weight(1F)
                                        .padding(vertical = 16.dp)
                                        .verticalScroll(rememberScrollState()),
                                    factory = { context ->
                                        TextView(context).apply {
                                            text = HtmlCompat.fromHtml(theClub.description, HtmlCompat.FROM_HTML_MODE_LEGACY)
                                            this.setTextColor(mainTextColor.toArgb())
                                        }
                                    }
                                )
                                if (!theClub.isMember) {
                                    Row(modifier = Modifier.fillMaxWidth()) {
                                        Icon(
                                            painter = painterResource(id = R.drawable.ic_check_mark),
                                            contentDescription = "check",
                                            tint = pink,
                                        )
                                        AndroidView(
                                            modifier = Modifier
                                                .weight(1F)
                                                .padding(start = 8.dp),
                                            factory = { context ->
                                                TextView(context).apply {
                                                    text =
                                                        HtmlCompat.fromHtml(
                                                            context.getString(R.string.club_rules),
                                                            HtmlCompat.FROM_HTML_MODE_LEGACY
                                                        )
                                                    this.setTextColor(gray.toArgb())
                                                }
                                            }
                                        )
                                    }
                                }
                                StandardEnablingExchangeButton(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 16.dp),
                                    text =
                                    if (theClub.isMember) stringResource(id = R.string.exit_club)
                                    else stringResource(id = R.string.enter_club),
                                    isEnabled = !viewModel.exchangeState.value,
                                    isExchange = viewModel.exchangeState.value
                                ) {
                                    if (theClub.isMember) {
                                        setShowExitDialog(true)
                                    } else {
                                        if (theClub.isAdult) {
                                            setShow21Dialog(true)
                                        } else {
                                            viewModel.enterClub()
                                        }
                                    }
                                }
                            }
                        }
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
    // isAdult dialog
    AnimatedVisibility(
        visible = show21Dialog,
        enter = slideInVertically(initialOffsetY = { it }),
        exit = slideOutVertically(targetOffsetY = { it })
    ) {
        YesNoDialog(
            showDialog = show21Dialog,
            setShowDialog = setShow21Dialog,
            text = stringResource(id = R.string.years_twenty_one),
            onClickYes = {viewModel.enterClub()} ,
            onClickNo = {}
        )
    }
    // exit club dialog
    AnimatedVisibility(
        visible = showExitDialog,
        enter = slideInVertically(initialOffsetY = { it }),
        exit = slideOutVertically(targetOffsetY = { it })
    ) {
        YesNoDialog(
            showDialog = showExitDialog,
            setShowDialog = setShowExitDialog,
            text = stringResource(id = R.string.ask_exit_club),
            onClickYes = {viewModel.exitClub()},
            onClickNo = {}
        )
    }
}
