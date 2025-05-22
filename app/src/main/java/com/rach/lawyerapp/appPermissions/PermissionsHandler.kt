package com.rach.lawyerapp.appPermissions

import android.Manifest
import androidx.fragment.app.FragmentActivity
import com.permissionx.guolindev.PermissionX
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PermissionsHandler @Inject constructor() {

    fun requestPermission(
        activity: FragmentActivity,
        onResult: (Boolean) -> Unit
    ) {

        PermissionX.init(activity).permissions(Manifest.permission.SYSTEM_ALERT_WINDOW)
            .onExplainRequestReason { scope, deniedList ->
                val message =
                    "We need your consent for the following permissions to enable offline call functionality."
                scope.showRequestReasonDialog(deniedList, message, "Allow", "Deny")
            }.onForwardToSettings { scope, deniedList ->
                val message =
                    "Please enable the following permissions in settings to use offline calls."
                scope.showForwardToSettingsDialog(deniedList, message, "Settings")
            }.request { allGranted, grantedList, deniedList ->
                if (!allGranted) {
                    Timber.e("Permissions denied: $deniedList")
                } else {
                    Timber.d("All permissions granted: $grantedList")
                }
                onResult(allGranted)
            }

    }

}