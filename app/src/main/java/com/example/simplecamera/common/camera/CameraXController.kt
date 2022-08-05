package com.example.simplecamera.common.camera

import android.content.Context
import android.view.Surface
import android.widget.Toast
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import com.google.common.util.concurrent.ListenableFuture
import java.io.File

class CameraXController(
    private val cameraPreview: PreviewView,
    private val context: Context,
    private val lifecycleOwner: LifecycleOwner
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


    // TODO(refactor this file handling)
    override fun takePhoto() {
        val outPutFileDirName =
            context.filesDir.absolutePath + File.separator + "my_photos" + File.separator
        val dir = File(outPutFileDirName)
        dir.mkdir()
        val outPutFileName = outPutFileDirName + System.currentTimeMillis().toString() + ".jpg"

        val outPutFile = File(outPutFileName)
        val outputFileOptions = ImageCapture.OutputFileOptions
            .Builder(
                outPutFile
            )
            .build()

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