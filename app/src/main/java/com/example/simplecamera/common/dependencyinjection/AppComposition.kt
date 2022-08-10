package com.example.simplecamera.common.dependencyinjection

import android.content.Context
import androidx.fragment.app.FragmentActivity
import com.example.simplecamera.common.camera.CameraController
import com.example.simplecamera.common.camera.CameraXController
import com.example.simplecamera.common.file.FileUtils
import com.example.simplecamera.common.permission.PermissionRequester
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.FragmentComponent

@Module
@InstallIn(FragmentComponent::class)
class AppComposition {

    @Provides
    fun context(activity: FragmentActivity): Context = activity

    @Provides
    fun permissionRequester(fragmentActivity: FragmentActivity): PermissionRequester =
        PermissionRequester(fragmentActivity)

    @Provides
    fun fileUtils(fragmentActivity: FragmentActivity): FileUtils = FileUtils(fragmentActivity)

    @Provides
    fun cameraController(
        fragmentActivity: FragmentActivity,
        fileUtils: FileUtils,
        permissionRequester: PermissionRequester
    ): CameraController = CameraXController(
        fragmentActivity,
        fileUtils,
        permissionRequester
    )
}