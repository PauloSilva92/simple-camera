package com.example.simplecamera.ui

import android.content.pm.PackageManager
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.FragmentActivity

class PermissionRequester(
    private val activity: FragmentActivity
) {

    fun request(permission: String, onPermissionGranted: () -> Unit, onError: () -> Unit) {
        if (permissionGranted(permission).not()) {
            requestPermission(permission, onPermissionGranted)
        }
    }

    private fun permissionGranted(permission: String) =
        activity.checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED

    private fun requestPermission(permission: String, onPermissionGranted: () -> Unit) {
        activity.registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
            if (granted) {
                onPermissionGranted()
            } else {

            }
        }.launch(permission)
    }
}