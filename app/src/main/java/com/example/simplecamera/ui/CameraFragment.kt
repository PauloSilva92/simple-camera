package com.example.simplecamera.ui

import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.AppCompatImageView
import androidx.camera.view.PreviewView
import androidx.fragment.app.Fragment
import com.example.simplecamera.R
import com.example.simplecamera.common.camera.CameraController
import com.example.simplecamera.common.dependencyinjection.AppComposition


class CameraFragment : Fragment(R.layout.fragment_camera) {

    private lateinit var cameraController: CameraController
    private lateinit var appComposition: AppComposition

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val cameraPreview: PreviewView = view.findViewById(R.id.camera_preview)
        val takePictureButton: AppCompatImageView = view.findViewById(R.id.take_picture_button)
        val toggleFlashButton: AppCompatImageView = view.findViewById(R.id.toggle_flash_button)

        appComposition = AppComposition(requireActivity())
        cameraController = appComposition.cameraController(cameraPreview)

        cameraController.start()

        takePictureButton.setOnClickListener {
            cameraController.takePhoto()
        }

        toggleFlashButton.setOnClickListener {
            cameraController.toggleTorch()


        }
    }
}