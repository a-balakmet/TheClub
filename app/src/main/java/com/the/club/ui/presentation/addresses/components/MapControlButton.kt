package com.the.club.ui.presentation.addresses.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.the.club.ui.theme.gray

@Composable
fun MapControlButton(icon: ImageVector, onClick: () -> Unit) {
    Box(modifier = Modifier
        .background(color = gray, shape = CircleShape)
        .size(38.dp)
        .clickable { onClick() }
    ) {
        Icon(
            imageVector = icon,
            contentDescription = "mapIco",
            tint = Color.White,
            modifier = Modifier.align(alignment = Alignment.Center)
        )
    }
}