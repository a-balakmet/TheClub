package com.the.club.ui.commonComponents

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.the.club.R
import com.the.club.ui.theme.*

@Composable
fun OkDialog(
    showDialog: Boolean,
    setShowDialog: (Boolean) -> Unit,
    text: String,
    onClick: () -> Unit
) {
    if (showDialog) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color.Black.copy(alpha = 0.5F))
        ) {
            Column(
                modifier = Modifier
                    .background(color = Color(0xFF505050), shape = Shapes.large)
                    .align(alignment = Alignment.Center)
                    .width(244.dp)
            ) {
                Box(
                    modifier = Modifier
                        .padding(vertical = 16.dp)
                        .size(width = 71.dp, height = 44.dp)
                        .background(brush = MainCardColors.toLoaderBrush(), shape = Shapes.small)
                        .align(alignment = Alignment.CenterHorizontally)
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
                Text(
                    text = text,
                    style = Typography.h2,
                    color = white,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .padding(start = 16.dp, end = 16.dp, bottom = 8.dp)
                        .align(alignment = Alignment.CenterHorizontally)

                )
                Divider(
                    modifier = Modifier
                        .height(1.dp)
                        .background(color = Color(0xFF6A6A6A))
                )
                Text(
                    text = stringResource(id = R.string.ok),
                    style = Typography.h2,
                    color = blue,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .align(alignment = Alignment.CenterHorizontally)
                        .padding(vertical = 8.dp)
                        .clickable {
                            setShowDialog(false)
                            onClick()
                        }
                )
            }
        }
    }
}