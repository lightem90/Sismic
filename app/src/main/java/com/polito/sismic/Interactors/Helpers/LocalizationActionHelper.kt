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
import com.polito.sismic.Extensions.toast
import com.polito.sismic.Presenters.ReportActivity.Fragments.InfoLocReportFragment
import com.polito.sismic.R
import com.google.android.gms.location.places.AutocompleteFilter




class LocalizationActionHelper {

    val PLACE_PICKER_REQUEST = 50
    val REVERSE_LOCALIZATION_REQUEST = 51

    private var  mFusedLocationClient: FusedLocationProviderClient? = null

    fun handleActionRequest(typeLocalization: LocalizationActionType, caller : InfoLocReportFragment, mLocationCallback: InfoLocReportFragment.CurrentLocationProvided?)
    {
        when (typeLocalization)
        {
            LocalizationActionType.PlacePicker -> launchPlacePicker(caller)
            LocalizationActionType.Localization -> launchLocalization(caller.activity, mLocationCallback)
            LocalizationActionType.ReverseLocalization -> launchReverseLocalization(caller)
        }
    }

    private fun launchReverseLocalization(caller: InfoLocReportFragment) {
        //Autocomplete filter on Italian addresses
        val typeFilter = AutocompleteFilter.Builder()
                .setCountry("IT")
                .setTypeFilter(AutocompleteFilter.TYPE_FILTER_ADDRESS)
                .build()
        val intent = PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)
                .setFilter(typeFilter)
                .build(caller.activity)
        caller.startActivityForResult(intent, REVERSE_LOCALIZATION_REQUEST)
    }

    //Already checked!
    @SuppressLint("MissingPermission")
    private fun launchLocalization(caller: Activity, mLocationCallback: InfoLocReportFragment.CurrentLocationProvided?) {
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(caller)
        mFusedLocationClient?.let {
            it.requestLocationUpdates(LocationRequest(), LocationCallback(), Looper.getMainLooper())
            it.lastLocation?.addOnSuccessListener(caller, { location ->
                // Got last known location. In some rare situations this can be null.
                mLocationCallback?.onLocationAcquired(location)
            })
            it.removeLocationUpdates(LocationCallback())
        }
    }

    private fun launchPlacePicker(caller: InfoLocReportFragment) {
        //The fragment launches and handles the result
        caller.startActivityForResult(PlacePicker.IntentBuilder().build(caller.activity), PLACE_PICKER_REQUEST)
    }

    fun handlePickerResponse(caller: Activity, resultCode : Int, data : Intent?) : Place?
    {
        if (resultCode == Activity.RESULT_OK) {
            return PlacePicker.getPlace(caller, data)
        }
        //Error message on failure
        caller.toast(R.string.place_picker_failed)
        return null
    }

    fun handleAutoCompleteMapsResponse(caller: Activity, resultCode : Int, data : Intent?) : Place?
    {
        if (resultCode == Activity.RESULT_OK) {
            return  PlaceAutocomplete.getPlace(caller, data)
        }
        //Error message on failure
        caller.toast(R.string.autocomplete_failed)
        return null
    }
}



