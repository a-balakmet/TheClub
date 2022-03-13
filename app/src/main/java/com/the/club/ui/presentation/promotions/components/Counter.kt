package com.the.club.ui.presentation.promotions.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.the.club.ui.theme.Shapes
import com.the.club.ui.theme.Typography
import com.the.club.ui.theme.pink
import com.the.club.ui.theme.white

@Composable
fun CounterItem(modifier: Modifier, isCollapsed: Boolean, value: String){
    Box(
        modifier = modifier
            .background(color = if (isCollapsed) white else pink, shape = Shapes.medium)
            .size(width = 25.dp, height = 35.dp)
    ) {
        Text(
            text = value,
            style = Typography.h2,
            color = if (isCollapsed) pink else white,
            modifier = Modifier.align(alignment = Alignment.Center)
        )
    }
}

@Composable
fun CounterRow(values: List<String>, isCollapsed: Boolean) {
    Row {
        CounterItem(modifier = Modifier.padding(end = 4.dp), isCollapsed, value = values[0])
        CounterItem(modifier = Modifier.padding(end = 16.dp), isCollapsed, value = values[1])
        CounterItem(modifier = Modifier.padding(end = 4.dp), isCollapsed, value = values[2])
        CounterItem(modifier = Modifier.padding(end = 16.dp), isCollapsed, value = values[3])
        CounterItem(modifier = Modifier.padding(end = 4.dp), isCollapsed, value = values[4])
        CounterItem(modifier = Modifier.padding(end = 16.dp), isCollapsed, value = values[5])
        CounterItem(modifier = Modifier.padding(end = 4.dp), isCollapsed, value = values[6])
        CounterItem(modifier = Modifier.padding(end = 0.dp), isCollapsed, value = values[7])
    }
}

