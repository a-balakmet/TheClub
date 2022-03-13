package com.the.club.ui.commonComponents

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.the.club.R
import com.the.club.ui.theme.*

@Composable
fun StartToolbar(modifier: Modifier) {
    Image(
        painter = painterResource(id = R.drawable.ic_curved_toolbar),
        contentDescription = "toolbar",
        contentScale = ContentScale.FillBounds,
        modifier = modifier.height(67.dp)
    )
}

@Composable
fun StartScreenToolbar(
    homeIcon: ImageVector,
    onClickHome: () -> Unit,
    title: String,
    menuIcon: Painter?,
    counter: Int,
    onClickIcon: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = pink)
    ) {
        Box(modifier = Modifier
            .size(width = 48.dp, height = 36.dp)
            .align(Alignment.CenterStart)
            .padding(start = 16.dp)
        ) {
            Icon(
                imageVector = homeIcon,
                contentDescription = "home button",
                modifier = Modifier
                    .size(24.dp)
                    .align(alignment = Alignment.CenterStart)
                    .clickable { onClickHome() },
                tint = white
            )
            if (counter != 0) {
                Box(modifier = Modifier
                    .size(16.dp)
                    .align(alignment = Alignment.CenterEnd)
                    .background(color = lightYellow, shape = CircleShape)
                ) {
                    Text(
                        text = " $counter ",
                        color = green,
                        style = Typography.caption,
                        modifier = Modifier.align(alignment = Alignment.Center)
                    )
                }
            }
        }
        Text(
            text = title,
            style = Typography.h2,
            color = white,
            modifier = Modifier
                .padding(vertical = 16.dp)
                .align(Alignment.Center),
            textAlign = TextAlign.Center
        )
        menuIcon?.let {
            Icon(
                painter = it,
                contentDescription = "menu button",
                modifier = Modifier
                    .padding(16.dp)
                    .align(Alignment.CenterEnd)
                    .clickable { onClickIcon() },
                tint = white
            )
        }
    }
}

@Composable
fun MainToolbar(
    homeIcon: ImageVector,
    onClickHome: () -> Unit,
    title: String,
    menuIcon: Painter?,
    onClickIcon: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = pink)
    ) {
        Box(modifier = Modifier
            .size(width = 48.dp, height = 24.dp)
            .align(Alignment.CenterStart)
            .padding(start = 16.dp)
        ) {
            Icon(
                imageVector = homeIcon,
                contentDescription = "home button",
                modifier = Modifier
                    //.padding(16.dp)
                    .size(24.dp)
                    .align(alignment = Alignment.CenterStart)
                    .clickable { onClickHome() },
                tint = white
            )
        }
        Text(
            text = title,
            style = Typography.h2,
            color = white,
            modifier = Modifier
                .padding(vertical = 16.dp, horizontal = 48.dp)
                .align(Alignment.Center),
            textAlign = TextAlign.Center
        )
        menuIcon?.let {
            Icon(
                painter = it,
                contentDescription = "menu button",
                modifier = Modifier
                    .padding(16.dp)
                    .align(Alignment.CenterEnd)
                    .clickable { onClickIcon() },
                tint = white
            )
        }
    }
}