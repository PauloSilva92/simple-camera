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
import com.example.simplecamera.common.file.FileUtils
import com.example.simplecamera.common.peermission.PermissionRequester


class CameraFragment : Fragment(R.layout.fragment_camera) {

    private lateinit var cameraController: CameraController
    private lateinit var permissionRequester: PermissionRequester
    private lateinit var fileUtils: FileUtils

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val cameraPreview: PreviewView = view.findViewById(R.id.camera_preview)
        val takePictureButton: AppCompatImageView = view.findViewById(R.id.take_picture_button)
        val toggleFlashButton: AppCompatImageView = view.findViewById(R.id.toggle_flash_button)

        fileUtils = FileUtils(requireContext())

        cameraController = CameraXController(
            cameraPreview,
            requireContext(),
            this,
            fileUtils
        )


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