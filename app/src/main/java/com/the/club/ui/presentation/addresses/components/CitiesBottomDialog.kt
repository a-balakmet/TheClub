package com.the.club.ui.presentation.addresses.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.chargemap.compose.numberpicker.ListItemPicker
import com.the.club.R
import com.the.club.domain.model.City
import com.the.club.ui.theme.*

@Composable
fun CitiesBottomDialog(citiesList: List<City>, onClickCancel: () -> Unit, onClickReady: (City) -> Unit) {
    var cityPickerValue by remember { mutableStateOf(citiesList[0]) }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = dialogBackground)
    ) {
        Column(modifier = Modifier.padding(vertical = 16.dp)) {
            Row(Modifier.fillMaxWidth()) {
                Text(
                    text = stringResource(id = R.string.cancel),
                    style = Typography.body1,
                    color = blue,
                    textAlign = TextAlign.Start,
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .weight(1F)
                        .clickable { onClickCancel() }
                )
                Text(
                    text = stringResource(id = R.string.ready),
                    style = Typography.body1,
                    color = blue,
                    textAlign = TextAlign.End,
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .weight(1F)
                        .clickable { onClickReady(cityPickerValue) }
                )
            }
            // sex picker
            ListItemPicker(
                modifier = Modifier
                    .align(alignment = Alignment.CenterHorizontally)
                    .padding(bottom = 16.dp),
                value = cityPickerValue,
                label = { it.name },
                onValueChange = { cityPickerValue = it },
                list = citiesList,
                dividersColor = semiTransparent,
                textStyle = TextStyle(
                    color = white,
                    fontFamily = AppFont,
                    fontWeight = FontWeight.Normal,
                    fontSize = 17.sp,
                )
            )
        }
    }
}