package com.rach.lawyerapp.appPermissions

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.rach.lawyerapp.MainActivity
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PermissionsUtils @Inject constructor(
    @ApplicationContext private val context: Context
){

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    fun hasPermissionGranted():Boolean{
        return ContextCompat.checkSelfPermission(context,Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_DENIED
                &&
                ContextCompat.checkSelfPermission(context,Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
                &&
                ContextCompat.checkSelfPermission(context,Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED

    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    fun shouldShowPermissionRationale(activity: MainActivity): Boolean{
      return  ActivityCompat.shouldShowRequestPermissionRationale(activity,Manifest.permission.CAMERA)
              ||
              ActivityCompat.shouldShowRequestPermissionRationale(activity,Manifest.permission.POST_NOTIFICATIONS)
              ||
              ActivityCompat.shouldShowRequestPermissionRationale(activity,Manifest.permission.RECORD_AUDIO)
    }

}