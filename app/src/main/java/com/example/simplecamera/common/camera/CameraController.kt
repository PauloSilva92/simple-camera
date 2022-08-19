package com.example.simplecamera.common.camera

import android.net.Uri
import android.view.View

interface CameraController {
    fun <T : View> start(previewView: T)
    fun stop()
    fun takePhoto(onSuccess: (photoUri: Uri?) -> Unit, onError: (exception: Throwable) -> Unit)
    fun toggleTorch(toggleCallback: (flashState: FlashState) -> Unit)
}