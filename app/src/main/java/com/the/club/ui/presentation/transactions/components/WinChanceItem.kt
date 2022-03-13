package com.the.club.ui.presentation.transactions.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.the.club.domain.model.transactions.WinningChance
import com.the.club.ui.theme.Typography
import com.the.club.ui.theme.textColor
import com.the.club.ui.theme.yellow

@Composable
fun WinChanceItem(chance: WinningChance){
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Bottom
    ) {
        Text(
            text = chance.campaignName,
            style = Typography.body1,
            color = textColor(),
        )
        Spacer(modifier = Modifier.weight(1F))
        Text(
            text = "+${chance.chance}",
            style = Typography.h3,
            color = yellow
        )
    }
}