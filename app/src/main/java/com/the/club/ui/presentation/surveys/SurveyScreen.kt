package com.the.club.ui.presentation.surveys

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIos
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.the.club.R
import com.the.club.common.CommonKeys
import com.the.club.ui.commonComponents.*
import com.the.club.ui.presentation.surveys.components.QuestionList
import com.the.club.ui.theme.*

@ExperimentalComposeUiApi
@Composable
fun SurveyScreen(navController: NavController, surveyName: String) {
    val viewModel = hiltViewModel<SurveyViewModel>()
    var uploadingErrors by remember { mutableStateOf("") }
    val (showOkDialog, setShowOkDialog) = remember { mutableStateOf(false) }
    val (showErrorsDialog, setShowErrorsDialog) = remember { mutableStateOf(false) }
    BackHandler(onBack = navController::popBackStack)
    Surface(color = MaterialTheme.colors.background) {
        Column {
            // toolbar
            MainToolbar(
                homeIcon = Icons.Filled.ArrowBackIos,
                onClickHome = { navController.popBackStack() },
                title = surveyName,
                menuIcon = null,
                onClickIcon = { }
            )
            // content of screen
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = backgroundColor())
                    .padding(horizontal = 24.dp, vertical = 16.dp)
            ) {
                // content of survey questions
                Column(
                    modifier = Modifier.fillMaxSize()
                ) {
                    viewModel.questionsState.value.let {
                        // loading questions
                        if (it.isLoading) {
                            Box(modifier = Modifier.fillMaxSize()) {
                                Logo(
                                    modifier = Modifier.align(Alignment.Center),
                                    colors = MainCardColors,
                                    isAnimate = true,
                                    text = stringResource(id = R.string.loading),
                                    textColor = textColor()
                                )
                            }
                        }
                        // list of survey questions
                        if (it.questions.isNotEmpty()) {
                            LazyColumn(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .weight(1F),
                                //.padding(horizontal = 24.dp, vertical = 24.dp),
                                verticalArrangement = Arrangement.spacedBy(32.dp),
                            ) {
                                items(it.questions) { aQuestion ->
                                    QuestionList(question = aQuestion, viewModel = viewModel)
                                }
                            }
                        }
                        // errors during loading survey
                        if (it.error.isNotBlank()) {
                            Box(modifier = Modifier.fillMaxSize()) {
                                Logo(
                                    modifier = Modifier.align(Alignment.Center),
                                    colors = GrayColorShades,
                                    isAnimate = false,
                                    text =
                                    if (viewModel.questionsState.value.error == CommonKeys.noNetwork) stringResource(id = R.string.no_internet_connection)
                                    else viewModel.questionsState.value.error,
                                    textColor = if (viewModel.questionsState.value.error == CommonKeys.noNetwork) red else textColor()
                                )
                            }
                        }
                        // no surveys
                        if (it.isEmptyList) {
                            Box(modifier = Modifier.fillMaxSize()) {
                                NoDataMessage(
                                    modifier = Modifier.align(alignment = Alignment.Center),
                                    icon = R.drawable.ic_questionnaire
                                )
                            }
                        }
                        // button to upload answers
                        StandardEnablingExchangeButton(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 16.dp),
                            text = stringResource(id = R.string.confirm),
                            isEnabled = viewModel.expectingAnswersCount.value == viewModel.inputtedAnswersCount.value,
                            isExchange = viewModel.answersState.value.isLoading
                        ) {
                            viewModel.uploadAnswers()
                        }
                    }
                }
                // content of uploading answers
                viewModel.answersState.value.let {
                    // uploading answers
                    if (it.isLoading) {
                        Logo(
                            modifier = Modifier.align(Alignment.Center),
                            colors = MainCardColors,
                            isAnimate = true,
                            text = stringResource(id = R.string.loading),
                            textColor = textColor()
                        )
                    }
                    // successful upload
                    if (it.result) setShowOkDialog(true)
                    // errors during uploading answers
                    if (it.error.isNotBlank()) {
                        uploadingErrors = when (it.error) {
                            CommonKeys.noNetwork -> stringResource(id = R.string.no_internet_connection)
                            CommonKeys.noProduct -> stringResource(id = R.string.no_product)
                            else -> it.error
                        }
                        setShowErrorsDialog(true)
                    }
                }
            }
        }

        // dialog on successful uploading
        AnimatedVisibility(
            visible = showOkDialog,
            enter = slideInVertically(initialOffsetY = { it }),
            exit = slideOutVertically(targetOffsetY = { it })
        ) {
            OkDialog(
                showDialog = showOkDialog,
                setShowDialog = setShowOkDialog,
                text = stringResource(id = R.string.thanks_for_survey),
                onClick = { navController.popBackStack() }
            )
        }
        // dialog uploading error
        AnimatedVisibility(
            visible = showErrorsDialog,
            enter = slideInVertically(initialOffsetY = { it }),
            exit = slideOutVertically(targetOffsetY = { it })
        ) {
            YesNoDialog(
                showDialog = showErrorsDialog,
                setShowDialog = setShowErrorsDialog,
                text = "${stringResource(id = R.string.error)} $uploadingErrors\n${stringResource(id = R.string.repeat)}",
                onClickYes = { viewModel.uploadAnswers() },
                onClickNo = { navController.popBackStack() }
            )
        }
    }
}