package com.polito.sismic.Interactors.Helpers
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import com.google.android.gms.location.places.ui.PlacePicker
import com.google.android.gms.location.places.Place

import android.content.Intent
import android.location.Criteria
import android.content.Context.LOCATION_SERVICE
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.gms.tasks.Task


class ActionHelper {

    val PLACE_PICKER_REQUEST = 50
    val LOCALIZATION_REQUEST = 51
    val REVERSE_LOCALIZATION_REQUEST = 52
    private var  mFusedLocationClient: FusedLocationProviderClient? = null

    fun handleActionRequest(type : ActionType, caller : Activity)
    {
        when (type)
        {
            ActionType.PlacePicker -> launchPlacePicker(caller)
            ActionType.Localization -> launchLocalization(caller)
            ActionType.ReverseLocalization -> launchReverseLocalization(caller)
        }
    }

    private fun launchReverseLocalization(caller: Activity) {
    }


    @SuppressLint("MissingPermission")
    private fun launchLocalization(caller: Activity) : Location? {
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(caller)
        var  requestedLocation: Location? = null
        if(mFusedLocationClient != null)
        {
            mFusedLocationClient!!.requestLocationUpdates(LocationRequest(), LocationCallback(), Looper.getMainLooper())
            mFusedLocationClient!!.lastLocation?.addOnSuccessListener(caller, OnSuccessListener<Location> { location ->
                // Got last known location. In some rare situations this can be null.
                requestedLocation = location
            })
            mFusedLocationClient!!.removeLocationUpdates(LocationCallback())
        }
        return requestedLocation
    }

    private fun launchPlacePicker(caller: Activity) {
        val builder = PlacePicker.IntentBuilder()

        caller.startActivityForResult(builder.build(caller), PLACE_PICKER_REQUEST)
    }

    fun handlePickerResponse(caller: Activity, resultCode : Int, data : Intent?) : Place?
    {
        if (resultCode == Activity.RESULT_OK) {
            return PlacePicker.getPlace(caller, data)
        }

        //TODO segnalare errore
        return null
    }
}



