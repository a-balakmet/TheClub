package com.the.club.ui.commonComponents

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.the.club.ui.theme.GrayColorShades
import com.the.club.ui.theme.Shapes
import com.the.club.ui.theme.backgroundColor
import com.the.club.ui.theme.textColor

@Composable
fun ErrorDialog(showDialog: Boolean, setShowDialog: (Boolean) -> Unit, text: String, onClick: () -> Unit) {
    if (showDialog) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color.Black.copy(alpha = 0.5F))
        ) {
            Column(
                modifier = Modifier
                    .background(color = backgroundColor(), shape = Shapes.large)
                    .align(alignment = Alignment.Center)
                    .padding(all = 16.dp)
            ) {
                Logo(
                    modifier = Modifier
                        .padding(horizontal = 24.dp, vertical = 8.dp)
                        .align(alignment = Alignment.CenterHorizontally),
                    colors = GrayColorShades,
                    isAnimate = false,
                    text = text,
                    textColor = textColor()
                )
                NonBoarderButton(
                    text = "OK",
                    modifier = Modifier
                        .padding(all = 8.dp)
                        .align(alignment = Alignment.CenterHorizontally)
                ) {
                    setShowDialog(false)
                    onClick()
                }
            }
        }
    }
}