package com.polito.sismic.Interactors.Helpers

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.location.Location
import android.os.Looper
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.places.Place
import com.google.android.gms.location.places.ui.PlaceAutocomplete
import com.google.android.gms.location.places.ui.PlacePicker
import com.google.android.gms.tasks.OnSuccessListener
import com.polito.sismic.Presenters.ReportActivity.Fragments.InfoLocReportFragment
import android.support.v4.app.ActivityCompat.startActivityForResult




class ActionHelper {

    val PLACE_PICKER_REQUEST = 50
    val REVERSE_LOCALIZATION_REQUEST = 51

    private var  mFusedLocationClient: FusedLocationProviderClient? = null

    fun handleActionRequest(type : ActionType, caller : Activity, mLocationCallback: InfoLocReportFragment.OnCurrentLocationProvided?)
    {
        when (type)
        {
            ActionType.PlacePicker -> launchPlacePicker(caller)
            ActionType.Localization -> launchLocalization(caller, mLocationCallback)
            ActionType.ReverseLocalization -> launchReverseLocalization(caller)
        }
    }

    private fun launchReverseLocalization(caller: Activity) {

        val intent = PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN).build(caller)
        startActivityForResult(caller, intent, REVERSE_LOCALIZATION_REQUEST, null)
    }


    @SuppressLint("MissingPermission")
    private fun launchLocalization(caller: Activity, mLocationCallback: InfoLocReportFragment.OnCurrentLocationProvided?) {
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(caller)
        if(mFusedLocationClient != null)
        {
            mFusedLocationClient!!.requestLocationUpdates(LocationRequest(), LocationCallback(), Looper.getMainLooper())
            mFusedLocationClient!!.lastLocation?.addOnSuccessListener(caller, OnSuccessListener<Location> { location ->
                // Got last known location. In some rare situations this can be null.
                //Rimbalzo di eventi tra activity e fragment
                mLocationCallback?.onLocationAcquired(location)
            })
            mFusedLocationClient!!.removeLocationUpdates(LocationCallback())
        }
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

    fun handleAutoCompleteMapsResponse(caller: Activity, resultCode : Int, data : Intent?) : Place?
    {
        if (resultCode == Activity.RESULT_OK) {
            return  PlaceAutocomplete.getPlace(caller, data)
        }

        //TODO segnalare errore
        return null
    }
}



