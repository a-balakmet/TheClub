package com.the.club.ui.commonComponents

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.chargemap.compose.numberpicker.ListItemPicker
import com.chargemap.compose.numberpicker.NumberPicker
import com.the.club.R
import com.the.club.common.ktx.DD_MM_YYYY
import com.the.club.common.ktx.getLastDayOfMonth
import com.the.club.common.ktx.toDate
import com.the.club.common.ktx.toString
import com.the.club.ui.theme.*
import java.util.*

@Composable
fun DatePickerBottomDialog(
    dateValue: String,
    iBirthday: Boolean,
    onClick: (String?) -> Unit
) {
    val theDate = dateValue.split(".")
    var dayPickerValue by remember { mutableStateOf(theDate[0].toInt()) }
    val monthNames = stringArrayResource(id = R.array.months_relative).toList()
    var monthPickerValue by remember { mutableStateOf(monthNames[theDate[1].toInt() - 1]) }
    var theMonth = monthNames.indexOf(monthPickerValue) + 1
    var yearPickerValue by remember { mutableStateOf(theDate[2].toInt()) }
    val yearMin = if (iBirthday) 1900 else 2000
    val yearMax = Date().toString(DD_MM_YYYY).split(".")[2].toInt()
    var dayMax by remember { mutableStateOf(31) }
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
            Row(Modifier.fillMaxWidth()) {
                Text(
                    text = stringResource(id = R.string.cancel),
                    style = Typography.body1,
                    color = blue,
                    textAlign = TextAlign.Start,
                    modifier = Modifier
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                        .weight(1F)
                        .clickable { onClick(null) }
                )
                Text(
                    text = stringResource(id = R.string.ready),
                    style = Typography.body1,
                    color = blue,
                    textAlign = TextAlign.End,
                    modifier = Modifier
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                        .weight(1F)
                        .clickable {
                            val newDate = ("$dayPickerValue.$theMonth.$yearPickerValue")
                                .toDate(pattern = DD_MM_YYYY)
                                ?.toString(DD_MM_YYYY)
                            onClick(newDate)
                        }
                )
            }
            // row of pickers
            Row(modifier = Modifier.align(alignment = Alignment.CenterHorizontally)) {
                // day picker
                NumberPicker(
                    value = dayPickerValue,
                    onValueChange = {
                        dayPickerValue = it
                    },
                    range = 1..dayMax,
                    dividersColor = semiTransparent,
                    textStyle = TextStyle(
                        color = white,
                        fontFamily = AppFont,
                        fontWeight = FontWeight.Normal,
                        fontSize = 17.sp,
                    )
                )
                // month picker
                ListItemPicker(
                    value = monthPickerValue,
                    onValueChange = {
                        monthPickerValue = it
                        theMonth = monthNames.indexOf(it) + 1
                        ("01.$theMonth.$yearPickerValue").toDate(pattern = DD_MM_YYYY)?.let { theDate ->
                            dayMax = getLastDayOfMonth(theDate)
                            if (dayPickerValue > dayMax) dayPickerValue = dayMax
                        }
                    },
                    list = monthNames,
                    dividersColor = semiTransparent,
                    textStyle = TextStyle(
                        color = white,
                        fontFamily = AppFont,
                        fontWeight = FontWeight.Normal,
                        fontSize = 17.sp,
                    )
                )
                // year picker
                NumberPicker(
                    value = yearPickerValue,
                    onValueChange = {
                        yearPickerValue = it
                        ("01.$theMonth.$yearPickerValue").toDate(pattern = DD_MM_YYYY)?.let { theDate ->
                            dayMax = getLastDayOfMonth(theDate)
                            if (dayPickerValue > dayMax) dayPickerValue = dayMax
                        }
                    },
                    range = yearMin..yearMax,
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
}