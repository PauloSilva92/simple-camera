package com.example.simplecamera.common.depedencyinjection

import androidx.camera.view.PreviewView
import androidx.fragment.app.FragmentActivity
import com.example.simplecamera.common.camera.CameraController
import com.example.simplecamera.common.camera.CameraXController
import com.example.simplecamera.common.file.FileUtils
import com.example.simplecamera.common.peermission.PermissionRequester

class AppComposition(
    private val activity: FragmentActivity
) {

    val permissionRequester = PermissionRequester(activity)

    val fileUtils = FileUtils(activity)

    fun cameraController(previewView: PreviewView): CameraController = CameraXController(
        previewView,
        activity,
        fileUtils,
        permissionRequester
    )
}