package com.the.club.ui.commonComponents

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.the.club.R

@Composable
fun RightBorder(modifier: Modifier) {
    Image(
        painter = painterResource(id = R.drawable.ic_curved_border),
        contentDescription = "border",
        contentScale = ContentScale.FillBounds,
        modifier = modifier
    )
}