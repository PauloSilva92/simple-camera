package com.example.simplecamera.common.camera

import android.view.View

interface CameraController {
    fun <T : View> start(previewView: T)
    fun stop()
    fun takePhoto()
    fun toggleTorch()
}