package com.the.club.ui.presentation.transactions.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.the.club.R
import com.the.club.common.ktx.formatWithThousand
import com.the.club.common.ktx.toFullDate
import com.the.club.domain.model.transactions.Transaction
import com.the.club.ui.theme.*

@Composable
fun BurningItemFull(transaction: Transaction) {
    val months = stringArrayResource(id = R.array.months_relative)
    val theDate = transaction.date.toFullDate(months)
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Box(
            modifier = Modifier
                .size(38.dp)
                .background(color = pink.copy(alpha = 0.07F), shape = CircleShape)
                .align(alignment = Alignment.CenterVertically)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_burning),
                contentDescription = "burning",
                tint = red,
                modifier = Modifier.align(alignment = Alignment.Center)
            )
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1F)
                .padding(horizontal = 16.dp)
                .align(alignment = Alignment.CenterVertically)
        ) {
            Text(
                text = stringResource(id = R.string.burning_bonus),
                style = Typography.body1,
                color = textColor(),
                modifier = Modifier.padding(bottom = 4.dp)
            )
            Text(
                text = theDate,
                style = Typography.body1,
                color = gray,
            )
        }
        Text(
            text = transaction.withdrawalBonuses.formatWithThousand(),
            style = Typography.h2,
            color = red,
            textAlign = TextAlign.End,
            modifier = Modifier
                .padding(vertical = 8.dp)
                .align(alignment = Alignment.Top)
        )
    }
}
