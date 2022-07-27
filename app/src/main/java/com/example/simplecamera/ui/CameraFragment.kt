package com.example.simplecamera.ui

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.AppCompatImageView
import androidx.camera.core.CameraSelector
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.simplecamera.R
import com.google.common.util.concurrent.ListenableFuture


class CameraFragment : Fragment(R.layout.fragment_camera) {

    private lateinit var cameraPreview: PreviewView
    private lateinit var takePictureButton: AppCompatImageView

    private lateinit var cameraProviderFuture: ListenableFuture<ProcessCameraProvider>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        cameraPreview = view.findViewById(R.id.camera_preview)
        takePictureButton = view.findViewById(R.id.take_picture_button)

        cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())


        if (permissionsGranted()) {
            configCameraPreview()
        } else {
            requestCameraPermission()
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
            val cameraProvider = cameraProviderFuture.get()
            bindPreview(cameraProvider)

        }, ContextCompat.getMainExecutor(requireContext()))
    }

    private fun bindPreview(cameraProvider: ProcessCameraProvider) {
        val preview = Preview.Builder().build()
        val cameraSelector = CameraSelector.Builder().build()

        preview.setSurfaceProvider(cameraPreview.surfaceProvider)

        var camera = cameraProvider.bindToLifecycle(this, cameraSelector, preview)
    }


}