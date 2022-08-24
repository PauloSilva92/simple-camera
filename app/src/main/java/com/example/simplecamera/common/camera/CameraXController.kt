package com.example.simplecamera.common.camera

import android.Manifest
import android.net.Uri
import android.view.View
import android.widget.Toast
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import com.example.simplecamera.common.permission.PermissionRequester
import com.google.common.util.concurrent.ListenableFuture
import javax.inject.Inject

class CameraXController @Inject constructor(
    private val activity: FragmentActivity,
    private val cameraFileHandler: CameraFileHandler,
    private val permissionRequester: PermissionRequester
) : CameraController {

    private lateinit var cameraProviderFuture: ListenableFuture<ProcessCameraProvider>
    private lateinit var cameraProvider: ProcessCameraProvider
    private lateinit var camera: Camera
    private lateinit var imageCapture: ImageCapture
    private lateinit var cameraPreview: PreviewView

    private var torchEnabled = false

    private val cameraSelector = CameraSelector.Builder().build()
    private val preview = Preview.Builder().build()


    override fun <T : View> start(previewView: T) {
        if (previewView !is PreviewView) {
            throw PreviewTypeMismatch("Preview type is not a PreviewView")
        }

        cameraPreview = previewView
        permissionRequester.request(
            Manifest.permission.CAMERA,
            {
                imageCapture = ImageCapture.Builder().build()
                cameraProviderFuture = ProcessCameraProvider.getInstance(activity)
                configCameraPreview()
            },
            {
                Toast.makeText(activity, "Didn't work =/", Toast.LENGTH_SHORT).show()
            }
        )
    }

    override fun stop() {
        cameraProvider.unbindAll()
    }


    override fun takePhoto(
        onSuccess: (photoUri: Uri?) -> Unit,
        onError: (exception: Throwable) -> Unit
    ) {

        val outputFileOptions = cameraFileHandler.createOutputFileOptions()

        imageCapture.takePicture(
            outputFileOptions,
            ContextCompat.getMainExecutor(activity),
            object : ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                    onSuccess(outputFileResults.savedUri)
                }

                override fun onError(exception: ImageCaptureException) {
                    onError(exception)
                }

            }

        )
    }

    override fun toggleTorch(toggleCallback: (flashState: FlashState) -> Unit) {
        torchEnabled = !torchEnabled
        camera.cameraControl.enableTorch(torchEnabled)

        val flashState = if (torchEnabled) {
            FlashState.ON
        } else {
            FlashState.OFF
        }

        toggleCallback(flashState)
    }


    private fun configCameraPreview() {
        cameraProviderFuture.addListener({
            cameraProvider = cameraProviderFuture.get()
            bindPreview(cameraProvider)
        }, ContextCompat.getMainExecutor(activity))
    }

    private fun bindPreview(cameraProvider: ProcessCameraProvider) {
        preview.setSurfaceProvider(cameraPreview.surfaceProvider)
        camera =
            cameraProvider.bindToLifecycle(activity, cameraSelector, preview, imageCapture)
    }

}

class PreviewTypeMismatch(exception: String) : RuntimeException(exception)