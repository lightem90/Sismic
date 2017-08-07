package com.polito.sismic.Interactors.Helpers

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.os.Build
import android.support.v4.content.ContextCompat
import com.polito.sismic.Presenters.ReportActivity.Fragments.InfoLocReportFragment


/**
 * Created by Matteo on 06/08/2017.
 */
class PermissionsHelper
{
    val PERMISSION_POSITION = 100
    var PERMISSION_POSITION_GRANTED = false

    //Solo internet e posizione
    fun checAndAskLocationPermissions(caller : Activity, fragment : InfoLocReportFragment)
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            if (ContextCompat.checkSelfPermission(caller, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                    ContextCompat.checkSelfPermission(caller, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            {
                fragment.requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION),
                        PERMISSION_POSITION)
            }
        }
    }

    fun handlePermissionResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray)
    {
        when (requestCode) {
            PERMISSION_POSITION -> {
                // If request is cancelled, the result arrays are empty.
                PERMISSION_POSITION_GRANTED = grantResults.size > 0 && grantResults[0] === PackageManager.PERMISSION_GRANTED
                                                                    && grantResults[1] === PackageManager.PERMISSION_GRANTED
            }
        }
    }
}