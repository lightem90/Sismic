package com.polito.sismic.Presenters.ReportActivity.Fragments

import android.content.Context
import android.content.Intent
import android.location.Location
import android.os.Bundle
import android.support.annotation.Nullable
import android.view.*
import com.google.android.gms.location.places.Place
import com.polito.sismic.AsyncTasks.PlaceDetailsTask
import com.polito.sismic.Extensions.toFormattedString
import com.polito.sismic.Extensions.toast
import com.polito.sismic.Interactors.Helpers.*
import com.polito.sismic.R
import com.stepstone.stepper.StepperLayout
import com.stepstone.stepper.VerificationError
import kotlinx.android.synthetic.main.info_loc_report_layout.*


/**
 * Created by Matteo on 29/07/2017.
 */
class InfoLocReportFragment : BaseReportFragment() {

    private var mLocationCallback: InfoLocReportFragment.CurrentLocationProvided? = null
    private var mActionHelper = LocalizationActionHelper()
    private var mPermissionHelper = PermissionsHelper()
    private lateinit var mLocaliationInfoHelper: LocationInfoHelper

    // Container Activity must implement this interface
    interface CurrentLocationProvided {
        fun onLocationAcquired(location: Location)
        fun onCoordinatesUpdated()
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mLocationCallback = context as CurrentLocationProvided?
        } catch (e: ClassCastException) {
            throw ClassCastException(context!!.toString() + " must implement CurrentLocationProvided")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mPermissionHelper.checAndAskLocationPermissions(activity, this)
        mLocaliationInfoHelper = LocationInfoHelper(activity)
        mLocaliationInfoHelper.initialize()
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getReport().let {
            report_info_name_label.setValue(it.reportDetails.userIdentifier)
            report_info_number_label.setValue(it.reportDetails.id.toString())
            report_info_date_label.setValue(it.reportDetails.date.toFormattedString())
        }

        comune_parameter.attachDataConfirmedCallback { newComune ->
            mLocaliationInfoHelper.setZoneCodeForComune(zona_sismica_parameter, codice_istat_parameter, newComune)
        }
        province_parameter.attachDataConfirmedCallback { newProvince ->
            mLocaliationInfoHelper.setComuniSuggestionForProvince(comune_parameter, newProvince)
        }
        region_parameter.attachDataConfirmedCallback { newRegion ->
            mLocaliationInfoHelper.setProvinceSuggestionForRegion(province_parameter, newRegion)
        }

        //Signal to the activity that latitude and longitude changed, so certain data should be recalculated
        //TODO, dont trigger if data doesnt't change
        lat_parameter.attachDataConfirmedCallback { mLocationCallback?.onCoordinatesUpdated() }
        long_parameter.attachDataConfirmedCallback { mLocationCallback?.onCoordinatesUpdated() }
    }

    override fun onCreateView(inflater: LayoutInflater?, @Nullable container: ViewGroup?, @Nullable savedInstanceState: Bundle?): View? {
        return inflateFragment(R.layout.info_loc_report_layout, inflater, container)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item != null) {
            when (item.itemId) {
                R.id.reverseGeolocalization -> {
                    if (havePermission()) mActionHelper.handleActionRequest(LocalizationActionType.ReverseLocalization, this, null)
                    return true
                }

                R.id.geolocalization -> {
                    if (havePermission()) mActionHelper.handleActionRequest(LocalizationActionType.Localization, this, mLocationCallback)
                    return true
                }

                R.id.fromMap -> {
                    if (havePermission()) mActionHelper.handleActionRequest(LocalizationActionType.PlacePicker, this, null)
                    return true
                }
            }
        }
        return false;
    }

    private fun havePermission(): Boolean {
        if (!mPermissionHelper.PERMISSION_POSITION_GRANTED) {
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
        mPermissionHelper.handlePermissionResult(requestCode, grantResults)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            mActionHelper.PLACE_PICKER_REQUEST -> {
                updateByPlace(mActionHelper.handlePickerResponse(activity, resultCode, data))
            }

            mActionHelper.REVERSE_LOCALIZATION_REQUEST -> {
                updateByPlace(mActionHelper.handleAutoCompleteMapsResponse(activity, resultCode, data))
            }
        }
    }

    private fun updateByPlace(place: Place?) {
        if (place != null) {
            lat_parameter.setParameterValue("%.4f".format(place.latLng.latitude))
            long_parameter.setParameterValue("%.4f".format(place.latLng.longitude))

            askGoogleForPlaceId(place)
        }
    }

    //TODO: remove
    fun updateByLocation(location: Location) {
        lat_parameter.setParameterValue("%.3f".format(location.latitude))
        long_parameter.setParameterValue("%.3f".format(location.longitude))
        country_parameter.setParameterValue("italia")
        region_parameter.setParameterValue("marche")
        province_parameter.setParameterValue("pesaro")
        comune_parameter.setParameterValue("pesaro")
        address_parameter.setParameterValue("via achilli")
        zona_sismica_parameter.setParameterValue("1")
        codice_istat_parameter.setParameterValue("2")
        cap_parameter.setParameterValue("61122")
    }

    private fun askGoogleForPlaceId(place: Place) {
        PlaceDetailsTask(view!!, context).execute(place)
    }

    //all parameters must have a value
    override fun verifyStep(): VerificationError? {
        if (lat_parameter.isEmpty()) return VerificationError(String.format(resources.getString(R.string.verification_empty_field), lat_parameter.getTitle()))
        if (long_parameter.isEmpty()) return VerificationError(String.format(resources.getString(R.string.verification_empty_field), long_parameter.getTitle()))
        if (country_parameter.isEmpty()) return VerificationError(String.format(resources.getString(R.string.verification_empty_field), country_parameter.getTitle()))
        if (region_parameter.isEmpty()) return VerificationError(String.format(resources.getString(R.string.verification_empty_field), region_parameter.getTitle()))
        if (province_parameter.isEmpty()) return VerificationError(String.format(resources.getString(R.string.verification_empty_field), province_parameter.getTitle()))
        if (comune_parameter.isEmpty()) return VerificationError(String.format(resources.getString(R.string.verification_empty_field), comune_parameter.getTitle()))
        if (address_parameter.isEmpty()) return VerificationError(String.format(resources.getString(R.string.verification_empty_field), address_parameter.getTitle()))
        if (zona_sismica_parameter.isEmpty()) return VerificationError(String.format(resources.getString(R.string.verification_empty_field), zona_sismica_parameter.getTitle()))
        if (codice_istat_parameter.isEmpty()) return VerificationError(String.format(resources.getString(R.string.verification_empty_field), codice_istat_parameter.getTitle()))
        return super.verifyStep()
    }

    //callback to activity, must add location info to all other fragments
    override fun onNextClicked(callback: StepperLayout.OnNextClickedCallback?) {
        getReport().reportState.localizationState = UiMapper.createLocationStateForDomain(this)
        super.onNextClicked(callback)
    }
}


