package com.polito.sismic.Interactors.Helpers

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.os.Build
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import com.polito.sismic.Presenters.ReportActivity.Fragments.InfoLocReportFragment


/**
 * Created by Matteo on 06/08/2017.
 */
class PermissionsHelper {
    val PERMISSION_POSITION = 100
    val PERMISSION_AUDIO = 200
    val PERMISSION_VIDEO = 300
    var PERMISSION_POSITION_GRANTED = false
    var PERMISSION_AUDIO_GRANTED = false
    var PERMISSION_VIDEO_GRANTED = false

    private val audio_permissions = arrayOf(Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE)
    private val video_permissions = arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
    //Solo internet e posizione
    fun checAndAskLocationPermissions(caller: Activity, fragment: InfoLocReportFragment) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(caller, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                    ContextCompat.checkSelfPermission(caller, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                fragment.requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION),
                        PERMISSION_POSITION)
            } else
                PERMISSION_POSITION_GRANTED = true
        }
    }

    fun checkAndAskAudioPermissions(caller: Activity) {
        if (ActivityCompat.checkSelfPermission(caller, audio_permissions[0]) != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(caller, audio_permissions[1]) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(caller, audio_permissions, PERMISSION_AUDIO)
        }
        else
            PERMISSION_AUDIO_GRANTED = true
    }

    fun checkAndAskVideoPermissions(caller: Activity) {
        if (ActivityCompat.checkSelfPermission(caller, video_permissions[0]) != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(caller, video_permissions[1]) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(caller, video_permissions, PERMISSION_VIDEO)
        }
        else
            PERMISSION_VIDEO_GRANTED = true
    }

    fun handlePermissionResult(requestCode: Int, grantResults: IntArray) {
        when (requestCode) {
            PERMISSION_POSITION -> {
                // If request is cancelled, the result arrays are empty.
                PERMISSION_POSITION_GRANTED = grantResults.isNotEmpty() && grantResults[0] === PackageManager.PERMISSION_GRANTED
                        && grantResults[1] === PackageManager.PERMISSION_GRANTED
            }
            PERMISSION_AUDIO -> {
                PERMISSION_AUDIO_GRANTED = grantResults.isNotEmpty() && grantResults[0] === PackageManager.PERMISSION_GRANTED
                        && grantResults[1] === PackageManager.PERMISSION_GRANTED
            }
            PERMISSION_VIDEO -> {
                PERMISSION_AUDIO_GRANTED = grantResults.isNotEmpty() && grantResults[0] === PackageManager.PERMISSION_GRANTED
                        && grantResults[1] === PackageManager.PERMISSION_GRANTED
            }
        }
    }
}