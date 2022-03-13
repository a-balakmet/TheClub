package com.the.club.ui.commonComponents

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.the.club.ui.theme.lightGray

@Composable
fun NumPad(modifier: Modifier, onClick: (String) -> Unit) {
    Column(modifier = modifier
        //.padding(horizontal = 5.dp)
        .background(color = lightGray),
        verticalArrangement = Arrangement.spacedBy(5.dp)) {
        Row(modifier = Modifier
            .padding(top = 5.dp, start = 5.dp, end = 5.dp),
            //.align(Alignment.CenterHorizontally),
            horizontalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            NumberButton(
                modifier = Modifier.weight(1F),
                text = "1")
            { onClick("1") }
            NumberButton(
                modifier = Modifier.weight(1F),
                text = "2")
            { onClick("2") }
            NumberButton(
                modifier = Modifier.weight(1F),
                text = "3")
            { onClick("3") }
        }
        Row(modifier = Modifier.padding(horizontal = 5.dp),
            horizontalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            NumberButton(
                modifier = Modifier.weight(1F),
                text = "4") {
                onClick("4") }
            NumberButton(
                modifier = Modifier.weight(1F),
                text = "5")
            { onClick("5") }
            NumberButton(
                modifier = Modifier.weight(1F),
                text = "6")
            { onClick("6") }
        }
        Row(modifier = Modifier.padding(horizontal = 5.dp),
            horizontalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            NumberButton(
                modifier = Modifier.weight(1F),
                text = "7")
            { onClick("7")}
            NumberButton(
                modifier = Modifier.weight(1F),
                text = "8")
            { onClick("8") }
            NumberButton(
                modifier = Modifier.weight(1F),
                text = "9")
            { onClick("9") }
        }
        Row(modifier = Modifier.padding(start = 5.dp, end = 5.dp, bottom = 5.dp),
            horizontalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            Box(
                modifier = Modifier.size(56.dp).weight(1F)
            )
            NumberButton(
                modifier = Modifier.weight(1F),
                text = "0"
            )
            { onClick("0") }
            NumberButton(
                modifier = Modifier.weight(1F),
                text = "\u232b"
            )
            { onClick("-") }
        }
    }
}
