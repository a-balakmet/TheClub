package com.the.club.ui.presentation.addresses.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.model.LatLng
import com.the.club.R
import com.the.club.common.ktx.toHours
import com.the.club.common.ktx.toTimeToClose
import com.the.club.domain.model.shops.Shop
import com.the.club.ui.theme.*

@Composable
fun ShopBottomDialog(shop: Shop, onClick: (LatLng) -> Unit) {
    val time2close = shop.openHour.toTimeToClose(shop.closeHour)
    Box(
        Modifier
            .fillMaxWidth()
            .background(color = backgroundColor())
    ) {
        Column(
            Modifier.fillMaxWidth(),
        ) {
            Box(
                modifier = Modifier
                    .padding(top = 10.dp)
                    .size(width = 44.dp, height = 4.dp)
                    .background(color = lightGray, shape = Shapes.large)
                    .align(alignment = Alignment.CenterHorizontally),
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 44.dp, top = 26.dp, end = 24.dp)
            ) {
                Text(
                    text = shop.name,
                    style = Typography.h2,
                    color = pink,
                    modifier = Modifier.weight(1F)
                )
                Icon(
                    painter = painterResource(id = R.drawable.ic_navigation),
                    contentDescription = "navigation",
                    tint = pink,
                    modifier = Modifier.clickable { onClick(shop.geo!!) }
                )
            }
            Divider(
                modifier = Modifier
                    .padding(all = 16.dp)
                    .background(color = lightGray)
                    .height(1.dp)
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
            ) {
                Icon(
                    imageVector = Icons.Outlined.LocationOn,
                    contentDescription = "location",
                    tint = pink,
                    modifier = Modifier.size(16.dp)
                )
                Column(modifier = Modifier
                    .padding(start = 12.dp)
                    .weight(1F)
                ) {
                    Text(
                        text = stringResource(id = R.string.address),
                        style = Typography.body1,
                        color = gray
                    )
                    Text(
                        text = shop.address ?: "",
                        style = Typography.h2,
                        color = textColor(),
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 24.dp, top = 24.dp, end = 24.dp, bottom = 48.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.AccessTime,
                    contentDescription = "time",
                    tint = pink,
                    modifier = Modifier
                        .size(16.dp)
                )
                Column(modifier = Modifier
                    .padding(start = 12.dp)
                    .weight(1F)
                ) {
                    Text(
                        text = stringResource(id = R.string.working_hours),
                        style = Typography.body1,
                        color = gray
                    )
                    Text(
                        text = shop.openHour.toHours(closeHour = shop.closeHour),
                        style = Typography.body1,
                        color = textColor(),
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                    Box(modifier = Modifier.background(color = veryLightPink, shape = Shapes.medium)) {
                        Text(
                            text = stringResource(id = R.string.untilClosing, time2close[0], time2close[1]),
                            style = Typography.body1,
                            color = pink,
                            modifier = Modifier
                                .padding(all = 8.dp)

                        )
                    }
                }
            }
        }
    }
}