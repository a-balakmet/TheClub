package com.the.club.ui.commonComponents

import androidx.compose.animation.core.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

@Composable
fun List<Color>.toAnimatedBrush(): Brush {
    val transition = rememberInfiniteTransition()
    val translateAnim by transition.animateFloat(
        initialValue = 0f,
        targetValue = 2000f,
        animationSpec = infiniteRepeatable(
            tween(durationMillis = 2000, easing = FastOutLinearInEasing),
            RepeatMode.Restart
        )
    )
    return Brush.linearGradient(
        colors = this,
        start = Offset(10f, 10f),
        end = Offset(translateAnim, translateAnim)
    )
}

@Composable
fun List<Color>.toMainCardBrush() : Brush {
    return Brush.linearGradient(
        colors = this,
        start = Offset(10f, 10f),
        end = Offset(700F, 700F)
    )
}

@Composable
fun List<Color>.toLoaderBrush() : Brush {
    return Brush.linearGradient(
        colors = this,
        start = Offset(1f, 1F),
        end = Offset(100F, 100F)
    )
}