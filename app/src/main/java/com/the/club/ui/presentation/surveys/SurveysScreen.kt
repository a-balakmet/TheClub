package com.the.club.ui.presentation.surveys

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIos
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavController
import com.the.club.R
import com.the.club.common.CommonKeys
import com.the.club.ui.commonComponents.*
import com.the.club.ui.navigation.Screen
import com.the.club.ui.presentation.surveys.components.SurveyListItem
import com.the.club.ui.theme.*

@Composable
fun SurveysScreen(navController: NavController){
    val viewModel = hiltViewModel<SurveysViewModel>()
    BackHandler(onBack = navController::popBackStack)
    OnLifecycleEvent { _, event ->
        when (event) {
            Lifecycle.Event.ON_RESUME -> {
                viewModel.getSurveys()
            }
            else -> {}
        }
    }
    Surface(color = MaterialTheme.colors.background) {
        Column {
            // toolbar
            MainToolbar(
                homeIcon = Icons.Filled.ArrowBackIos,
                onClickHome = {  navController.popBackStack() },
                title = stringResource(id = R.string.questionnaire_title),
                menuIcon = null,
                onClickIcon = { }
            )
            // content
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = backgroundColor())
                    .padding(horizontal = 24.dp, vertical = 16.dp)
            ) {
                viewModel.surveysState.value.let{
                    // loading surveys
                    if (it.isLoading) {
                        Column(modifier = Modifier.align(Alignment.Center)) {
                            Logo(
                                modifier = Modifier.align(Alignment.CenterHorizontally),
                                colors = MainCardColors,
                                isAnimate = true,
                                text = stringResource(id = R.string.loading),
                                textColor = textColor()
                            )
                        }
                    }
                    // list of surveys
                    if (viewModel.surveysState.value.surveys.isNotEmpty()) {
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(horizontal = 24.dp, vertical = 32.dp),
                            verticalArrangement = Arrangement.spacedBy(32.dp),
                        ) {
                            items(it.surveys) { aSurvey ->
                                SurveyListItem(survey = aSurvey, onClick = { theSurvey->
                                    navController.navigate(Screen.SurveyScreen.route + "/${theSurvey.id}/${theSurvey.title}")
                                })
                            }
                        }
                    }
                    // errors during loading surveys
                    if (it.error.isNotBlank()) {
                        Column(modifier = Modifier.align(Alignment.Center)) {
                            Logo(
                                modifier = Modifier.align(Alignment.CenterHorizontally),
                                colors = GrayColorShades,
                                isAnimate = false,
                                text =
                                if (viewModel.surveysState.value.error == CommonKeys.noNetwork) stringResource(id = R.string.no_internet_connection)
                                else viewModel.surveysState.value.error,
                                textColor = if (viewModel.surveysState.value.error == CommonKeys.noNetwork) red else textColor()
                            )
                        }
                    }
                    // no surveys
                    if (it.isEmptyList) {
                        NoDataMessage(
                            modifier = Modifier.align(alignment = Alignment.Center),
                            icon = R.drawable.ic_questionnaire
                        )
                    }
                }
            }
        }
    }
}