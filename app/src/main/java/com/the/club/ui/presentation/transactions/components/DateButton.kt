package com.the.club.ui.presentation.transactions.components

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.the.club.ui.theme.Typography
import com.the.club.ui.theme.pink

@Composable
fun DateButton(modifier: Modifier, textDate: String, onClick: () -> Unit) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(36.dp)
            .border(width = 1.dp, color = pink, shape = RoundedCornerShape(10.dp))
            .clickable { onClick() }
    ) {
        Text(
            text = textDate,
            style = Typography.body1,
            color = pink,
            modifier = Modifier
                .padding(start = 16.dp)
                .weight(1F)
                .align(Alignment.CenterVertically)
        )
        Icon(
            imageVector = Icons.Filled.KeyboardArrowDown,
            contentDescription = "arrow down",
            tint = pink,
            modifier = Modifier.padding(top = 8.dp, bottom = 8.dp, end = 16.dp)
        )
    }
}
