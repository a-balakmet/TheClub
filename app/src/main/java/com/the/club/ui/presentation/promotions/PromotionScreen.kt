package com.the.club.ui.presentation.promotions

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIos
import androidx.compose.material.icons.filled.SaveAlt
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.input.pointer.consumeAllChanges
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.accompanist.glide.rememberGlidePainter
import com.the.club.R
import com.the.club.common.CommonKeys.noNetwork
import com.the.club.ui.commonComponents.BackHandler
import com.the.club.ui.commonComponents.Logo
import com.the.club.ui.commonComponents.MainToolbar
import com.the.club.ui.presentation.promotions.components.CounterRow
import com.the.club.ui.theme.*
import kotlin.math.roundToInt

@Composable
fun PromotionScreen(navController: NavController) {
    val viewModel = hiltViewModel<PromotionViewModel>()
    BackHandler(onBack = navController::popBackStack)
    val mainOffset = 50F
    var tempOffset = 50F
    var offsetY by remember { mutableStateOf(mainOffset) }
    val listState = rememberLazyListState()
    val context = LocalContext.current
    val nestedScrollConnection = remember {
        object : NestedScrollConnection {
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                tempOffset += available.y
                when {
                    tempOffset < 0 -> {
                        offsetY = 0F
                        tempOffset = offsetY
                    }
                    tempOffset > mainOffset -> {
                        offsetY = mainOffset
                        tempOffset = offsetY
                    }
                    else -> offsetY = tempOffset
                }
                return Offset.Zero
            }
        }
    }

    Surface(color = MaterialTheme.colors.background) {
        Column(modifier = Modifier.fillMaxSize()) {
            // toolbar
            MainToolbar(
                homeIcon = Icons.Filled.ArrowBackIos,
                onClickHome = { navController.popBackStack() },
                title = stringResource(id = R.string.promo_actions_title),
                menuIcon = null,
                onClickIcon = { }
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = backgroundColor())
            ) {
                viewModel.promotionDetailsState.value.let {
                    // loading for details of promotions
                    if (it.isLoading) {
                        Column(modifier = Modifier.align(alignment = Alignment.Center)) {
                            Logo(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .align(Alignment.CenterHorizontally),
                                colors = MainCardColors,
                                isAnimate = true,
                                text = stringResource(id = R.string.loading),
                                textColor = textColor()
                            )
                        }
                    }
                    // show promotion description
                    it.promotionDetails?.let { promotion ->
                        Box(modifier = Modifier.fillMaxSize()) {
                            // header with promotion image
                            Image(
                                painter = rememberGlidePainter(
                                    request = promotion.imageUrl,
                                    previewPlaceholder = R.drawable.ic_logo
                                ),
                                contentDescription = "banner",
                                contentScale = ContentScale.FillBounds,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .heightIn(max = 200.dp)
                                    .alpha(
                                        alpha = when {
                                            offsetY == 50F -> 1F
                                            offsetY <= 0 -> 0F
                                            else -> offsetY / 50
                                        }
                                    )
                            )
                            // draggable content of the promotion
                            Column(modifier = Modifier
                                .fillMaxSize()
                                .padding(
                                    top = when {
                                        offsetY >= mainOffset -> mainOffset.dp
                                        offsetY <= 0 -> 0.dp
                                        else -> offsetY.roundToInt().dp
                                    },
                                    bottom = 16.dp
                                )
                                .offset { IntOffset(0, offsetY.roundToInt()) }
                                .pointerInput(Unit) {
                                    detectDragGestures { change, dragAmount ->
                                        change.consumeAllChanges()
                                        offsetY += dragAmount.y
                                        if (offsetY < 0) offsetY = 0F
                                        if (offsetY > mainOffset) offsetY = mainOffset
                                    }
                                }
                            ) {
                                // card with basics and counter
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(
                                            color = pink.copy(
                                                alpha = when {
                                                    offsetY >= mainOffset -> 0F
                                                    offsetY <= 0 -> 1F
                                                    else -> 1F - offsetY / 50
                                                }
                                            ),
                                            shape = RoundedCornerShape(
                                                topStart = if (offsetY == 0F) 0.dp else 10.dp,
                                                topEnd = if (offsetY == 0F) 0.dp else 10.dp,
                                                bottomEnd = 10.dp,
                                                bottomStart = 10.dp
                                            )
                                        )
                                ) {
                                    Card(
                                        shape = Shapes.large,
                                        elevation = if (offsetY > 0) 8.dp else 0.dp,
                                        backgroundColor = if (offsetY > 0F) backgroundColor() else pink,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(start = 24.dp, top = 16.dp, end = 24.dp, bottom = 16.dp)
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .background(
                                                    color = pink.copy(
                                                        alpha = when {
                                                            offsetY >= mainOffset -> 0F
                                                            offsetY <= 0 -> 1F
                                                            else -> 1F - offsetY / 50
                                                        }
                                                    )
                                                )
                                        ) {
                                            Column(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .padding(start = 10.dp, top = if (offsetY > 0) 24.dp else 0.dp, end = 10.dp, bottom = 10.dp)
                                            ) {
                                                Text(
                                                    text = promotion.title,
                                                    style = Typography.h2,
                                                    color = if (offsetY > 0) textColor() else white,
                                                    textAlign = TextAlign.Center,
                                                    modifier = Modifier
                                                        .fillMaxWidth()
                                                        .padding(bottom = 8.dp)
                                                )
                                                Text(
                                                    text = stringResource(id = R.string.promo_till_title_text),
                                                    color = if (offsetY > 0) gray else white,
                                                    style = Typography.body2,
                                                    textAlign = TextAlign.Center,
                                                    modifier = Modifier
                                                        .fillMaxWidth()
                                                        .padding(bottom = 10.dp)
                                                )
                                                Box(modifier = Modifier.align(alignment = Alignment.CenterHorizontally)) {
                                                    viewModel.counter.value.let { counterArray ->
                                                        if (counterArray.size == 8) {
                                                            CounterRow(values = counterArray, isCollapsed = offsetY == 0F)
                                                        } else {
                                                            CounterRow(values = listOf("", "", "", "", "", "", "", ""), isCollapsed = false)
                                                        }
                                                    }
                                                }
                                                Text(
                                                    text = stringResource(id = R.string.promo_date_range, promotion.start, promotion.stop),
                                                    color = if (offsetY > 0) gray else white,
                                                    style = Typography.body2,
                                                    textAlign = TextAlign.Center,
                                                    modifier = Modifier
                                                        .fillMaxWidth()
                                                        .padding(vertical = 10.dp)
                                                )
                                            }
                                        }
                                    }
                                }
                                LazyColumn(
                                    state = listState,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 16.dp, vertical = 24.dp)
                                        .nestedScroll(nestedScrollConnection),
                                ) {
                                    item {
                                        promotion.shortDescription.isNotBlank().let { isText ->
                                            if (isText) Text(
                                                text = promotion.shortDescription,
                                                style = Typography.h2,
                                                color = textColor(),
                                                textAlign = TextAlign.Center,
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .padding(top = 16.dp)
                                            )
                                        }
                                    }
                                    item {
                                        Text(
                                            text = promotion.fullDescription,
                                            style = Typography.body1,
                                            color = textColor(),
                                            modifier = Modifier.padding(top = 16.dp, bottom = 32.dp)
                                        )
                                    }
                                    if (promotion.conditionsFull.isNotEmpty()) {
                                        item {
                                            Text(
                                                text = stringResource(id = R.string.promo_to_do),
                                                style = Typography.h2,
                                                color = textColor(),
                                                modifier = Modifier.padding(bottom = 16.dp)
                                            )
                                        }
                                        item {
                                            Text(
                                                text = promotion.conditionsFull,
                                                style = Typography.body1,
                                                color = textColor(),
                                                modifier = Modifier.padding(bottom = 32.dp)
                                            )
                                        }
                                    }
                                    item {
                                        Text(
                                            text = promotion.conditionsShort,
                                            style = Typography.h2,
                                            color = textColor(),
                                            modifier = Modifier
                                                .padding(bottom = 24.dp)
                                                .align(alignment = Alignment.CenterHorizontally)
                                        )
                                    }
                                    if (promotion.downloadLink.isNotEmpty()) {
                                        item {
                                            Box(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .padding(bottom = 24.dp)
                                                    .background(color = pink, shape = Shapes.large)
                                                    .clickable {
                                                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(promotion.downloadLink))
                                                        context.startActivity(intent)
                                                    }
                                            ) {
                                                Icon(
                                                    imageVector = Icons.Filled.SaveAlt,
                                                    contentDescription = "save",
                                                    tint = white,
                                                    modifier = Modifier
                                                        .padding(horizontal = 24.dp, vertical = 8.dp)
                                                )
                                                Text(
                                                    text = stringResource(id = R.string.promo_document),
                                                    color = white,
                                                    style = Typography.h5,
                                                    modifier = Modifier
                                                        .align(alignment = Alignment.Center)
                                                        .padding(vertical = 4.dp)
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                    if (it.error.isNotBlank()) {
                        Column(modifier = Modifier.align(alignment = Alignment.Center)) {
                            Logo(
                                modifier = Modifier.align(Alignment.CenterHorizontally),
                                colors = GrayColorShades,
                                isAnimate = false,
                                text = if (it.error == noNetwork) stringResource(id = R.string.no_internet_connection) else it.error,
                                textColor = gray
                            )
                        }
                    }
                }
            }
        }
    }
}