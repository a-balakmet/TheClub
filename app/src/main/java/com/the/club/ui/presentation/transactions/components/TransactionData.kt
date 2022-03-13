package com.the.club.ui.presentation.transactions.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.the.club.R
import com.the.club.common.ktx.formatWithThousand
import com.the.club.common.ktx.toFullDate
import com.the.club.domain.model.transactions.Transaction
import com.the.club.ui.theme.*

@Composable
fun TransactionData(transaction: Transaction, shopAddress: String) {
    val months = stringArrayResource(id = R.array.months_relative)
    val theDate = transaction.date.toFullDate(months)
    Column(modifier = Modifier.padding(top = 16.dp)) {
        // shop name
        Text(
            text = shopAddress,
            color = gray,
            modifier = Modifier.padding(bottom = 4.dp)
        )
        // row for date and time
        Row(modifier = Modifier.fillMaxWidth()) {
            Icon(
                imageVector = Icons.Filled.AccessTime,
                contentDescription = "time",
                tint = gray,
                modifier = Modifier
                    .size(14.dp)
                    .align(alignment = Alignment.CenterVertically)
            )
            Text(
                text = theDate,
                color = gray,
                modifier = Modifier.padding(start = 4.dp)
            )
            Text(
                text = transaction.time,
                color = gray,
                modifier = Modifier.padding(start = 4.dp)
            )
        }
        // row for bonuses
        Row(
            modifier = Modifier.fillMaxWidth().padding(top = 16.dp)
        ) {
            Text(
                text = stringResource(id = R.string.charged),
                color = textColor(),
                modifier = Modifier
                    .weight(1F)
                    .align(alignment = Alignment.CenterVertically)
            )
            Text(
                text = "+ ${transaction.accruedBonuses.formatWithThousand()}",
                style = Typography.h3,
                color = green
            )
        }
        // row for withdrawals
        if (transaction.withdrawalBonuses < 0) {
            Row(modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = stringResource(id = R.string.withdrawn),
                    color = textColor(),
                    modifier = Modifier
                        .weight(1F)
                        .align(alignment = Alignment.CenterVertically)
                )
                Text(
                    text = transaction.withdrawalBonuses.formatWithThousand(),
                    style = Typography.h3,
                    color = orange
                )
            }
        }
    }
}