package com.the.club.ui.presentation.priceCheck

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.util.Log
import android.view.ViewGroup
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIos
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.navigation.NavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.common.util.concurrent.ListenableFuture
import kotlinx.coroutines.launch
import com.the.club.R
import com.the.club.common.BarCodeAnalyser
import com.the.club.common.CommonKeys.noNetwork
import com.the.club.common.CommonKeys.noProduct
import com.the.club.common.CommonKeys.noShop
import com.the.club.common.isPermanentlyDenied
import com.the.club.data.remote.shops.dto.ProductPrice
import com.the.club.domain.model.shops.Shop
import com.the.club.ui.commonComponents.*
import com.the.club.ui.presentation.priceCheck.components.*
import com.the.club.ui.theme.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

@ExperimentalMaterialApi
@ExperimentalPermissionsApi
@Composable
fun PriceCheckScreen(navController: NavController) {
    val viewModel = hiltViewModel<PriceCheckViewModel>()
    val context = LocalContext.current
    BackHandler(onBack = navController::popBackStack)
    val (showGPSDialog, setShowGPSDialog) = remember { mutableStateOf(false) }
    val (showNoPermissionDialog, setShowNoPermissionDialog) = remember { mutableStateOf(false) }
    val (showNearestShopDialog, setShowNearestShopDialog) = remember { mutableStateOf(false) }
    val (showErrorsDialog, setShowErrorsDialog) = remember { mutableStateOf(false) }
    var errorText by remember { mutableStateOf("") }
    var isScanPrice = false
    var noPermissionMessage = ""
    var waitOneShop by remember { mutableStateOf(true) }
    var hasGPS by remember { mutableStateOf(false) }
    var cameraPermission by remember { mutableStateOf(false) }
    val lifecycleOwner = LocalLifecycleOwner.current
    val permissionsState = rememberMultiplePermissionsState(
        listOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.CAMERA,
        )
    )
    var theShopDetected by remember { mutableStateOf<Shop?>(null) }
    var preview by remember { mutableStateOf<Preview?>(null) }
    var isScanBarcode by remember { mutableStateOf<Boolean?>(null) }
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp
    val lineLength = (configuration.screenWidthDp - 160).toFloat()
    val bottomSheetScaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = rememberBottomSheetState(
            initialValue = BottomSheetValue.Collapsed
        )
    )
    DisposableEffect(
        key1 = lifecycleOwner,
        effect = {
            val observer = LifecycleEventObserver { _, event ->
                if (event == Lifecycle.Event.ON_START) {
                    permissionsState.launchMultiplePermissionRequest()
                }
            }
            lifecycleOwner.lifecycle.addObserver(observer)

            onDispose {
                lifecycleOwner.lifecycle.removeObserver(observer)
            }
        }
    )
    OnLifecycleEvent { _, event ->
        when (event) {
            Lifecycle.Event.ON_RESUME -> {
                if (viewModel.canGetLocation()) {
                    hasGPS = true

                } else setShowGPSDialog(true)
            }
            else -> {}
        }
    }
    val coroutineScope = rememberCoroutineScope()
    var dialogShop = Shop("", null, -1, null, -1, "", 0, 0)
    var dialogShops: ArrayList<Shop> = ArrayList()
    dialogShops.add(dialogShop)
    val aPrice = ProductPrice("", 0)
    var dialogPrice by remember { mutableStateOf(aPrice) }
    val thePrice = remember { mutableStateOf(0L) }
    var isPriceDialog by remember { mutableStateOf(true) }
    if (hasGPS) {
        AskPermissions(lifecycleOwner = lifecycleOwner, permissionsState = permissionsState)
        permissionsState.permissions.forEach { permission ->
            when (permission.permission) {
                Manifest.permission.ACCESS_FINE_LOCATION -> {
                    when {
                        permission.hasPermission -> {
                            if (theShopDetected == null && waitOneShop) {
                                viewModel.getDeviceLocation()
                            }
                        }
                        permission.shouldShowRationale -> {
                            noPermissionMessage = stringResource(id = R.string.location_disabled)
                            setShowNoPermissionDialog(true)
                        }
                        permission.isPermanentlyDenied() -> {
                            noPermissionMessage = stringResource(id = R.string.location_disabled)
                            setShowNoPermissionDialog(true)
                        }
                    }
                }
                Manifest.permission.CAMERA -> {
                    when {
                        permission.hasPermission -> cameraPermission = true
                        permission.shouldShowRationale -> {
                            noPermissionMessage = stringResource(id = R.string.camera_disabled)
                            setShowNoPermissionDialog(true)
                        }
                        permission.isPermanentlyDenied() -> {
                            noPermissionMessage = stringResource(id = R.string.camera_disabled)
                            setShowNoPermissionDialog(true)
                        }
                    }
                }
            }
        }
    }
    if (!viewModel.isLocationDetected) {
        isScanBarcode = false
    }
    // content
    BottomSheetScaffold(
        scaffoldState = bottomSheetScaffoldState,
        sheetShape = RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp),
        sheetContent = {
            if (isPriceDialog) {
                ProductPriceDialog(
                    shop = dialogShop,
                    productPrice = dialogPrice,
                    onDrag = {
                        isScanBarcode = true
                    }
                )
            } else ShopsDialog(
                shops = dialogShops,
                onClickShop = {
                    coroutineScope.launch { bottomSheetScaffoldState.bottomSheetState.collapse() }
                    theShopDetected = it
                    viewModel.dropShopsValue()
                    isScanBarcode = true
                }, onClickCancel = {
                    coroutineScope.launch { bottomSheetScaffoldState.bottomSheetState.collapse() }
                    viewModel.dropShopsValue()
                    isScanBarcode = false
                })
        },
        sheetPeekHeight = 0.dp,
    ) {
        Surface(color = MaterialTheme.colors.background) {
            Column(modifier = Modifier
                .fillMaxSize()
                .clickable {
                    isScanBarcode = true
                    coroutineScope.launch {
                        if (bottomSheetScaffoldState.bottomSheetState.isExpanded) {
                            bottomSheetScaffoldState.bottomSheetState.collapse()
                        }
                    }
                }) {
                // toolbar
                MainToolbar(
                    homeIcon = Icons.Filled.ArrowBackIos,
                    onClickHome = { navController.popBackStack() },
                    title = stringResource(id = R.string.price_check_title),
                    menuIcon = rememberVectorPainter(Icons.Outlined.LocationOn),
                    onClickIcon = {
                        theShopDetected = null
                        waitOneShop = true
                        viewModel.dropPriceValue()
                    }
                )
                Box(modifier = Modifier.fillMaxSize()) {
                    if (cameraPermission && hasGPS) {
                        AndroidView(
                            factory = { AndroidViewContext ->
                                PreviewView(AndroidViewContext).apply {
                                    this.scaleType = PreviewView.ScaleType.FILL_CENTER
                                    layoutParams = ViewGroup.LayoutParams(
                                        ViewGroup.LayoutParams.MATCH_PARENT,
                                        ViewGroup.LayoutParams.MATCH_PARENT,
                                    )
                                    implementationMode = PreviewView.ImplementationMode.COMPATIBLE
                                }

                            },
                            modifier = Modifier.fillMaxSize(),
                            update = { previewView ->
                                val cameraSelector: CameraSelector = CameraSelector.Builder()
                                    .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                                    .build()
                                val cameraExecutor: ExecutorService = Executors.newSingleThreadExecutor()
                                val cameraProviderFuture: ListenableFuture<ProcessCameraProvider> =
                                    ProcessCameraProvider.getInstance(context)

                                cameraProviderFuture.addListener({
                                    preview = Preview.Builder().build().also { thePreview ->
                                        if (isScanBarcode != null) thePreview.setSurfaceProvider(previewView.surfaceProvider)
                                    }
                                    val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()
                                    val barcodeAnalyser = BarCodeAnalyser { barcodes ->
                                        barcodes.forEach { barcode ->
                                            barcode.rawValue?.let { barcodeValue ->
                                                isScanBarcode?.let { ready2scanBarcode ->
                                                    isScanBarcode = null
                                                    if (ready2scanBarcode) {
                                                        viewModel.dropPriceValue()
                                                        viewModel.getProductPrice(
                                                            theShopId = theShopDetected!!.id,
                                                            barcode = barcodeValue
                                                        )
                                                    } else {
                                                        coroutineScope.launch { bottomSheetScaffoldState.bottomSheetState.collapse() }
                                                        viewModel.getShopByQr(qr = barcodeValue)
                                                    }
                                                }
                                            }
                                        }
                                    }
                                    val imageAnalysis: ImageAnalysis = ImageAnalysis.Builder()
                                        .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                                        .build()
                                        .also { analysis -> analysis.setAnalyzer(cameraExecutor, barcodeAnalyser) }

                                    try {
                                        cameraProvider.unbindAll()
                                        cameraProvider.bindToLifecycle(
                                            lifecycleOwner,
                                            cameraSelector,
                                            preview,
                                            imageAnalysis
                                        )
                                    } catch (e: Exception) {
                                        Log.d("TAG", "CameraPreview: ${e.localizedMessage}")
                                    }
                                }, ContextCompat.getMainExecutor(context))
                            }
                        )
                        isScanBarcode?.let {
                            if (it) ScannerBarcodeShape(
                                modifier = Modifier
                                    .width(screenWidth.dp)
                                    .height((screenWidth / 1.5).dp)
                                    .padding(all = 48.dp)
                                    .align(alignment = Alignment.Center), lineLength = lineLength
                            )
                            else ScannerQrShape(
                                modifier = Modifier
                                    .size(screenWidth.dp)
                                    .padding(all = 48.dp)
                                    .align(alignment = Alignment.Center), lineLength = lineLength
                            )
                        }
                        //}
                    }
                    // observe state of shop value
                    viewModel.shopState.value.let {
                        if (it.isLoading) {
                            Logo(
                                modifier = Modifier.align(alignment = Alignment.Center),
                                colors = MainCardColors,
                                isAnimate = true,
                                text = stringResource(id = R.string.loading),
                                textColor = textColor()
                            )
                        }
                        if (it.shop != null) {
                            setShowNearestShopDialog(true)
                        }
                        if (it.error.isNotEmpty()) {
                            if (it.error == noShop) {
                                hasGPS = true
                                isScanBarcode = false
                            } else {
                                errorText = when (it.error) {
                                    noNetwork -> stringResource(id = R.string.no_internet_connection)
                                    noProduct -> stringResource(id = R.string.no_product)
                                    else -> it.error
                                }
                                isScanPrice = false
                            }
                        }
                    }
                    // observe state of product price
                    viewModel.priceState.value.let {
                        if (it.isLoading) {
                            Logo(
                                modifier = Modifier.align(alignment = Alignment.Center),
                                colors = MainCardColors,
                                isAnimate = true,
                                text = stringResource(id = R.string.loading),
                                textColor = textColor()
                            )
                        }
                        it.productPrice?.let { price ->
                            dialogPrice = price
                            thePrice.value = price.price
                            isPriceDialog = true
                            coroutineScope.launch {
                                if (bottomSheetScaffoldState.bottomSheetState.isCollapsed) {
                                    bottomSheetScaffoldState.bottomSheetState.expand()
                                } else {
                                    bottomSheetScaffoldState.bottomSheetState.collapse()
                                }
                            }
                        }
                        if (it.error.isNotEmpty()) {
                            errorText = when (it.error) {
                                noNetwork -> stringResource(id = R.string.no_internet_connection)
                                noProduct -> stringResource(id = R.string.no_product)
                                else -> it.error
                            }
                            isScanPrice = true
                        }
                    }
                    // observe state of shops list
                    viewModel.shopsState.value.let {
                        if (it.isLoading) {
                            Logo(
                                modifier = Modifier.align(alignment = Alignment.Center),
                                colors = MainCardColors,
                                isAnimate = true,
                                text = stringResource(id = R.string.loading),
                                textColor = textColor()
                            )
                        }
                        if (it.shops.isNotEmpty()) {
                            dialogShops = ArrayList()
                            for (item in it.shops) {
                                dialogShops.add(item)
                            }
                            isPriceDialog = false
                            coroutineScope.launch {
                                if (bottomSheetScaffoldState.bottomSheetState.isCollapsed) {
                                    bottomSheetScaffoldState.bottomSheetState.expand()
                                } else {
                                    bottomSheetScaffoldState.bottomSheetState.collapse()
                                }
                            }
                        }
                        if (it.error.isNotEmpty()) {
                            errorText = when (it.error) {
                                noNetwork -> stringResource(id = R.string.no_internet_connection)
                                noProduct -> stringResource(id = R.string.no_product)
                                else -> it.error
                            }
                            isScanPrice = false
                        }
                    }

                    if (errorText.isNotEmpty()) {
                        setShowErrorsDialog(true)
                    }
                    // the shop detected
                    theShopDetected?.let { theShop ->
                        dialogShop = theShop
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .align(alignment = Alignment.TopCenter)
                                .background(color = backgroundColor(), shape = RoundedCornerShape(bottomStart = 10.dp, bottomEnd = 10.dp))
                        ) {
                            Text(
                                text = theShop.name,
                                style = Typography.body1,
                                color = textColor(),
                                modifier = Modifier
                                    .padding(all = 16.dp)
                                    .align(alignment = Alignment.Center)
                            )
                        }
                    }
                    // message about what we are scanning
                    isScanBarcode?.let { ready2scanBarcode ->
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .align(alignment = Alignment.BottomCenter)
                                .padding(all = 48.dp)
                                .background(color = backgroundColor(), shape = Shapes.large)
                        ) {
                            Text(
                                text =
                                if (ready2scanBarcode)
                                    stringResource(id = R.string.scan_barcode)
                                else
                                    stringResource(id = R.string.scan_qr),
                                style = Typography.body1,
                                color = textColor(),
                                textAlign = TextAlign.Center,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp, vertical = 8.dp)
                                    .align(alignment = Alignment.Center)
                            )
                        }
                    }
                    // error on no location
                    if (!viewModel.isLocationDetected) {
                        hasGPS = true
                        isScanBarcode = false
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
                    onClickYes = {
                        context.startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
                    },
                    onClickNo = {
                        hasGPS = true
                        isScanBarcode = false
                    }
                )
            }
            // dialog if permissions denied
            AnimatedVisibility(
                visible = showNoPermissionDialog,
                enter = slideInVertically(initialOffsetY = { it }),
                exit = slideOutVertically(targetOffsetY = { it })
            ) {
                YesNoDialog(
                    showDialog = showNoPermissionDialog,
                    setShowDialog = setShowNoPermissionDialog,
                    text = noPermissionMessage,
                    onClickYes = {
                        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                        val uri: Uri = Uri.fromParts("package", context.packageName, null)
                        intent.data = uri
                        context.startActivity(intent)
                    },
                    onClickNo = { navController.popBackStack() }
                )
            }
            // dialog with nearest shop
            AnimatedVisibility(
                visible = showNearestShopDialog,
                enter = slideInVertically(initialOffsetY = { it }),
                exit = slideOutVertically(targetOffsetY = { it })
            ) {
                NearestShopDialog(
                    showDialog = showNearestShopDialog,
                    setShowDialog = setShowNearestShopDialog,
                    shopName = viewModel.shopState.value.shop?.name ?: "",
                    shopAddress = viewModel.shopState.value.shop?.address ?: "",
                    onClickYes = {
                        coroutineScope.launch { bottomSheetScaffoldState.bottomSheetState.collapse() }
                        viewModel.isLocationDetected = true
                        theShopDetected = viewModel.shopState.value.shop!!
                        viewModel.dropShopValue()
                        setShowNearestShopDialog(false)
                        isScanBarcode = true
                    },
                    onClickNo = {
                        coroutineScope.launch { bottomSheetScaffoldState.bottomSheetState.collapse() }
                        waitOneShop = false
                        setShowNearestShopDialog(false)
                        viewModel.dropShopValue()
                        viewModel.getNearestShops()
                    }
                )
            }
            // dialog with errors
            AnimatedVisibility(
                visible = showErrorsDialog,
                enter = slideInVertically(initialOffsetY = { it }),
                exit = slideOutVertically(targetOffsetY = { it })
            ) {
                OkDialog(
                    showDialog = showErrorsDialog,
                    setShowDialog = setShowErrorsDialog,
                    text = errorText,
                    onClick = {
                        if (isScanPrice) {
                            errorText = ""
                            viewModel.dropErrors()
                            setShowErrorsDialog(false)
                            isScanBarcode = true
                        } else {
                            navController.popBackStack()
                        }

                    }
                )
            }
        }
    }
}