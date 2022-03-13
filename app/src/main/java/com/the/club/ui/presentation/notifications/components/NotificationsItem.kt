package com.the.club.ui.presentation.notifications.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.the.club.R
import com.the.club.domain.model.Notification
import com.the.club.ui.theme.*

@Composable
fun NotificationsItem(notification: Notification, onClick: (String) -> Unit) {
    Row(modifier = Modifier
        .fillMaxWidth()
        .padding(bottom = 8.dp)
        .clickable { notification.link?.let { onClick(it) } }
    ) {
        Box(
            modifier = Modifier
                .size(24.dp)
                .clip(CircleShape)
                .background(color = red)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_logo),
                contentDescription = "logo",
                modifier = Modifier
                    .padding(2.dp)
                    .align(Alignment.Center),
                tint = white
            )
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 8.dp)
                .background(color = cardBackgroundColor(), shape = Shapes.large)
        ) {
            Text(
                text = notification.title,
                style = Typography.h4,
                color = textColor(),
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(16.dp)
                    .align(alignment = Alignment.CenterHorizontally)
            )
            Text(
                text = notification.message,
                style = Typography.body1,
                color = textColor(),
                modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 8.dp)
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp, bottom = 8.dp)
            ) {
                Text(
                    text = notification.time,
                    style = Typography.body2,
                    color = gray,
                    modifier = Modifier
                        .weight(1F)
                        .align(alignment = Alignment.CenterVertically)
                )
                notification.link?.let {
                    Text(
                        text = stringResource(id = R.string.in_details),
                        style = Typography.body1,
                        color = red
                    )
                }
            }
        }
    }
}