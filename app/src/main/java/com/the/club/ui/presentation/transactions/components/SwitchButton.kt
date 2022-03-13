package com.the.club.ui.presentation.transactions.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.the.club.ui.theme.*

@Composable
fun SwitchButton(modifier: Modifier, text: String, isActive: Boolean, onClick: () -> Unit) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(if (isActive) RoundedCornerShape(10.dp) else RoundedCornerShape(0.dp))
            .background(color = if (isActive) pink else backgroundColor())
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = rememberRipple(color = backgroundColor()),
            ) {
                onClick()
            }
    ) {
        Text(
            text = text,
            style = Typography.body1,
            color = if (isActive) white else textColor(),
            modifier = Modifier
                .padding(vertical = 8.dp)
                .align(Alignment.Center)
        )
    }
}