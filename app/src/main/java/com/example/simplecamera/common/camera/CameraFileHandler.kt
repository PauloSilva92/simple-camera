package com.example.simplecamera.common.camera

import androidx.camera.core.ImageCapture

interface CameraFileHandler {
    fun createOutputFileOptions(): ImageCapture.OutputFileOptions
}