package com.example.simplecamera.common.camera

import android.Manifest
import android.widget.Toast
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import com.example.simplecamera.common.file.FileUtils
import com.example.simplecamera.common.permission.PermissionRequester
import com.google.common.util.concurrent.ListenableFuture

class CameraXController(
    private val cameraPreview: PreviewView,
    private val activity: FragmentActivity,
    private val fileUtils: FileUtils,
    private val permissionRequester: PermissionRequester
) : CameraController {

    private lateinit var cameraProviderFuture: ListenableFuture<ProcessCameraProvider>
    private lateinit var cameraProvider: ProcessCameraProvider
    private lateinit var camera: Camera
    private lateinit var imageCapture: ImageCapture

    private var torchEnabled = false

    private val cameraSelector = CameraSelector.Builder().build()
    private val preview = Preview.Builder().build()


    override fun start() {
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

    }


    override fun takePhoto() {

        val outputFileOptions = fileUtils.createOutputFileOptions()

        imageCapture.takePicture(
            outputFileOptions,
            ContextCompat.getMainExecutor(activity),
            object : ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                    Toast.makeText(activity, "Image saved", Toast.LENGTH_SHORT).show()
                }

                override fun onError(exception: ImageCaptureException) {
                    exception.printStackTrace()
                    Toast.makeText(activity, "Image not saved", Toast.LENGTH_SHORT).show()
                }

            }

        )
    }

    override fun toggleTorch() {
        torchEnabled = !torchEnabled
        camera.cameraControl.enableTorch(torchEnabled)
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