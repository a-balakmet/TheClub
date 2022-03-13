package com.the.club.ui.presentation.surveys.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import com.the.club.R
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.the.club.common.ktx.toFullDate
import com.the.club.domain.model.Survey
import com.the.club.ui.theme.*

@Composable
fun SurveyListItem(survey: Survey, onClick: (Survey) -> Unit) {
    val months = stringArrayResource(id = R.array.months_relative)
    val start = survey.dateStart.toFullDate(months)
    val end = survey.dateEnd.toFullDate(months)
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = cardBackgroundColor(), shape = Shapes.large)
            .clickable {
                onClick(survey)
            }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 16.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            ) {
                Text(
                    text = survey.title,
                    style = Typography.h4,
                    color = textColor(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1F)
                        .align(alignment = Alignment.CenterVertically)
                        .padding(end = 16.dp)
                )
                Icon(
                    imageVector = Icons.Default.ArrowForwardIos,
                    contentDescription = "open survey",
                    tint = pink,
                    modifier = Modifier.size(24.dp)
                )
            }
            Text(
                text = stringResource(id = R.string.validity_date),
                style = Typography.body1,
                color = gray
            )
            Text(
                text = "$start - $end",
                style = Typography.h4,
                color = gray
            )
        }
    }
}