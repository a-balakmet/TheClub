package com.the.club.ui.presentation.settings.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.the.club.R
import com.the.club.ui.theme.Shapes
import com.the.club.ui.theme.Typography
import com.the.club.ui.theme.blue
import com.the.club.ui.theme.white

@Composable
fun BlockCardDialog(
    showDialog: Boolean,
    setShowDialog: (Boolean) -> Unit,
    onClick: () -> Unit
) {
    if (showDialog) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color.Black.copy(alpha = 0.8F))
        ) {
            Column(
                modifier = Modifier
                    .background(color = Color(0xFF505050), shape = Shapes.large)
                    .align(alignment = Alignment.Center)
                    .width(244.dp)
            ) {
                Text(
                    text = stringResource(id = R.string.block_card_title),
                    style = Typography.h3,
                    color = white,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                        .align(alignment = Alignment.CenterHorizontally)

                )
                Text(
                    text = stringResource(id = R.string.ask_block_card),
                    style = Typography.body1,
                    color = white,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
                        .align(alignment = Alignment.CenterHorizontally)

                )
                Divider(
                    modifier = Modifier
                        .height(1.dp)
                        .background(color = Color(0xFF6A6A6A))
                )
                Text(
                    text = stringResource(id = R.string.to_block),
                    style = Typography.h3,
                    color = blue,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .align(alignment = Alignment.CenterHorizontally)
                        .padding(vertical = 16.dp)
                        .clickable {
                            setShowDialog(false)
                            onClick()
                        }
                )
                Divider(
                    modifier = Modifier
                        .height(1.dp)
                        .background(color = Color(0xFF6A6A6A))
                )
                Text(
                    text = stringResource(id = R.string.cancel),
                    style = Typography.h3,
                    color = blue,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .align(alignment = Alignment.CenterHorizontally)
                        .padding(vertical = 16.dp)
                        .clickable {
                            setShowDialog(false)
                        }
                )
            }
        }
    }
}