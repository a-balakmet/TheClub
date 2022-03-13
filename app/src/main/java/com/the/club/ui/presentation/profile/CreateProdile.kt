package com.the.club.ui.presentation.profile

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import kotlinx.coroutines.launch
import com.the.club.ui.commonComponents.StandardEnablingButton
import com.the.club.R
import com.the.club.common.CommonKeys
import com.the.club.common.ktx.*
import com.the.club.domain.model.Profile
import com.the.club.ui.commonComponents.BackHandler
import com.the.club.ui.commonComponents.DatePickerBottomDialog
import com.the.club.ui.commonComponents.OneButtonDialog
import com.the.club.ui.navigation.Screen
import com.the.club.ui.presentation.profile.components.GenderChoiceDialog
import com.the.club.ui.theme.*

@ExperimentalMaterialApi
@ExperimentalComposeUiApi
@Composable
fun CreateProfileScreen(navController: NavController){
    val viewModel = hiltViewModel<ProfileViewModel>()
    BackHandler(onBack = navController::popBackStack)
    var navigationListener by remember { mutableStateOf(false) }
    val keyboardController = LocalSoftwareKeyboardController.current
    val surname = remember { mutableStateOf("") }
    val surnameError = remember { mutableStateOf("") }
    val name = remember { mutableStateOf("") }
    val nameError = remember { mutableStateOf("") }
    val birthday = remember { mutableStateOf(minBirthday()) }
    var birthday2check = minBirthday()
    val birthdayError = remember { mutableStateOf("") }
    val months = stringArrayResource(id = R.array.months_relative)
    val gender = remember { mutableStateOf("male") }
    val email = remember { mutableStateOf("") }
    val emailError = remember { mutableStateOf("") }
    val surnameRequired = stringResource(id = R.string.surname_required)
    val nameRequired = stringResource(id = R.string.name_required)
    val nameRule = stringResource(id = R.string.name_rule)
    val birthdayRule = stringResource(id = R.string.rule_for_birthday)
    val mailRule = stringResource(id = R.string.wrong_mail)
    var isBirthday by remember { mutableStateOf(true) }
    val coroutineScope = rememberCoroutineScope()
    val bottomSheetScaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = BottomSheetState(BottomSheetValue.Collapsed)
    )
    BottomSheetScaffold(
        scaffoldState = bottomSheetScaffoldState,
        sheetShape = RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp),
        sheetContent = {
            if (isBirthday) {
                DatePickerBottomDialog(
                    dateValue = birthday.value,
                    iBirthday = true,
                    onClick = { date ->
                        coroutineScope.launch {
                            bottomSheetScaffoldState.bottomSheetState.collapse()
                        }
                        date?.let { theDate ->
                            birthday.value = theDate
                            birthday2check = theDate
                            if (!birthday2check.isEnoughYears()) birthdayError.value = birthdayRule
                            else birthdayError.value = ""
                        }
                    })
            } else {
                GenderChoiceDialog(gender = gender.value, onClick = {
                    gender.value = it
                    coroutineScope.launch {
                        bottomSheetScaffoldState.bottomSheetState.collapse()
                    }
                })
            }
        },
        sheetPeekHeight = 0.dp
    ) {
        Surface(color = MaterialTheme.colors.background) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp, vertical = 16.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    Text(
                        text = stringResource(id = R.string.fill_profile_title),
                        style = Typography.h1,
                        color = textColor()
                    )
                    Text(
                        text = stringResource(id = R.string.fill_profile_descr_title),
                        style = Typography.body1,
                        color = gray,
                        modifier = Modifier.padding(vertical = 24.dp)
                    )
                    // surname
                    OutlinedTextField(
                        value = surname.value,
                        onValueChange = {
                            surname.value = it
                            when {
                                surname.value.isEmpty() -> surnameError.value = surnameRequired
                                !surname.value.isOnlyLetters() -> surnameError.value = nameRule
                                else -> surnameError.value = ""
                            }
                        },
                        textStyle = Typography.h3,
                        label = {
                            if (surname.value.isNotEmpty()) {
                                Text(
                                    text = stringResource(id = R.string.surname),
                                    style = Typography.body2,
                                    color = gray
                                )
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            cursorColor = textColor(),
                            focusedBorderColor = gray,
                            focusedLabelColor = gray,
                            unfocusedBorderColor = gray,
                            unfocusedLabelColor = gray
                        ),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Done
                        ),
                        keyboardActions = KeyboardActions(
                            onDone = {
                                when {
                                    surname.value.isEmpty() -> surnameError.value = surnameRequired
                                    !surname.value.isOnlyLetters() -> surnameError.value = nameRule
                                    else -> keyboardController?.hide()
                                }
                            }
                        ),
                        placeholder = { Text(text = stringResource(id = R.string.surname)) },
                        shape = Shapes.large
                    )
                    // error in surname
                    Text(
                        text = surnameError.value,
                        style = Typography.body2,
                        color = yellow,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .padding(vertical = 4.dp)
                            .align(alignment = Alignment.CenterHorizontally)
                    )
                    // name
                    OutlinedTextField(
                        value = name.value,
                        onValueChange = {
                            name.value = it
                            when {
                                name.value.isEmpty() -> nameError.value = nameRequired
                                !name.value.isOnlyLetters() -> nameError.value = nameRule
                                else -> nameError.value = ""
                            }
                        },
                        textStyle = Typography.h3,
                        label = {
                            if (name.value.isNotEmpty()) {
                                Text(
                                    text = stringResource(id = R.string.name),
                                    style = Typography.body2,
                                    color = gray
                                )
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp),
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            cursorColor = textColor(),
                            focusedBorderColor = gray,
                            focusedLabelColor = gray,
                            unfocusedBorderColor = gray,
                            unfocusedLabelColor = gray,
                            disabledBorderColor = gray,
                            disabledLabelColor = gray
                        ),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Done
                        ),
                        keyboardActions = KeyboardActions(
                            onDone = {
                                when {
                                    name.value.isEmpty() -> nameError.value = nameRequired
                                    !name.value.isOnlyLetters() -> nameError.value = nameRule
                                    else -> keyboardController?.hide()
                                }
                            }
                        ),
                        placeholder = { Text(text = stringResource(id = R.string.name)) },
                        shape = Shapes.large
                    )
                    // error in name
                    Text(
                        text = nameError.value,
                        style = Typography.body2,
                        color = yellow,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .padding(vertical = 4.dp)
                            .align(alignment = Alignment.CenterHorizontally)
                    )
                    // birthday field
                    OutlinedTextField(
                        value = birthday.value.toFullDate(months = months),
                        onValueChange = {
                            //birthday2check = it
                            birthday.value = it.toFullDate(months = months)
                            //if (!it.isEnoughYears()) birthdayError.value = birthdayRule
                            //else birthdayError.value = ""
                        },
                        textStyle = Typography.h3,
                        label = {
                            Text(
                                text = stringResource(id = R.string.birth_date),
                                style = Typography.body2,
                                color = gray
                            )
                        },
                        enabled = false,
                        trailingIcon = {
                            Icon(
                                imageVector = Icons.Filled.KeyboardArrowDown,
                                contentDescription = "down",
                                tint = gray
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp)
                            .clickable {
                                isBirthday = true
                                keyboardController?.hide()
                                coroutineScope.launch {
                                    if (bottomSheetScaffoldState.bottomSheetState.isCollapsed) {
                                        bottomSheetScaffoldState.bottomSheetState.expand()
                                    } else {
                                        bottomSheetScaffoldState.bottomSheetState.collapse()
                                    }
                                }
                            },
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            cursorColor = textColor(),
                            focusedBorderColor = gray,
                            focusedLabelColor = gray,
                            unfocusedBorderColor = gray,
                            unfocusedLabelColor = gray,
                            disabledBorderColor = gray,
                            disabledLabelColor = gray,
                            disabledTextColor = textColor(),
                            disabledTrailingIconColor = gray
                        ),
                        shape = Shapes.large
                    )
                    // error in birthday
                    Text(
                        text = birthdayError.value,
                        style = Typography.body2,
                        color = yellow,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .padding(vertical = 4.dp)
                            .align(alignment = Alignment.CenterHorizontally)
                    )
                    // gender field
                    OutlinedTextField(
                        value = if (gender.value == "male") stringResource(id = R.string.male) else stringResource(id = R.string.female),
                        onValueChange = { },
                        textStyle = Typography.h3,
                        label = {
                            Text(
                                text = stringResource(id = R.string.sex),
                                style = Typography.body2,
                                color = gray
                            )
                        },
                        enabled = false,
                        trailingIcon = {
                            Icon(
                                imageVector = Icons.Filled.KeyboardArrowDown,
                                contentDescription = "down",
                                tint = gray
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 32.dp)
                            .clickable {
                                isBirthday = false
                                keyboardController?.hide()
                                coroutineScope.launch {
                                    if (bottomSheetScaffoldState.bottomSheetState.isCollapsed) {
                                        bottomSheetScaffoldState.bottomSheetState.expand()
                                    } else {
                                        bottomSheetScaffoldState.bottomSheetState.collapse()
                                    }
                                }
                            },
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            cursorColor = textColor(),
                            focusedBorderColor = gray,
                            focusedLabelColor = gray,
                            unfocusedBorderColor = gray,
                            unfocusedLabelColor = gray,
                            disabledBorderColor = gray,
                            disabledLabelColor = gray,
                            disabledTextColor = textColor(),
                            disabledTrailingIconColor = gray
                        ),
                        shape = Shapes.large
                    )
                    // email
                    OutlinedTextField(
                        value = email.value,
                        onValueChange = {
                            email.value = it
                            when {
                                !email.value.isEmailValid() -> emailError.value = mailRule
                                else -> emailError.value = ""
                            }
                        },
                        textStyle = Typography.h3,
                        label = {
                            if (email.value.isNotEmpty()) {
                                Text(
                                    text = "E-mail",
                                    style = Typography.body2,
                                    color = gray
                                )
                            }

                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            cursorColor = textColor(),
                            focusedBorderColor = gray,
                            focusedLabelColor = gray,
                            unfocusedBorderColor = gray,
                            unfocusedLabelColor = gray
                        ),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Email,
                            imeAction = ImeAction.Done
                        ),
                        keyboardActions = KeyboardActions(
                            onDone = {
                                when {
                                    !email.value.isEmailValid() -> emailError.value = mailRule
                                    else -> keyboardController?.hide()
                                }
                            }
                        ),
                        placeholder = { Text(text = "E-mail") },
                        shape = Shapes.large
                    )
                    // error in email
                    Text(
                        text = emailError.value,
                        style = Typography.body2,
                        color = yellow,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .padding(vertical = 4.dp)
                            .align(alignment = Alignment.CenterHorizontally)
                    )
                    // error during profile update
                    Text(
                        text = if (viewModel.profileState.value.error == CommonKeys.noNetwork)
                            stringResource(id = R.string.no_internet_connection)
                        else viewModel.profileState.value.error,
                        style = Typography.body1,
                        color = red,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .padding(vertical = 8.dp)
                            .align(alignment = Alignment.CenterHorizontally)

                    )

                }
                StandardEnablingButton(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(all = 24.dp)
                        .align(alignment = Alignment.BottomCenter),
                    text = stringResource(id = R.string.confirm),
                    isEnabled = name.value.isNotEmpty() && name.value.isOnlyLetters()
                            && surname.value.isNotEmpty() && surname.value.isOnlyLetters()
                            && birthday.value.isNotEmpty() && birthday2check.isEnoughYears()
                            && email.value.isNotEmpty() && email.value.isEmailValid()
                ) {
                    val profile = Profile(
                        birthday = birthday2check.toDate(DD_MM_YYYY)!!.toString(FULL_DATE),
                        city_id = 1,
                        email = email.value,
                        first_name = name.value,
                        gender = gender.value,
                        last_name = surname.value,
                    )
                    viewModel.updateProfile(profile)
                }
                ///
                viewModel.profileState.value.let {
                    if (it.isLoading) OneButtonDialog(text = stringResource(id = R.string.loading)) {}
                    if (it.profile != null) navigationListener = true
                    if (it.error.isNotEmpty()) {
                        val error = if (it.error == CommonKeys.noNetwork) stringResource(id = R.string.no_internet_connection) else it.error
                        OneButtonDialog(text = error) { navigationListener = true }
                    }
                }
            }
        }
    }
    // navigator
    LaunchedEffect(navigationListener) {
        if (navigationListener) {
            navController.navigate(Screen.MainScreen.route) { popUpTo(0) }
        }
    }
}