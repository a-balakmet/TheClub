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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.the.club.R
import com.the.club.ui.theme.*

@Composable
fun OneButtonDialog(text: String, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.Black.copy(alpha = 0.5F))
    ) {
        Column(
            modifier = Modifier
                .background(color = backgroundColor(), shape = Shapes.large)
                .align(alignment = Alignment.Center)
                .padding(all = 8.dp)
        ) {
            Logo(
                modifier = Modifier
                    .padding(horizontal = 8.dp, vertical = 16.dp)
                    .align(alignment = Alignment.CenterHorizontally),
                colors = if (text == stringResource(id = R.string.loading)) MainCardColors else GrayColorShades,
                isAnimate = text == stringResource(id = R.string.loading),
                text = text,
                textColor = textColor()
            )
            if (text != stringResource(id = R.string.loading)) {
                NonBoarderButton(
                    text = "OK",
                    modifier = Modifier
                        .padding(all = 8.dp)
                        .align(alignment = Alignment.CenterHorizontally)
                ) {
                    onClick()
                }
            }
        }
    }
}