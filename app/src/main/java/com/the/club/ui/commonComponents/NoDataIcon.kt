package com.the.club.ui.commonComponents

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.the.club.R
import com.the.club.ui.theme.Typography
import com.the.club.ui.theme.gray

@Composable
fun NoDataMessage(modifier: Modifier, icon: Int) {
    Column(modifier = modifier) {
        Icon(
            painter = painterResource(id = icon),
            contentDescription = "icon",
            tint = gray,
            modifier = Modifier.align(alignment = Alignment.CenterHorizontally)
        )
        Text(
            text = stringResource(id = R.string.no_data),
            style = Typography.body2,
            color = gray,
            modifier = Modifier.align(alignment = Alignment.CenterHorizontally)
        )
    }
}