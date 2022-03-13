package com.the.club.ui.presentation.auth.otp.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.the.club.ui.commonComponents.ButtonsDivider
import com.the.club.ui.theme.*

@Composable
fun SmsGroup(modifier: Modifier, sms: String) {
    val splitText = sms.chunked(1)
    Row(modifier = modifier) {
        for (i in splitText.indices) {
            SmsItem(text = splitText[i], modifier = Modifier.weight(2F))
            if (i < splitText.size-1) {
                ButtonsDivider(modifier = Modifier.weight(1F))
            }
        }
    }
}

@Composable
fun SmsItem(modifier: Modifier, text: String) {
    Box(
        modifier = modifier
            .size(62.dp)
            .background(color = lightGray, shape = Shapes.large)
    ) {
        Text(
            text = text,
            style = Typography.h1,
            textAlign = TextAlign.Center,
            modifier = Modifier.align(Alignment.Center),
            color = if (text != "-") textColor() else lightGray
        )
    }
}