package com.the.club.ui.presentation.profile

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIos
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavController
import com.google.gson.Gson
import com.the.club.R
import com.the.club.common.CommonKeys.agreementLink
import com.the.club.common.CommonKeys.noNetwork
import com.the.club.common.CommonKeys.profileBackKey
import com.the.club.common.ktx.toFullDate
import com.the.club.common.ktx.toPhoneFormat
import com.the.club.data.remote.profile.dto.ProfileDto
import com.the.club.ui.commonComponents.*
import com.the.club.ui.navigation.Screen
import com.the.club.ui.theme.*
import java.util.*

@Composable
fun UserProfileScreen(navController: NavController, profile: ProfileDto?, savedStateHandle: SavedStateHandle?) {
    val context = LocalContext.current
    val viewModel = hiltViewModel<UserProfileViewModel>()
    var userProfile by remember { mutableStateOf(profile) }
    val months = stringArrayResource(id = R.array.months_relative)
    BackHandler(onBack = navController::popBackStack)
    if (savedStateHandle != null) {
        val isUpdateProfile by savedStateHandle.getLiveData<Boolean>(profileBackKey).observeAsState()
        LaunchedEffect(isUpdateProfile) {
            isUpdateProfile?.let {
                if (it) {
                    viewModel.getProfile()
                    savedStateHandle.remove<Boolean>(profileBackKey)
                }
            }
        }
    }
    Surface(color = MaterialTheme.colors.background) {
        Column {
            // toolbar
            MainToolbar(
                homeIcon = Icons.Filled.ArrowBackIos,
                onClickHome = { navController.popBackStack() },
                title = stringResource(id = R.string.my_profile_title),
                menuIcon = painterResource(id = R.drawable.ic_settings),
                onClickIcon = { navController.navigate(Screen.SettingsScreen.route + "/settings") }
            )
            // content
            Column(modifier = Modifier.fillMaxSize()) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 24.dp, top = 35.dp, end = 24.dp, bottom = 16.dp)
                        .weight(1F)
                        .verticalScroll(rememberScrollState())
                ) {
                    Text(
                        text = if (userProfile?.patronymic != null)
                            "${userProfile?.firstName ?: ""} ${userProfile?.patronymic ?: ""}\n${userProfile?.lastName ?: ""}"
                        else "${userProfile?.firstName ?: ""} ${userProfile?.lastName ?: ""}",
                        style = Typography.h2,
                        textAlign = TextAlign.Center,
                        color = textColor(),
                        modifier = Modifier
                            .align(alignment = Alignment.CenterHorizontally)
                    )
                    BoarderButton(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 32.dp, vertical = 16.dp),
                        text = stringResource(id = R.string.edit_profile)
                    ) {
                        val theProfile =
                            if (profile != null) Gson().toJson(userProfile)
                            else Gson().toJson(ProfileDto(
                                birthday = Date(),
                                city = null,
                                dateCreated = Date(),
                                email = null,
                                firstName = "",
                                gender = "male",
                                id = 0,
                                lastName = "",
                                patronymic = null,
                                phone = ""
                            ))
                        navController.navigate(Screen.EditProfileScreen.route + "/$theProfile")
                    }
                    Text(
                        text = stringResource(id = R.string.birth_date),
                        style = Typography.body1,
                        color = gray,
                        modifier = Modifier.padding(top = 48.dp, bottom = 4.dp)
                    )
                    Text(
                        text = userProfile?.birthday?.toFullDate(months = months) ?: "-",
                        style = Typography.h2,
                        color = textColor()
                    )
                    Divider(modifier = Modifier.padding(vertical = 16.dp))
                    Text(
                        text = stringResource(id = R.string.phone_number),
                        style = Typography.body1,
                        color = gray,
                        modifier = Modifier.padding(bottom = 4.dp)
                    )
                    Text(
                        text = "7${userProfile?.phone ?: ""}".toPhoneFormat(),
                        style = Typography.h2,
                        color = textColor()
                    )
                    Divider(modifier = Modifier.padding(vertical = 16.dp))
                    Text(
                        text = stringResource(id = R.string.email),
                        style = Typography.body1,
                        color = gray,
                        modifier = Modifier.padding(bottom = 4.dp)
                    )
                    Text(
                        text = if (userProfile?.email != null) userProfile?.email ?: "-" else "-",
                        style = Typography.h2,
                        color = textColor()
                    )
                    viewModel.profileState.value.let {
                        when {
                            it.isLoading -> {
                                Logo(
                                    modifier = Modifier
                                        .padding(top = 16.dp)
                                        .align(alignment = Alignment.CenterHorizontally),
                                    colors = MainCardColors,
                                    isAnimate = true,
                                    text = stringResource(id = R.string.loading),
                                    textColor = textColor()
                                )
                            }
                            it.profile != null -> {
                                userProfile = it.profile
                                //reloadProfile = true
                                Text(
                                    text = stringResource(id = R.string.profile_updated),
                                    style = Typography.body1,
                                    color = textColor(),
                                    modifier = Modifier
                                        .padding(top = 16.dp)
                                        .align(alignment = Alignment.CenterHorizontally)
                                )
                            }
                            it.error.isNotEmpty() -> {
                                Text(
                                    text =
                                    if (it.error == noNetwork) stringResource(id = R.string.no_internet_connection)
                                    else it.error,
                                    style = Typography.body1,
                                    color = red,
                                    modifier = Modifier
                                        .padding(top = 16.dp)
                                        .align(alignment = Alignment.CenterHorizontally)
                                )
                            }
                        }
                    }
                }
                Row(
                    modifier = Modifier
                        .padding(vertical = 16.dp)
                        .align(alignment = Alignment.CenterHorizontally)
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = rememberRipple(color = backgroundColor()),
                        ) {
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(agreementLink))
                            context.startActivity(intent)
                        }
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_edit),
                        contentDescription = "edit",
                        tint = pink,
                        modifier = Modifier
                            .size(24.dp)
                            .padding(end = 8.dp)
                    )
                    Text(
                        text = stringResource(id = R.string.read_agreement),
                        style = Typography.body1,
                        color = pink,
                        modifier = Modifier.align(alignment = Alignment.CenterVertically)
                    )
                }
            }
        }
    }
}