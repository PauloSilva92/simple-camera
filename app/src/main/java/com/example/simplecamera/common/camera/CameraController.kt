package com.example.simplecamera.common.camera

interface CameraController {
    fun start()
    fun stop()
    fun takePhoto()
    fun toggleTorch()
}