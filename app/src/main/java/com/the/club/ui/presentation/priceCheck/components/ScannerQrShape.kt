package com.the.club.ui.presentation.priceCheck.components

import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color

@Composable
fun ScannerQrShape(
    modifier: Modifier,
    lineLength: Float,
) {
    val strokeWidth = 8F
    val radius = 4f
    Canvas(
        modifier = modifier,
        onDraw = {
            drawLine(
                color = Color.White,
                start = Offset(0F, 0F),
                end = Offset(0F, lineLength),
                strokeWidth = strokeWidth
            )
            drawLine(
                color = Color.White,
                start = Offset(0F, 0F),
                end = Offset(lineLength, 0F),
                strokeWidth = strokeWidth
            )
            drawLine(
                color = Color.White,
                start = Offset(size.width, 0F),
                end = Offset(size.width-lineLength, 0F),
                strokeWidth = strokeWidth
            )
            drawLine(
                color = Color.White,
                start = Offset(size.width, 0F),
                end = Offset(size.width, lineLength),
                strokeWidth = strokeWidth
            )
            drawLine(
                color = Color.White,
                start = Offset(size.width, size.height),
                end = Offset(size.width, size.height-lineLength),
                strokeWidth = strokeWidth
            )
            drawLine(
                color = Color.White,
                start = Offset(size.width, size.height),
                end = Offset(size.width-lineLength, size.height),
                strokeWidth = strokeWidth
            )
            drawLine(
                color = Color.White,
                start = Offset(0F, size.height),
                end = Offset(0F, size.height-lineLength),
                strokeWidth = strokeWidth
            )
            drawLine(
                color = Color.White,
                start = Offset(0F, size.height),
                end = Offset(lineLength, size.height),
                strokeWidth = strokeWidth
            )
            drawCircle(
                color = Color.White,
                radius = radius,
                center = Offset(0F, 0F)
            )
            drawCircle(
                color = Color.White,
                radius = radius,
                center = Offset(0F, size.height)
            )
            drawCircle(
                color = Color.White,
                radius = radius,
                center = Offset(size.width, size.height)
            )
            drawCircle(
                color = Color.White,
                radius = radius,
                center = Offset(size.width, 0F)
            )
        }
    )
}
