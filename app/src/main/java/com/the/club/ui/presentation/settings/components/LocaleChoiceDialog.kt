package com.the.club.ui.presentation.settings.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.the.club.R
import com.the.club.ui.theme.*

@Composable
fun LocaleChoiceDialog(
    showDialog: Boolean,
    setShowDialog: (Boolean) -> Unit,
    locale: String,
    onClick: (String) -> Unit
) {
    if (showDialog) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color.Black.copy(alpha = 0.8F))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = backgroundColor())
                    .align(alignment = Alignment.BottomCenter)
            ) {
                Text(
                    text = stringResource(id = R.string.setup_app_language),
                    style = Typography.body1,
                    color = blue,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(all = 10.dp)
                        .align(alignment = Alignment.CenterHorizontally)
                )
                // "ru" locale
                Box(modifier = Modifier
                    .fillMaxWidth()
                    .background(color = if (locale == "ru") lightGray else backgroundColor())
                    .clickable {
                        onClick("ru")
                        setShowDialog(false)
                    }
                ) {
                    Text(
                        text = stringResource(id = R.string.russian),
                        style = Typography.h4,
                        color = if (locale == "ru") textColor() else gray,
                        modifier = Modifier
                            .padding(all = 16.dp)
                            .align(alignment = Alignment.Center)
                    )
                }
                // "kz" locale
                Box(modifier = Modifier
                    .fillMaxWidth()
                    .background(color = if (locale == "kk") lightGray else backgroundColor())
                    .clickable {
                        onClick("kk")
                        setShowDialog(false)
                    }
                ) {
                    Text(
                        text = stringResource(id = R.string.kazakh),
                        style = Typography.h4,
                        color = if (locale == "kk") textColor() else gray,
                        modifier = Modifier
                            .padding(all = 16.dp)
                            .align(alignment = Alignment.Center)
                    )
                }
            }
        }
    }
}