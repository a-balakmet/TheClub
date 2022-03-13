package com.the.club.ui.presentation.auth.startAuth.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.the.club.R
import com.the.club.ui.theme.*

@Composable
fun LanguageButtons(locale: String, modifier: Modifier = Modifier, onClickRU: () -> Unit, onClickKZ: () -> Unit) {
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(16.dp)) {
        // KZ-button
        Box(modifier = Modifier
            .fillMaxWidth()
            .background(shape = Shapes.large, color = if (locale == "ru") lightGray else pink)
            .height(50.dp)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = rememberRipple(color = backgroundColor()),
            ) {
                onClickKZ()
            }
        ) {
            Text(
                text = stringResource(id = R.string.kazakh),
                style = Typography.h2,
                color = if (locale == "ru") black else white,
                modifier = Modifier
                    .padding(vertical = 12.dp)
                    .align(Alignment.Center)
            )

            if (locale != "ru") {
                Icon(
                    imageVector = Icons.Rounded.Check,
                    contentDescription = "checked",
                    modifier = Modifier
                        .padding(end = 32.dp)
                        .align(Alignment.CenterEnd),
                    tint = white
                )
            }
        }
        // RU-button
        Box(modifier = Modifier
            .fillMaxWidth()
            .background(shape = Shapes.large, color = if (locale == "ru") pink else lightGray)
            .height(50.dp)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = rememberRipple(color = backgroundColor()),
            ) {
                onClickRU()
            }
        ) {
            Text(
                text = stringResource(id = R.string.russian),
                style = Typography.h2,
                color = if (locale == "ru") white else black,
                modifier = Modifier
                    .padding(vertical = 12.dp)
                    .align(Alignment.Center)
            )
            if (locale == "ru") {
                Icon(
                    imageVector = Icons.Rounded.Check,
                    contentDescription = "checked",
                    modifier = Modifier
                        .padding(end = 32.dp)
                        .align(Alignment.CenterEnd),
                    tint = white
                )
            }
        }
    }
}