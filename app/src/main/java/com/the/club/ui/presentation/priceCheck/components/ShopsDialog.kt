package com.the.club.ui.presentation.priceCheck.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.the.club.R
import com.the.club.domain.model.shops.Shop
import com.the.club.ui.commonComponents.BoarderButton
import com.the.club.ui.theme.*

@Composable
fun ShopsDialog(shops: List<Shop>, onClickShop:(Shop) -> Unit, onClickCancel:() -> Unit){
    Column(modifier = Modifier
        .fillMaxWidth()
        .background(color = backgroundColor())
    ) {
        Box(
            modifier = Modifier
                .padding(top = 10.dp)
                .size(width = 44.dp, height = 4.dp)
                .background(color = lightGray, shape = Shapes.large)
                .align(alignment = Alignment.CenterHorizontally),
        )
        Text(
            text = stringResource(id = R.string.nearest_shops),
            style = Typography.h2,
            color = textColor(),
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 24.dp)
        )
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            items(shops) { shopItem ->
                ShortShopListItem(shop = shopItem, onClick = {
                    onClickShop(it)
                })
            }
        }
        BoarderButton(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp, vertical = 16.dp),
            text = stringResource(id = R.string.no_shop_in_list),
            onClick = { onClickCancel() }
        )
    }
}