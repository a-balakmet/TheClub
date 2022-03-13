package com.the.club.ui.commonComponents

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.the.club.ui.theme.*

@Composable
fun NonBoarderButton(modifier: Modifier = Modifier, text: String, onClick: () -> Unit) {
    Text(
        text = text,
        style = Typography.h2,
        color = pink,
        modifier = modifier.clickable(
            interactionSource = remember { MutableInteractionSource() },
            indication = rememberRipple(color = backgroundColor()),
        ) {
            onClick()
        }
    )
}

@Composable
fun BoarderButton(modifier: Modifier = Modifier, text: String, onClick: () -> Unit) {
    Box(
        modifier = modifier
            .height(50.dp)
            .border(width = 1.dp, color = pink, shape = RoundedCornerShape(10.dp))
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = rememberRipple(color = backgroundColor()),
            ) {
                onClick()
            },
    ) {
        Text(
            text = text,
            style = Typography.body1,
            color = pink,
            modifier = Modifier
                .align(alignment = Alignment.Center)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = rememberRipple(color = backgroundColor()),
                ) {
                    onClick()
                }
        )
    }
}

@Composable
fun StandardButton(modifier: Modifier = Modifier, text: String, buttonColor: Color, onClick: () -> Unit) {
    Box(
        modifier = modifier
            .background(shape = Shapes.large, color = buttonColor)
            .height(50.dp)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = rememberRipple(color = backgroundColor()),
            ) {
                onClick()
            }
    ) {
        Text(
            text = text,
            style = Typography.h2,
            color = if (buttonColor == pink) white else black,
            modifier = Modifier
                .padding(vertical = 12.dp)
                .align(Alignment.Center)
        )
    }
}

@Composable
fun StandardEnablingButton(modifier: Modifier = Modifier, text: String, isEnabled: Boolean, onClick: () -> Unit) {
    Box(
        modifier = modifier
            .background(shape = Shapes.large, color = if (isEnabled) pink else lightPink)
            .height(50.dp)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = rememberRipple(color = backgroundColor()),
            ) {
                if (isEnabled) onClick()
            }
    ) {
        Text(
            text = text,
            style = Typography.h2,
            color = if (isEnabled) white else gray,
            modifier = Modifier
                .padding(vertical = 12.dp)
                .align(Alignment.Center)
        )
    }
}

@Composable
fun StandardEnablingExchangeButton(
    modifier: Modifier = Modifier,
    text: String,
    isEnabled: Boolean,
    isExchange: Boolean,
    onClick: () -> Unit
) {
    if (isExchange) {
        val brush = LoadingColorShades.toAnimatedBrush()
        Box(
            modifier = modifier
                .background(shape = Shapes.large, brush = brush)
                .height(50.dp)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = rememberRipple(color = backgroundColor()),
                ) { }
        ) {
            Text(
                text = text,
                style = Typography.h2,
                color = gray,
                modifier = Modifier
                    .padding(vertical = 12.dp)
                    .align(Alignment.Center)
            )
        }
    } else {
        Box(
            modifier = modifier
                .background(shape = Shapes.large, color = if (isEnabled) pink else lightPink)
                .height(50.dp)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = rememberRipple(color = backgroundColor()),
                ) {
                    if (isEnabled) onClick()
                }
        ) {
            Text(
                text = text,
                style = Typography.h2,
                color = if (isEnabled) white else gray,
                modifier = Modifier
                    .padding(vertical = 12.dp)
                    .align(Alignment.Center)
            )
        }
    }

}

@Composable
fun NumberButton(modifier: Modifier, text: String, onClick: () -> Unit) {
    Surface(
        shape = Shapes.medium,
        modifier = modifier
            .height(56.dp)
            .background(color = backgroundColor(), shape = Shapes.medium)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = rememberRipple(color = backgroundColor()),
            ) {
                onClick()
            },
        elevation = 3.dp
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Text(
                text = text,
                style = TextStyle(
                    color = textColor(),
                    fontFamily = AppFont,
                    fontWeight = FontWeight.Normal,
                    fontSize = 30.sp
                ),
                modifier = Modifier.align(Alignment.Center),
                textAlign = TextAlign.Center
            )
        }
    }
}
