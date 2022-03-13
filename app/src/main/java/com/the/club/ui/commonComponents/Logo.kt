package com.the.club.ui.commonComponents

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.the.club.R
import com.the.club.ui.theme.Shapes
import com.the.club.ui.theme.Typography
import com.the.club.ui.theme.lightGray

@Composable
fun MainLogo(
    modifier: Modifier,
    colors: List<Color>,
    isStandard: Boolean,
    text: String?,
    textColor: Color
) {
    val brush = colors.toAnimatedBrush()
    Column {
        Box(
            modifier = modifier
                .size(if (isStandard) 100.dp else 50.dp)
                .padding(8.dp)
        ) {
            Canvas(
                modifier = Modifier.fillMaxSize(),
                onDraw = {
                    drawCircle(
                        brush = brush,
                        radius = if (isStandard) 50.dp.toPx() else 25.dp.toPx(),
                    )
                })
            Icon(
                painter = painterResource(id = R.drawable.ic_logo),
                contentDescription = "logo",
                modifier = Modifier
                    .align(Alignment.Center)
                    .fillMaxSize(),
                tint = Color.White,
            )
        }
        text?.let {
            Text(
                text = it,
                style = Typography.body2,
                color = textColor,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(top = if (isStandard) 16.dp else 8.dp)
            )
        }
    }
}

@Composable
fun Logo(
    modifier: Modifier,
    colors: List<Color>,
    isAnimate: Boolean,
    text: String?,
    textColor: Color
) {
    val duration = 500
    val transition = rememberInfiniteTransition()
    val shadowSize by transition.animateFloat(
        initialValue = 1F,
        targetValue = if (isAnimate) 0.1F else 1F,
        animationSpec = infiniteRepeatable(
            tween(durationMillis = duration, easing = FastOutLinearInEasing),
            RepeatMode.Reverse
        )
    )
    val rotationValue by transition.animateFloat(
        initialValue = 0F,
        targetValue = if (isAnimate) -18F else 0F,
        animationSpec = infiniteRepeatable(
            tween(durationMillis = duration, easing = FastOutLinearInEasing),
            RepeatMode.Reverse
        )
    )
    val paddingValue by transition.animateFloat(
        initialValue = 0F,
        targetValue = if (isAnimate) 10F else 0F,
        animationSpec = infiniteRepeatable(
            tween(durationMillis = duration, easing = FastOutLinearInEasing),
            RepeatMode.Reverse
        )
    )
    Box(
        modifier = modifier
            .padding(top = 16.dp)
            .background(color = Color.Transparent)
            .size(width = 220.dp, height = 150.dp)
    ) {
        Column(modifier = Modifier.align(alignment = Alignment.Center)) {
            Card(
                modifier = Modifier
                    .padding(bottom = paddingValue.dp)
                    .size(width = 71.dp, height = 44.dp)
                    .align(alignment = Alignment.CenterHorizontally)
                    .rotate(rotationValue),
                elevation = 2.dp
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(brush = colors.toLoaderBrush(), shape = Shapes.small)
                ) {
                    Text(
                        text = "The\nClub",
                        style = Typography.subtitle2,
                        color = Color.White,
                        modifier = Modifier.padding(start = 2.dp, top = 2.dp)
                    )
                    Image(
                        painter = painterResource(id = R.drawable.ic_logo_cut),
                        contentDescription = "logo",
                        modifier = Modifier.align(alignment = Alignment.BottomEnd)
                    )
                }
            }
            Box(
                modifier = Modifier
                    .padding(top = 12.dp - paddingValue.dp)
                    .size(width = 60.dp * shadowSize, height = 2.dp)
                    .background(color = lightGray, shape = Shapes.small)
                    .align(alignment = Alignment.CenterHorizontally),
            )
            if (text != null) {
                Text(
                    text = text,
                    style = Typography.body1,
                    textAlign = TextAlign.Center,
                    color = textColor,
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(top = 8.dp)
                )
            }
        }
    }
}