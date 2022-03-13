package com.the.club.ui.commonComponents

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lens
import androidx.compose.material.icons.outlined.Lens
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.the.club.ui.theme.grayMedium

@Composable
fun PinGroup(modifier: Modifier, length: Int) {
    Row(modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(26.dp)
    ) {
        Icon(
            imageVector = if (length > 0) Icons.Filled.Lens else Icons.Outlined.Lens,
            contentDescription = "one",
            tint = grayMedium)
        Icon(
            imageVector = if (length > 1) Icons.Filled.Lens else Icons.Outlined.Lens,
            contentDescription = "two",
            tint = grayMedium)
        Icon(
            imageVector = if (length > 2) Icons.Filled.Lens else Icons.Outlined.Lens,
            contentDescription = "three",
            tint = grayMedium)
        Icon(
            imageVector = if (length > 3) Icons.Filled.Lens else Icons.Outlined.Lens,
            contentDescription = "four",
            tint = grayMedium)
    }
}