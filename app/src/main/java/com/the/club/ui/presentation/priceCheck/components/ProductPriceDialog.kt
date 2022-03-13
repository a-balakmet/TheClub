package com.the.club.ui.presentation.priceCheck.components

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.consumeAllChanges
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.the.club.R
import com.the.club.data.remote.shops.dto.ProductPrice
import com.the.club.domain.model.shops.Shop
import com.the.club.ui.theme.*

@Composable
fun ProductPriceDialog(shop: Shop, productPrice: ProductPrice, onDrag: () -> Unit){
    Column(modifier = Modifier
        .fillMaxWidth()
        .background(color = backgroundColor())
        .pointerInput(Unit) {
            detectDragGestures(onDragEnd = {
                onDrag()
            }){ change, _ ->
                change.consumeAllChanges()
            }
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = pink, RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp))
        ) {
            Box(
                modifier = Modifier
                    .padding(top = 10.dp)
                    .size(width = 44.dp, height = 4.dp)
                    .background(color = white, shape = Shapes.large)
                    .align(alignment = Alignment.CenterHorizontally),
            )
            Text(
                text = shop.name,
                style = Typography.h2,
                color = white,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp)
            )
            Text(
                text = shop.address ?: "",
                style = Typography.body1,
                color = white,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp)
            )
        }
        Column(modifier = Modifier
            .fillMaxWidth()
            .background(color = backgroundColor())
        ) {
            Text(
                text = stringResource(id = R.string.product_name),
                color = textColor(),
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 72.dp)
            )
            Text(
                text = productPrice.name,
                style = Typography.h2,
                color = textColor(),
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 24.dp, top = 16.dp, end = 24.dp)
            )
            Text(
                text = "${productPrice.price} ₸",
                style = Typography.h6,
                color = textColor(),
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 40.dp)
            )
        }
    }
}