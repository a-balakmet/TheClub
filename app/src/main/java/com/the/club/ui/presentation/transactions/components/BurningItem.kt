package com.the.club.ui.presentation.transactions.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.the.club.common.ktx.formatWithThousand
import com.the.club.domain.model.transactions.Burning
import com.the.club.ui.theme.Typography
import com.the.club.ui.theme.red
import com.the.club.ui.theme.textColor

@Composable
fun BurningItem(burning: Burning, showDivider: Boolean){
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = burning.burningDate,
                style = Typography.body1,
                color = textColor(),
                modifier = Modifier
                    .padding(vertical = 8.dp)
                    .weight(1F)
                    .align(alignment = Alignment.CenterVertically)
            )
            Text(text = burning.burningSum.formatWithThousand(),
                style = Typography.h2,
                color = red,
                textAlign = TextAlign.End,
                modifier = Modifier
                    .padding(vertical = 8.dp)
                    .align(alignment = Alignment.CenterVertically)
            )
        }
        if (showDivider) Divider(modifier = Modifier.padding(top = 8.dp))
    }
}