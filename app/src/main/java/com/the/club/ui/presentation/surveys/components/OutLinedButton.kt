package com.the.club.ui.presentation.surveys.components

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.the.club.data.remote.surveys.dto.Option
import com.the.club.ui.theme.Typography
import com.the.club.ui.theme.backgroundColor
import com.the.club.ui.theme.grayMedium
import com.the.club.ui.theme.pink

@Composable
fun OutlinedButton(modifier: Modifier = Modifier, option: Option, text: String, isCenter: Boolean, onClick: (Boolean) -> Unit) {
    val borderColor =
        if (option.isSelected.value) pink
        else grayMedium
    Box(
        modifier = modifier
            .height(40.dp)
            .border(width = 1.dp, color = borderColor, shape = RoundedCornerShape(10.dp))
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = rememberRipple(color = backgroundColor()),
            ) {
                onClick(true)
            }
    ) {
        Text(
            text = text,
            style = Typography.body1,
            color = borderColor,
            modifier = Modifier
                .padding(start = if(isCenter) 1.dp else 36.dp, end = if(isCenter) 1.dp else 8.dp)
                .align(alignment = if(isCenter) Alignment.Center else Alignment.CenterStart)
        )
    }
}