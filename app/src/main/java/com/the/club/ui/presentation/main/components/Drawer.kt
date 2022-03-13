package com.the.club.ui.presentation.main.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.the.club.R
import com.the.club.common.model.AdditionalDrawerList
import com.the.club.common.model.DrawerMenuItem
import com.the.club.common.model.MainDrawerList
import com.the.club.ui.navigation.Screen
import com.the.club.ui.theme.*

@Composable
fun Drawer(
    modifier: Modifier = Modifier,
    userName: String?,
    unread: String?,
    surveys: String?,
    onDestinationClicked: (route: String) -> Unit
) {
    Column(modifier.fillMaxSize()) {
        // header of the drawer
        Row(
            modifier = Modifier
                .background(color = pink)
                .padding(vertical = 16.dp)
                .clickable { onDestinationClicked(Screen.UserProfileScreen.route) }
        ) {
            Column(
                modifier = Modifier
                    .padding(start = 16.dp)
                    .weight(1F)
            ) {
                Text(
                    text = if (userName != null) "${stringResource(id = R.string.hi_there)}, $userName" else stringResource(id = R.string.hi_there),
                    style = Typography.h2,
                    textAlign = TextAlign.Start,
                    color = white
                )
                Text(
                    text = stringResource(id = R.string.go_to_profile),
                    style = Typography.body1,
                    textAlign = TextAlign.Start,
                    color = white,
                    modifier = Modifier
                )
            }
            Icon(
                imageVector = Icons.Filled.ArrowForwardIos,
                contentDescription = "open",
                tint = white,
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .padding(end = 16.dp)
            )
        }
        // main items of the menu
        MainDrawerList.forEach { menuItem ->
            Spacer(modifier = Modifier.height(8.dp))
            DrawerItem(
                item = menuItem,
                onClick = { onDestinationClicked(it.route) },
                unread =
                when (menuItem.rout) {
                    Screen.NotificationsScreen -> unread
                    Screen.SurveysScreen -> surveys
                    else -> null
                }
            )

        }
        Divider(modifier = Modifier.padding(top = 8.dp))
        // additional items of the menu
        AdditionalDrawerList.forEach { menuItem ->
            Spacer(modifier = Modifier.height(8.dp))
            DrawerItem(
                item = menuItem,
                onClick = { onDestinationClicked(it.route) },
                unread = null
            )
        }
    }
}

@Composable
fun DrawerItem(item: DrawerMenuItem, onClick: (Screen) -> Unit, unread: String?) {
    Row(modifier = Modifier
        .fillMaxWidth()
        .padding(start = 19.dp, top = 8.dp, bottom = 8.dp, end = 8.dp)
        .clickable { onClick(item.rout) }
    ) {
        Icon(
            painter = painterResource(id = item.icon),
            contentDescription = "icon",
            tint = pink,
            modifier = Modifier
                .padding(end = 16.dp)
                .size(16.dp)
                .align(Alignment.CenterVertically)
        )
        Text(
            text = stringResource(id = item.text),
            style = Typography.body1,
            color = textColor(),
            modifier = Modifier
                .weight(1F)
                .align(Alignment.CenterVertically)
        )
        unread?.let {
            if (it != "0") {
                Box(
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .background(color = pink, shape = CircleShape)
                ) {
                    Text(
                        text = " $it ",
                        style = Typography.body1,
                        color = white,
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(horizontal = 4.dp, vertical = 2.dp)
                    )
                }
            }
        }
    }
}