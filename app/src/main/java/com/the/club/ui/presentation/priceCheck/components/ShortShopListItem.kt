package com.the.club.ui.presentation.priceCheck.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.the.club.common.ktx.toHours
import com.the.club.domain.model.shops.Shop
import com.the.club.ui.theme.*

@Composable
fun ShortShopListItem(shop: Shop, onClick: (Shop) -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = cardBackgroundColor(), shape = Shapes.large)
            .clickable { onClick(shop) }
    ) {
        Column(modifier = Modifier.padding(horizontal = 24.dp, vertical = 10.dp)) {
            Text(
                text = shop.name,
                style = Typography.h2,
                color = textColor(),
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Text(
                text = shop.address ?: "",
                style = Typography.body1,
                color = gray,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            Row(modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)) {
                Icon(
                    imageVector = Icons.Default.AccessTime,
                    contentDescription = "time",
                    tint = pink,
                    modifier = Modifier
                        .size(21.dp)
                        .padding(end = 8.dp)
                        .align(alignment = Alignment.CenterVertically)
                )
                Text(
                    text = shop.openHour.toHours(closeHour = shop.closeHour),
                    style = Typography.body1,
                    color = gray,
                    modifier = Modifier.align(alignment = Alignment.CenterVertically)
                )
            }
        }
    }
}