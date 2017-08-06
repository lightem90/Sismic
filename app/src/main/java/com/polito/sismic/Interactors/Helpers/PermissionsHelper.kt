package com.polito.sismic.Interactors.Helpers

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.os.Build

/**
 * Created by Matteo on 06/08/2017.
 */
class PermissionsHelper
{
    val PERMISSION_POSITION = 50
    val PERMISSION_INTERNET = 51

    //Solo internet e posizione
    fun checkPermissions(caller : Activity)
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            if (caller.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                caller.requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), PERMISSION_POSITION)
                return
            }

            if (caller.checkSelfPermission(Manifest.permission.ACCESS_NETWORK_STATE) != PackageManager.PERMISSION_GRANTED) {

                caller.requestPermissions(arrayOf(Manifest.permission.ACCESS_NETWORK_STATE), PERMISSION_INTERNET)
                return
            }
        }
    }
}