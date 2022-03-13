package com.the.club.ui.presentation.auth.cardCreation.components

import android.util.Log
import android.view.ViewGroup
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionRequired
import com.google.accompanist.permissions.rememberPermissionState
import com.google.common.util.concurrent.ListenableFuture
import com.the.club.common.BarCodeAnalyser
import com.the.club.ui.presentation.priceCheck.components.ScannerQrShape
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

@ExperimentalPermissionsApi
@Composable
fun BarcodeScanner(
    showDialog: Boolean,
    isBarcode: Boolean,
    setShowDialog: (Boolean) -> Unit,
    onScanned: (String) -> Unit
){
    if (showDialog) {
        val cameraPermissionState = rememberPermissionState(android.Manifest.permission.CAMERA)
        var preview by remember { mutableStateOf<Preview?>(null) }
        val context = LocalContext.current
        val lifecycleOwner = LocalLifecycleOwner.current
        val configuration = LocalConfiguration.current
        val screenWidth = configuration.screenWidthDp
        val lineLength = (configuration.screenWidthDp - 160).toFloat()
        DisposableEffect(
            key1 = lifecycleOwner,
            effect = {
                val observer = LifecycleEventObserver { _, event ->
                    if (event == Lifecycle.Event.ON_START) {
                        cameraPermissionState.launchPermissionRequest()
                    }
                }
                lifecycleOwner.lifecycle.addObserver(observer)
                onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
            }
        )
        Box(modifier = Modifier.fillMaxSize()) {
            PermissionRequired(
                permissionState = cameraPermissionState,
                permissionNotGrantedContent = { },
                permissionNotAvailableContent = {
                    setShowDialog(false)
                }
            ) {
                if (cameraPermissionState.hasPermission) {
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
                                    thePreview.setSurfaceProvider(previewView.surfaceProvider)
                                }
                                val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()
                                val barcodeAnalyser = BarCodeAnalyser { barcodes ->
                                    barcodes.forEach { barcode ->
                                        barcode.rawValue?.let { barcodeValue ->
                                            onScanned(barcodeValue)
                                            setShowDialog(false)
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
                                    setShowDialog(false)
                                }
                            }, ContextCompat.getMainExecutor(context))
                        }
                    )
                    ScannerQrShape(
                        modifier = Modifier
                            .width(screenWidth.dp)
                            .height(if (isBarcode) (screenWidth/2).dp else screenWidth.dp)
                            .padding(all = 48.dp)
                            .align(alignment = Alignment.Center), lineLength = lineLength
                    )
                }
            }
        }
    }
}