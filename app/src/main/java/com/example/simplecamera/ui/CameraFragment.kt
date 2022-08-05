package com.example.simplecamera.ui

import android.Manifest
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.AppCompatImageView
import androidx.camera.view.PreviewView
import androidx.fragment.app.Fragment
import com.example.simplecamera.R
import com.example.simplecamera.common.camera.CameraController
import com.example.simplecamera.common.camera.CameraXController
import com.example.simplecamera.common.peermission.PermissionRequester


class CameraFragment : Fragment(R.layout.fragment_camera) {

    private lateinit var cameraPreview: PreviewView
    private lateinit var takePictureButton: AppCompatImageView
    private lateinit var toggleFlashButton: AppCompatImageView

    private lateinit var cameraController: CameraController
    private lateinit var permissionRequester: PermissionRequester

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        cameraPreview = view.findViewById(R.id.camera_preview)
        takePictureButton = view.findViewById(R.id.take_picture_button)
        toggleFlashButton = view.findViewById(R.id.toggle_flash_button)


        cameraController = CameraXController(cameraPreview, requireContext(), this)


        permissionRequester = PermissionRequester(requireActivity())
        permissionRequester.request(
            Manifest.permission.CAMERA,
            {
                cameraController.start()
            }, {
                Toast.makeText(activity, "Didn't work =/", Toast.LENGTH_SHORT).show()
            }
        )

        takePictureButton.setOnClickListener {
            cameraController.takePhoto()
        }

        toggleFlashButton.setOnClickListener {
            cameraController.toggleTorch()
        }


    }



}