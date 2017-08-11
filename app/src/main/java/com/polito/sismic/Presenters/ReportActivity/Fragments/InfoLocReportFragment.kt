package com.polito.sismic.Presenters.ReportActivity.Fragments

import android.content.Context
import android.content.Intent
import android.location.Location
import android.os.Bundle
import android.support.annotation.Nullable
import android.view.*
import com.google.android.gms.location.places.Place
import com.polito.sismic.AsyncTasks.PlaceDetailsTask
import com.polito.sismic.Extensions.toast
import com.polito.sismic.Interactors.Helpers.LocalizationActionHelper
import com.polito.sismic.Interactors.Helpers.LocalizationActionType
import com.polito.sismic.Interactors.Helpers.LocationSuggestionsHelper
import com.polito.sismic.Interactors.Helpers.PermissionsHelper
import com.polito.sismic.Presenters.CustomLayout.ParameterReportLayout
import com.polito.sismic.R
import kotlinx.android.synthetic.main.info_loc_report_layout.*


/**
 * Created by Matteo on 29/07/2017.
 */
class InfoLocReportFragment : BaseReportFragment(), ParameterReportLayout.RegionSelectedListener, ParameterReportLayout.ProvinceSelectedListener {

    private var  mLocationCallback: InfoLocReportFragment.OnCurrentLocationProvided? = null
    private val  mLocationSuggestionsHelper : LocationSuggestionsHelper = LocationSuggestionsHelper(activity)
    private var  mActionHelper = LocalizationActionHelper()
    private var  mPermissionHelper = PermissionsHelper()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mPermissionHelper.checAndAskLocationPermissions(activity, this)
        setHasOptionsMenu(true)
        mLocationSuggestionsHelper.initialize()
    }


    override fun onAttach(context: Context?) {
        super.onAttach(context)
        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try
        {
            mLocationCallback = context as OnCurrentLocationProvided?
        }
        catch (e: ClassCastException) {
            throw ClassCastException(context!!.toString() + " must implement OnCurrentLocationProvided")
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, @Nullable container: ViewGroup?, @Nullable savedInstanceState: Bundle?): View? {
        return inflateFragment(R.layout.info_loc_report_layout, inflater, container)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item != null) {
            when (item.itemId)
            {
                R.id.reverseGeolocalization ->
                {
                    if (havePermission()) mActionHelper.handleActionRequest(LocalizationActionType.ReverseLocalization, this, null)
                    return true
                }

                R.id.geolocalization ->
                {
                    if (havePermission()) mActionHelper.handleActionRequest(LocalizationActionType.Localization, this, mLocationCallback)
                    return true
                }

                R.id.fromMap ->
                {
                    if (havePermission()) mActionHelper.handleActionRequest(LocalizationActionType.PlacePicker, this, null)
                    return true
                }
            }
        }
        return false;
    }

    fun havePermission() : Boolean
    {
        if (!mPermissionHelper.PERMISSION_POSITION_GRANTED)
        {
            context.toast(R.string.permission_denied)
            return false
        }
        return true
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.localization_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        //Setta la proprietà interna "permission granted"
        mPermissionHelper.handlePermissionResult(requestCode, permissions, grantResults)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode)
        {
            mActionHelper.PLACE_PICKER_REQUEST ->
            {
                updateByPlace(mActionHelper.handlePickerResponse(activity, resultCode, data))
            }

            mActionHelper.REVERSE_LOCALIZATION_REQUEST ->
            {
                updateByPlace(mActionHelper.handleAutoCompleteMapsResponse(activity, resultCode, data))
            }
        }
    }

    // Container Activity must implement this interface
    interface OnCurrentLocationProvided {
        fun onLocationAcquired(location: Location)
    }

    fun updateByPlace(place : Place?)
    {
        if (place != null)
        {
            lat_parameter.setParameterValue("%.4f".format(place.latLng.latitude))
            long_parameter.setParameterValue("%.4f".format(place.latLng.longitude))

            askGoogleForPlaceId(place)
        }
    }

    fun updateByLocation(location: Location)
    {
        lat_parameter.setParameterValue("%.4f".format(location.latitude))
        long_parameter.setParameterValue("%.4f".format(location.longitude))
    }

    private fun askGoogleForPlaceId(place: Place) {
        PlaceDetailsTask(view!!, context).execute(place)
    }

    override fun getAllViewParameters(): MutableList<Pair<String, Any>> {

        return mutableListOf(
                Pair(report_info_number_label.id.toString(), report_info_number_label.getValue().toInt()),
                Pair(report_info_name_label.id.toString(), report_info_name_label.getValue()),
                Pair(report_info_data_label.id.toString(), report_info_data_label.getValue()),
                Pair(lat_parameter.id.toString(), lat_parameter.getParameterValue().toDouble()),
                Pair(long_parameter.id.toString(), long_parameter.getParameterValue().toDouble()),
                Pair(country_parameter.id.toString(), country_parameter.getParameterValue()),
                Pair(region_parameter.id.toString(), region_parameter.getParameterValue()),
                Pair(comune_parameter.id.toString(), comune_parameter.getParameterValue()),
                Pair(address_parameter.id.toString(), address_parameter.getParameterValue()),
                Pair(cap_parameter.id.toString(), cap_parameter.getParameterValue()),
                Pair(zona_sismica_parameter.id.toString(), zona_sismica_parameter.getParameterValue().toInt()),
                Pair(codice_istat_parameter.id.toString(), codice_istat_parameter.getParameterValue().toInt())
        )
    }

    //Is the helper of the fragment that gives back the suggestions to its own children
    override fun OnRegionSelected(newRegion: String) {
        province_parameter.setSuggestions(mLocationSuggestionsHelper.getProvinceByRegion(newRegion))
    }

    override fun OnProvinceSelected(newProvince: String) {
        comune_parameter.setSuggestions(mLocationSuggestionsHelper.getLocalityByProvince(newProvince))
    }
}



