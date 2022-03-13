package com.the.club.ui.presentation.profile

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIos
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import kotlinx.coroutines.launch
import com.the.club.R
import com.the.club.common.CommonKeys.noNetwork
import com.the.club.common.CommonKeys.profileBackKey
import com.the.club.common.ktx.*
import com.the.club.data.remote.profile.dto.ProfileDto
import com.the.club.domain.model.Profile
import com.the.club.ui.commonComponents.MainToolbar
import com.the.club.ui.commonComponents.StandardEnablingExchangeButton
import com.the.club.ui.commonComponents.onBack
import com.the.club.ui.presentation.profile.components.GenderChoiceDialog
import com.the.club.ui.theme.*

@ExperimentalMaterialApi
@ExperimentalComposeUiApi
@Composable
fun EditProfileScreen(navController: NavController, profile: ProfileDto) {
    val viewModel = hiltViewModel<ProfileViewModel>()
    val keyboardController = LocalSoftwareKeyboardController.current
    val surname = remember { mutableStateOf(profile.lastName) }
    val surnameError = remember { mutableStateOf("") }
    val name = remember { mutableStateOf(profile.firstName) }
    val nameError = remember { mutableStateOf("") }
    val theGender = profile.gender ?: "male"
    val gender = remember { mutableStateOf(theGender) }
    val profileEmail = if (profile.email != null) profile.email!! else ""
    val email = remember { mutableStateOf(profileEmail) }
    val emailError = remember { mutableStateOf("") }
    val surnameRequired = stringResource(id = R.string.surname_required)
    val nameRequired = stringResource(id = R.string.name_required)
    val nameRule = stringResource(id = R.string.name_rule)
    val mailRule = stringResource(id = R.string.wrong_mail)
    val coroutineScope = rememberCoroutineScope()
    val bottomSheetScaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = BottomSheetState(BottomSheetValue.Collapsed)
    )
    BottomSheetScaffold(
        scaffoldState = bottomSheetScaffoldState,
        sheetShape = RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp),
        sheetContent = {
            GenderChoiceDialog(gender = gender.value, onClick = {
                gender.value = it
                coroutineScope.launch {
                    bottomSheetScaffoldState.bottomSheetState.collapse()
                }
            })
        },
        sheetPeekHeight = 0.dp
    ) {
        // screen content
        Surface(color = MaterialTheme.colors.background) {
            Column {
                // toolbar
                MainToolbar(
                    homeIcon = Icons.Filled.ArrowBackIos,
                    onClickHome = { navController.popBackStack() },
                    title = stringResource(id = R.string.edit_profile),
                    menuIcon = null,
                    onClickIcon = { }
                )
                // content
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 24.dp, vertical = 36.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1F)
                            .verticalScroll(rememberScrollState())
                    ) {
                        // surname
                        OutlinedTextField(
                            value = surname.value ?: "",
                            onValueChange = {
                                surname.value = it
                                profile.lastName = it
                                surname.value?.let { thisVal ->
                                    when {
                                        thisVal.isEmpty() -> surnameError.value = surnameRequired
                                        !thisVal.isOnlyLetters() -> surnameError.value = nameRule
                                        else -> surnameError.value = ""
                                    }
                                }
                            },
                            textStyle = Typography.h3,
                            label = {
                                surname.value?.let { thisValue ->
                                    if (thisValue.isNotEmpty()) {
                                        Text(
                                            text = stringResource(id = R.string.surname),
                                            style = Typography.body2,
                                            color = gray
                                        )
                                    }
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
                                    surname.value?.let { thisValue ->
                                        when {
                                            thisValue.isEmpty() -> {
                                                surnameError.value = surnameRequired
                                            }
                                            !thisValue.isOnlyLetters() -> {
                                                surnameError.value = nameRule
                                            }
                                            else -> {
                                                keyboardController?.hide()
                                                profile.lastName = surname.value
                                            }
                                        }
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
                            value = name.value ?: "",
                            onValueChange = {
                                name.value = it
                                profile.firstName = it
                                name.value?.let { thisValue ->
                                    when {
                                        thisValue.isEmpty() -> nameError.value = nameRequired
                                        !thisValue.isOnlyLetters() -> nameError.value = nameRule
                                        else -> nameError.value = ""
                                    }
                                }
                            },
                            textStyle = Typography.h3,
                            label = {
                                name.value?.let { thisValue ->
                                    if (thisValue.isNotEmpty()) {
                                        Text(
                                            text = stringResource(id = R.string.name),
                                            style = Typography.body2,
                                            color = gray
                                        )
                                    }
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
                                    name.value?.let { thisValue ->
                                        when {
                                            thisValue.isEmpty() -> {
                                                nameError.value = nameRequired
                                            }
                                            !thisValue.isOnlyLetters() -> {
                                                nameError.value = nameRule
                                            }
                                            else -> {
                                                keyboardController?.hide()
                                                profile.firstName = name.value
                                            }
                                        }
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
                                profile.email = it
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
                                        !email.value.isEmailValid() -> {
                                            emailError.value = mailRule
                                        }
                                        else -> {
                                            keyboardController?.hide()
                                            profile.email = email.value
                                        }
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
                            text = if (viewModel.profileState.value.error == noNetwork)
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
                    StandardEnablingExchangeButton(
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.CenterHorizontally),
                        text =
                        if (viewModel.profileState.value.isLoading) stringResource(id = R.string.loading)
                        else stringResource(id = R.string.confirm),
                        isEnabled = name.value != null && name.value!!.isNotEmpty() && name.value!!.isOnlyLetters()
                                && surname.value != null && surname.value!!.isNotEmpty() && surname.value!!.isOnlyLetters()
                                && email.value.isNotEmpty() && email.value.isEmailValid(),
                        isExchange = viewModel.profileState.value.isLoading
                    ) {
                        val newProfile = Profile(
                            birthday = profile.birthday?.toString(pattern = FULL_DATE) ?: minBirthdayFull(),
                            city_id = profile.city?.id ?: 1,
                            email = profile.email!!,
                            first_name = name.value ?: "",
                            gender = gender.value,
                            last_name = surname.value ?: "",
                        )
                        viewModel.updateProfile(newProfile)
                    }
                }
            }
            viewModel.profileState.value.profile?.let {
                LaunchedEffect(it) {
                    onBack(navController, mapOf(profileBackKey to true))
                }
            }
        }
    }
}