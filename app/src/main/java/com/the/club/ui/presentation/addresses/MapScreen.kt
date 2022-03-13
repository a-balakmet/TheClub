package com.the.club.ui.presentation.addresses

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBackIos
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.outlined.NearMe
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.navigation.NavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.coroutines.launch
import com.the.club.R
import com.the.club.common.isPermanentlyDenied
import com.the.club.common.ktx.toBitmapDescriptor
import com.the.club.common.ktx.toLatLng
import com.the.club.data.remote.shops.dto.toShop
import com.the.club.domain.model.shops.Shop
import com.the.club.ui.commonComponents.*
import com.the.club.ui.presentation.addresses.components.*
import com.the.club.ui.theme.*

@ExperimentalMaterialApi
@ExperimentalPermissionsApi
@Composable
fun MapScreen(navController: NavController) {
    val viewModel = hiltViewModel<MapViewModel>()
    val context = LocalContext.current
    BackHandler(onBack = navController::popBackStack)
    // location
    val (showGPSDialog, setShowGPSDialog) = remember { mutableStateOf(false) }
    val (showNoLocationDialog, setShowNoLocationDialog) = remember { mutableStateOf(false) }
    var hasGPS by remember { mutableStateOf(false) }
    // permission
    val lifecycleOwner = LocalLifecycleOwner.current
    val permissionsState = rememberMultiplePermissionsState(
        listOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
        )
    )
    DisposableEffect(
        key1 = lifecycleOwner,
        effect = {
            val observer = LifecycleEventObserver { _, event ->
                if (event == Lifecycle.Event.ON_START) {
                    permissionsState.launchMultiplePermissionRequest()
                    if (viewModel.canGetLocation()) {
                        hasGPS = true

                    } else setShowGPSDialog(true)
                }
            }
            lifecycleOwner.lifecycle.addObserver(observer)

            onDispose {
                lifecycleOwner.lifecycle.removeObserver(observer)
            }
        }
    )
    if (hasGPS) {
        AskPermissions(lifecycleOwner = lifecycleOwner, permissionsState = permissionsState)
        permissionsState.permissions.forEach { permission ->
            when (permission.permission) {
                Manifest.permission.ACCESS_COARSE_LOCATION -> {
                    when {
                        permission.hasPermission -> {
                            setShowNoLocationDialog(false)
                            viewModel.hasPermission = true
                            viewModel.loadCities()
                        }
                        permission.shouldShowRationale -> setShowNoLocationDialog(true)
                        permission.isPermanentlyDenied() -> setShowNoLocationDialog(true)
                    }
                }
            }
        }
    }
    val coroutineScope = rememberCoroutineScope()
    // map
    var showShops by remember { mutableStateOf(false) }
    var isMapLoaded by remember { mutableStateOf(false) }
    val mapView = rememberMapViewWithLifeCycle()
    // bottom dialog
    var isShowShop by remember { mutableStateOf(false) }
    val bottomSheetScaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = BottomSheetState(BottomSheetValue.Collapsed)
    )
    val emptyShop = Shop("", null, -1, null, -1, "", 0, 0)
    var clickedShop by remember { mutableStateOf(emptyShop) }
    // screen content
    BottomSheetScaffold(
        scaffoldState = bottomSheetScaffoldState,
        sheetShape = RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp),
        sheetContent = {
            if (isShowShop) {
                ShopBottomDialog(shop = clickedShop, onClick = { location ->
                    coroutineScope.launch {
                        bottomSheetScaffoldState.bottomSheetState.collapse()
                    }
                    viewModel.deviceLocation.value?.let { deviceLatLng ->
                        val intent = Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse(
                                "http://maps.google.com/maps?saddr="
                                        + deviceLatLng.latitude + "," + deviceLatLng.longitude + "&daddr="
                                        + location.latitude + "," + location.longitude
                            )
                        )
                        intent.setPackage("com.google.android.apps.maps")
                        context.startActivity(intent)
                    }
                })
            } else {
                CitiesBottomDialog(
                    citiesList = viewModel.cities,
                    onClickCancel = {
                        coroutineScope.launch {
                            bottomSheetScaffoldState.bottomSheetState.collapse()
                        }
                    },
                    onClickReady = { newLocation ->
                        viewModel.nearestCity.value = newLocation
                        coroutineScope.launch {
                            bottomSheetScaffoldState.bottomSheetState.collapse()
                            newLocation.geo?.let {
                                mapView.getMapAsync{
                                    it.animateCamera(CameraUpdateFactory.newLatLngZoom(newLocation.geo, 15f))
                                }
                            }
                        }
                    }
                )
            }
        },
        sheetPeekHeight = 0.dp
    ) {
        Surface(color = MaterialTheme.colors.background) {
            Column {
                // toolbar
                MainToolbar(
                    homeIcon = Icons.Filled.ArrowBackIos,
                    onClickHome = { navController.popBackStack() },
                    title = stringResource(id = R.string.shops_addresses_title),
                    menuIcon = null,
                    onClickIcon = { }
                )
                // content
                if (viewModel.hasPermission) {
                    Box(modifier = Modifier.fillMaxSize()) {
                        // map
                        if (viewModel.mapContentState.value.isFinished) {
                            viewModel.onMapLocation.let { deviceLocation ->
                               AndroidView({mapView}) { mapView ->
                                   coroutineScope.launch {
                                       mapView.getMapAsync { googleMap->
                                           isMapLoaded = true
                                           googleMap.mapType = 1
                                           googleMap.uiSettings.isZoomControlsEnabled = false
                                           googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(viewModel.initialCity.value, 15f))
                                           // markers on the map
                                           viewModel.shops.forEach { shop ->
                                               shop.geo?.let { latLng ->
                                                   if (latLng.latitude > 8 || latLng.longitude > 8) {
                                                       googleMap.addMarker(
                                                           MarkerOptions()
                                                               .icon((R.drawable.ic_shop_pin).toBitmapDescriptor(context))
                                                               .position(latLng)
                                                       )
                                                   }
                                               }
                                           }
                                           googleMap.setOnMarkerClickListener {
                                               isShowShop = true
                                               clickedShop = it.position.toShop(viewModel.shops)
                                               googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(it.position, 15f))
                                               coroutineScope.launch {
                                                   if (bottomSheetScaffoldState.bottomSheetState.isCollapsed) {
                                                       bottomSheetScaffoldState.bottomSheetState.expand()
                                                   } else {
                                                       bottomSheetScaffoldState.bottomSheetState.collapse()
                                                   }
                                               }
                                               true
                                           }
                                       }
                                   }
                               }
                                // on-map buttons
                                Column(
                                    modifier = Modifier
                                        .align(alignment = Alignment.CenterEnd)
                                        .padding(end = 8.dp)
                                ) {
                                    MapControlButton(icon = Icons.Filled.Add) {
                                        coroutineScope.launch {
                                            mapView.getMapAsync{
                                                it.animateCamera(CameraUpdateFactory.zoomIn())
                                            }
                                        }
                                    }
                                    Spacer(modifier = Modifier.size(width = 16.dp, height = 16.dp))
                                    MapControlButton(icon = Icons.Filled.Remove) {
                                        coroutineScope.launch {
                                            mapView.getMapAsync{
                                                it.animateCamera(CameraUpdateFactory.zoomOut())
                                            }
                                        }
                                    }
                                    Spacer(modifier = Modifier.size(width = 16.dp, height = 64.dp))
                                    MapControlButton(icon = Icons.Outlined.NearMe) {
                                        coroutineScope.launch {
                                            mapView.getMapAsync{
                                                it.animateCamera(CameraUpdateFactory.newLatLngZoom(deviceLocation.value.toLatLng(), 15f))
                                            }
                                        }
                                    }
                                }
                                if (!isMapLoaded) {
                                    Logo(
                                        modifier = Modifier.align(alignment = Alignment.Center),
                                        colors = MainCardColors,
                                        isAnimate = true,
                                        text = stringResource(id = R.string.loading),
                                        textColor = textColor()
                                    )
                                }
                                if (showShops) {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .background(color = backgroundColor())
                                    ) {
                                        viewModel.nearestCity.value.let { theCity ->
                                            viewModel.shops.filter { theShop ->
                                                theShop.cityId == theCity.id
                                            }.also { theShopsList ->
                                                LazyColumn(
                                                    modifier = Modifier
                                                        .padding(start = 24.dp, top = 96.dp, end = 24.dp, bottom = 16.dp)
                                                        .fillMaxSize(),
                                                    verticalArrangement = Arrangement.spacedBy(16.dp),
                                                ) {
                                                    items(theShopsList) { shopItem ->
                                                        ShopListItem(shop = shopItem, onClick = { shopLocation ->
                                                            shopLocation?.let { newLocation ->
                                                                viewModel.initialCity.value = newLocation
                                                                showShops = false
                                                            }
                                                        })
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        // subtitle menu
                        Card(
                            modifier = Modifier.padding(all = 24.dp),
                            elevation = 16.dp,
                            border = BorderStroke(width = 2.dp, color = pink.copy(alpha = 0.2F))
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(color = pink)
                                    .padding(horizontal = 16.dp, vertical = 16.dp)
                            ) {
                                Icon(
                                    painter =
                                    if (showShops) painterResource(id = R.drawable.ic_map_on)
                                    else painterResource(id = R.drawable.ic_shops_list),
                                    contentDescription = "switcher",
                                    tint = white,
                                    modifier = Modifier
                                        .align(alignment = Alignment.CenterVertically)
                                        .clickable {
                                            showShops = !showShops
                                        }
                                )
                                Spacer(modifier = Modifier.weight(1F))
                                viewModel.nearestCity.value.let { theCity ->
                                    Text(
                                        text = theCity.name,
                                        color = white,
                                        style = Typography.h2,
                                        modifier = Modifier
                                            .padding(end = 24.dp)
                                            .align(alignment = Alignment.CenterVertically)
                                            .clickable {
                                                isShowShop = false
                                                coroutineScope.launch {
                                                    if (bottomSheetScaffoldState.bottomSheetState.isCollapsed) {
                                                        bottomSheetScaffoldState.bottomSheetState.expand()
                                                    } else {
                                                        bottomSheetScaffoldState.bottomSheetState.collapse()
                                                    }
                                                }
                                            }
                                    )
                                }
                                Icon(
                                    imageVector = Icons.Filled.KeyboardArrowDown,
                                    contentDescription = "lister",
                                    tint = white,
                                    modifier = Modifier
                                        .align(alignment = Alignment.CenterVertically)
                                        .clickable {
                                            isShowShop = false
                                            coroutineScope.launch {
                                                if (bottomSheetScaffoldState.bottomSheetState.isCollapsed) {
                                                    bottomSheetScaffoldState.bottomSheetState.expand()
                                                } else {
                                                    bottomSheetScaffoldState.bottomSheetState.collapse()
                                                }
                                            }
                                        }
                                )
                            }
                        }
                        // loading icon
                        if (viewModel.mapContentState.value.isLoading) {
                            Logo(
                                modifier = Modifier.align(alignment = Alignment.Center),
                                colors = MainCardColors,
                                isAnimate = true,
                                text = stringResource(id = R.string.loading),
                                textColor = textColor()
                            )
                        }
                    }
                }
            }
            // ask gps dialog
            AnimatedVisibility(
                visible = showGPSDialog,
                enter = slideInVertically(initialOffsetY = { it }),
                exit = slideOutVertically(targetOffsetY = { it })
            ) {
                YesNoDialog(
                    showDialog = showGPSDialog,
                    setShowDialog = setShowGPSDialog,
                    text = stringResource(id = R.string.switch_gps),
                    onClickYes = { context.startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)) },
                    onClickNo = {
                        viewModel.hasPermission = true
                        viewModel.loadCities()
                    }
                )
            }
            // dialog if location permission denied
            AnimatedVisibility(
                visible = showNoLocationDialog,
                enter = slideInVertically(initialOffsetY = { it }),
                exit = slideOutVertically(targetOffsetY = { it })
            ) {
                YesNoDialog(
                    showDialog = showNoLocationDialog,
                    setShowDialog = setShowNoLocationDialog,
                    text = stringResource(id = R.string.location_disabled),
                    onClickYes = {
                        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                        val uri: Uri = Uri.fromParts("package", context.packageName, null)
                        intent.data = uri
                        context.startActivity(intent)
                    },
                    onClickNo = { navController.popBackStack() }
                )
            }
        }
    }
}