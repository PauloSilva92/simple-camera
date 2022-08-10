package com.example.simplecamera.ui

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.AppCompatImageView
import androidx.camera.view.PreviewView
import androidx.fragment.app.Fragment
import com.example.simplecamera.R
import com.example.simplecamera.common.camera.CameraController
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class CameraFragment : Fragment(R.layout.fragment_camera) {

    @Inject
    lateinit var cameraController: CameraController

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val cameraPreview: PreviewView = view.findViewById(R.id.camera_preview)
        val takePictureButton: AppCompatImageView = view.findViewById(R.id.take_picture_button)
        val toggleFlashButton: AppCompatImageView = view.findViewById(R.id.toggle_flash_button)

        cameraController.start(cameraPreview)

        takePictureButton.setOnClickListener {
            cameraController.takePhoto(
                onSuccess = { photoUri ->
                    Toast.makeText(requireContext(), "Image saved: $photoUri", Toast.LENGTH_SHORT)
                        .show()
                },
                onError = { exception: Throwable -> exception.printStackTrace() }
            )
        }

        toggleFlashButton.setOnClickListener {
            cameraController.toggleTorch()
        }
    }
}