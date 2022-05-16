package com.fernando.core.presentation.components.permissions

import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment


class PermissionRequester(
    fragment: Fragment,
    private val permission: String,
    onDenied: () -> Unit = {},
    onShowRationale: () -> Unit = {}
) {
    private var onGranted: () -> Unit = {}

    private val permissionContract = ActivityResultContracts.RequestPermission()

    private val launcher = fragment.registerForActivityResult(permissionContract) { isGranted ->
        when {
            isGranted -> onGranted()
            fragment.shouldShowRequestPermissionRationale(permission) -> onShowRationale()
            else -> onDenied()
        }
    }

    fun runWithPermission(onGranted: () -> Unit) {
        this.onGranted = onGranted
        launcher.launch(permission)
    }
}