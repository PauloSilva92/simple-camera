package com.example.simplecamera.common.permission

import android.content.pm.PackageManager
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.FragmentActivity
import javax.inject.Inject

class PermissionRequester @Inject constructor(
    private val activity: FragmentActivity
) {

    fun request(permission: String, onPermissionGranted: () -> Unit, onError: () -> Unit) {
        if (permissionGranted(permission).not()) {
            requestPermission(permission, onPermissionGranted, onError)
        } else {
            onPermissionGranted()
        }
    }

    private fun permissionGranted(permission: String) =
        activity.checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED

    private fun requestPermission(
        permission: String,
        onPermissionGranted: () -> Unit,
        onError: () -> Unit
    ) {
        activity.registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
            if (granted) {
                onPermissionGranted()
            } else {
                onError()
            }
        }.launch(permission)
    }
}
