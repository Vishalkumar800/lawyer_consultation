package com.rach.lawyerapp.appPermissions.viewModel

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import com.rach.lawyerapp.MainActivity
import com.rach.lawyerapp.appPermissions.PermissionsUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PermissionViewModel @Inject constructor(
    private val permissionsUtils: PermissionsUtils
) : ViewModel() {

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    fun hasAllPermissionsGranted(): Boolean {
        return permissionsUtils.hasPermissionGranted()
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    fun shouldShowPermissionRationaleGranted(activity: MainActivity): Boolean {
        return permissionsUtils.shouldShowPermissionRationale(activity)
    }

}