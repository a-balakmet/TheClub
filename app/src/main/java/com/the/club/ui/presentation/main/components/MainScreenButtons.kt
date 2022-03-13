package com.the.club.ui.presentation.main.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.the.club.ui.presentation.main.states.BonusCardState
import com.the.club.ui.theme.*

@Composable
fun SquareButton(
    modifier: Modifier,
    text: Int,
    icon: Int,
    state: BonusCardState,
    onClick: () -> Unit
) {
    Column(modifier = modifier.clickable(
        interactionSource = remember { MutableInteractionSource() },
        indication = rememberRipple(color = backgroundColor()),
    ) {
        onClick()
    }) {
        Box(
            modifier = Modifier
                .size(62.dp)
                .background(
                    color = if (state.cards.isNotEmpty()) pink else gray,
                    shape = Shapes.large
                )
        ) {
            Icon(
                painter = painterResource(id = icon),
                contentDescription = "checked",
                modifier = Modifier
                    .padding(8.dp)
                    .align(Alignment.Center),
                tint = white
            )
        }
        Text(
            text = stringResource(id = text),
            style = TextStyle(
                fontFamily = AppFont,
                fontWeight = FontWeight.Normal,
                fontSize = 10.sp,
                lineHeight = 10.sp,
                letterSpacing = (-0.3).sp
            ),
            modifier = Modifier
                .padding(top = 8.dp)
                .align(Alignment.CenterHorizontally),
            textAlign = TextAlign.Center,
            color = if (state.cards.isNotEmpty()) textColor() else gray
        )
    }
}

