package com.the.club.ui.presentation.transactions.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.the.club.R
import com.the.club.domain.model.transactions.TransactionProduct
import com.the.club.ui.theme.Typography
import com.the.club.ui.theme.textColor

@Composable
fun TransactionProductsItem(product: TransactionProduct) {
    Row(modifier = Modifier.fillMaxWidth().padding(top = 8.dp)) {
        Text(
            text = product.name,
            style = Typography.body1,
            color = textColor(),
            modifier = Modifier
                .weight(1F)
                .padding(end = 16.dp)
        )
        Column {
            Text(
                text =
                if ((product.quantity - product.quantity.toInt()) == 0.0)
                    "${product.quantity.toString().replace(".0", "")} ${stringResource(id = R.string.pieces)}"
                else "${product.quantity} кг.",
                style = Typography.body1,
                color = textColor(),
                modifier = Modifier.align(alignment = Alignment.End)
            )
            Text(
                text = product.sum,
                style = Typography.body1,
                color = textColor(),
                modifier = Modifier.align(alignment = Alignment.End)
            )
        }
    }
}
