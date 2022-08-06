package com.example.simplecamera.common.camera

import android.content.Context
import android.view.Surface
import android.widget.Toast
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import com.example.simplecamera.common.file.FileUtils
import com.google.common.util.concurrent.ListenableFuture

class CameraXController(
    private val cameraPreview: PreviewView,
    private val context: Context,
    private val lifecycleOwner: LifecycleOwner,
    private val fileUtils: FileUtils
) : CameraController {


    private lateinit var cameraProviderFuture: ListenableFuture<ProcessCameraProvider>
    private lateinit var cameraProvider: ProcessCameraProvider
    private lateinit var camera: Camera
    private lateinit var imageCapture: ImageCapture

    private var torchEnabled = false

    private val cameraSelector = CameraSelector.Builder().build()
    private val preview = Preview.Builder().build()


    override fun start() {

        imageCapture = ImageCapture.Builder()
            .setTargetRotation(Surface.ROTATION_90)
            .build()

        cameraProviderFuture = ProcessCameraProvider.getInstance(context)

        configCameraPreview()
    }

    override fun stop() {

    }


    override fun takePhoto() {

        val outputFileOptions = fileUtils.createOutputFileOptions()

        imageCapture.takePicture(
            outputFileOptions,
            ContextCompat.getMainExecutor(context),
            object : ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                    Toast.makeText(context, "Image saved", Toast.LENGTH_SHORT).show()
                }

                override fun onError(exception: ImageCaptureException) {
                    exception.printStackTrace()
                    Toast.makeText(context, "Image not saved", Toast.LENGTH_SHORT).show()
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
        }, ContextCompat.getMainExecutor(context))
    }

    private fun bindPreview(cameraProvider: ProcessCameraProvider) {
        preview.setSurfaceProvider(cameraPreview.surfaceProvider)
        camera =
            cameraProvider.bindToLifecycle(lifecycleOwner, cameraSelector, preview, imageCapture)
    }


}