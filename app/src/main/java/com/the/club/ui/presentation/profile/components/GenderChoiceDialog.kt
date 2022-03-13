package com.the.club.ui.presentation.profile.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.the.club.R
import com.the.club.ui.theme.*

@Composable
fun GenderChoiceDialog(gender: String, onClick: (String) -> Unit) {
    val initValue = gender == "male"
    var isMale by remember { mutableStateOf(initValue) }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = dialogBackground)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(alignment = Alignment.BottomCenter)
                .padding(vertical = 16.dp)
        ) {
            Row(modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 48.dp)
            ) {
                Text(
                    text = stringResource(id = R.string.choose_sex),
                    style = Typography.h2,
                    color = white,
                    modifier = Modifier
                        .padding(all = 10.dp)
                        .weight(1F)
                )
                Text(text = stringResource(id = R.string.ready), style = Typography.body1, color = blue, textAlign = TextAlign.End, modifier = Modifier
                    .padding(end = 16.dp)
                    .align(alignment = Alignment.CenterVertically)
                    .clickable {
                        onClick(
                            if (isMale) "male" else "female"
                        )
                    })
            }
            Box(modifier = Modifier.fillMaxWidth()
                .padding(bottom = 24.dp)
                .background(color = if (isMale) lightGray else dialogBackground)
                .clickable { isMale = true }) {
                Text(
                    text = stringResource(id = R.string.male),
                    style = Typography.h2,
                    color = if (isMale) white else lightGray,
                    modifier = Modifier.padding(vertical = 4.dp).align(alignment = Alignment.Center)
                )
            }
            Box(modifier = Modifier.fillMaxWidth()
                .padding(bottom = 24.dp)
                .background(color = if (!isMale) lightGray else dialogBackground)
                .clickable { isMale = false }) {
                Text(
                    text = stringResource(id = R.string.female),
                    style = Typography.h2,
                    color = if (!isMale) white else lightGray,
                    modifier = Modifier.align(alignment = Alignment.Center)
                )
            }
        }
    }
}