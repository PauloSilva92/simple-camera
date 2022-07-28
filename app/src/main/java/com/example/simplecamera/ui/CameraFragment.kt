package com.example.simplecamera.ui

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.Surface
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.AppCompatImageView
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.simplecamera.R
import com.google.common.util.concurrent.ListenableFuture
import java.io.File


class CameraFragment : Fragment(R.layout.fragment_camera) {

    private lateinit var cameraPreview: PreviewView
    private lateinit var takePictureButton: AppCompatImageView

    private lateinit var cameraProviderFuture: ListenableFuture<ProcessCameraProvider>
    private lateinit var cameraProvider: ProcessCameraProvider
    private lateinit var camera: Camera
    private lateinit var imageCapture: ImageCapture

    private val cameraSelector = CameraSelector.Builder().build()
    private val preview = Preview.Builder().build()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        cameraPreview = view.findViewById(R.id.camera_preview)
        takePictureButton = view.findViewById(R.id.take_picture_button)

        imageCapture = ImageCapture.Builder()
            .setTargetRotation(Surface.ROTATION_90)
            .build()

        cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())


        if (permissionsGranted()) {
            configCameraPreview()
        } else {
            requestCameraPermission()
        }


        takePictureButton.setOnClickListener {
            takePicture()
        }


    }

    private fun permissionsGranted() =
        requireActivity().checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED

    private fun requestCameraPermission() {
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
            if (granted) {
                configCameraPreview()
            } else {
                Toast.makeText(requireContext(), "Didn't work =/", Toast.LENGTH_SHORT).show()
            }
        }.launch(Manifest.permission.CAMERA)
    }


    private fun configCameraPreview() {
        cameraProviderFuture.addListener({
            cameraProvider = cameraProviderFuture.get()
            bindPreview(cameraProvider)

        }, ContextCompat.getMainExecutor(requireContext()))
    }

    private fun bindPreview(cameraProvider: ProcessCameraProvider) {

        preview.setSurfaceProvider(cameraPreview.surfaceProvider)


        camera = cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageCapture)
    }

    private fun takePicture() {
        val outPutFileDirName =
            requireContext().filesDir.absolutePath + File.separator + "my_photos" + File.separator
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
            ContextCompat.getMainExecutor(requireContext()),
            object : ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                    Toast.makeText(requireContext(), "Image saved", Toast.LENGTH_SHORT).show()
                }

                override fun onError(exception: ImageCaptureException) {
                    exception.printStackTrace()
                    Toast.makeText(requireContext(), "Image not saved", Toast.LENGTH_SHORT).show()
                }

            }

        )

    }


}