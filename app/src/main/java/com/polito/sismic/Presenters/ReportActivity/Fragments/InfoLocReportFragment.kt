package com.polito.sismic.Presenters.ReportActivity.Fragments

import android.content.Intent
import android.os.Bundle
import android.support.annotation.Nullable
import android.view.*
import com.polito.sismic.Extensions.toast
import com.polito.sismic.Interactors.Helpers.ActionHelper
import com.polito.sismic.Interactors.Helpers.ActionType
import com.polito.sismic.Interactors.Helpers.PermissionsHelper
import com.polito.sismic.Presenters.CustomLayout.ParameterReportLayout
import com.polito.sismic.R


/**
 * Created by Matteo on 29/07/2017.
 */
class InfoLocReportFragment : BaseReportFragment() {

    private var  mZonaSismicaParameter: ParameterReportLayout? = null
    private var  mCodiceIstatParameter: ParameterReportLayout? = null
    private var  mLatParameter: ParameterReportLayout? = null
    private var  mLonParameter: ParameterReportLayout? = null
    private var  mAddressParameter: ParameterReportLayout? = null
    private var  mActionHelper = ActionHelper()
    private var  mPermissionHelper = PermissionsHelper()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mPermissionHelper.checAndAskLocationPermissions(activity)
        setHasOptionsMenu(true)
    }



    override fun onCreateView(inflater: LayoutInflater?, @Nullable container: ViewGroup?, @Nullable savedInstanceState: Bundle?): View? {

        var view = inflateFragment(R.layout.info_loc_report_layout, inflater, container)

        //Da inizializzare coi bottoni
        mLatParameter = view?.findViewById<ParameterReportLayout>(R.id.lat_parameter)
        mLonParameter = view?.findViewById<ParameterReportLayout>(R.id.long_parameter)

        //Da inizializzare accedendo al server
        mZonaSismicaParameter = view?.findViewById<ParameterReportLayout>(R.id.zona_sismica_parameter)
        mCodiceIstatParameter = view?.findViewById<ParameterReportLayout>(R.id.codice_istat_parameter)
        mAddressParameter = view?.findViewById<ParameterReportLayout>(R.id.address_parameter)

        return view;
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        if (!mPermissionHelper.PERMISSION_POSITION_GRANTED)
        {
            context.toast(R.string.permission_denied)
            return true
        }
        if (item != null) {
            when (item.itemId)
            {
                R.id.reverseGeolocalization ->
                {
                    mActionHelper.handleActionRequest(ActionType.ReverseLocalization, activity)
                    return true
                }

                R.id.geolocalization ->
                {
                    mActionHelper.handleActionRequest(ActionType.Localization, activity)
                    return true
                }

                R.id.fromMap ->
                {
                    mActionHelper.handleActionRequest(ActionType.PlacePicker, activity)
                    return true
                }
            }
        }
        return false;
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.localization_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        //Setta la proprietà interna "permission granted"
        mPermissionHelper.handelPermissionResult(requestCode, permissions, grantResults)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode)
        {
            mActionHelper.PLACE_PICKER_REQUEST ->
            {
                val place = mActionHelper.handlePickerResponse(activity, resultCode, data)
                if (place != null)
                {
                    mLatParameter?.setParameterValue(place.latLng?.latitude.toString())
                    mLonParameter?.setParameterValue(place.latLng?.longitude.toString())
                    mAddressParameter?.setParameterValue(place.address?.toString()!!)
                }
            }

            mActionHelper.LOCALIZATION_REQUEST ->
            {

            }

            mActionHelper.REVERSE_LOCALIZATION_REQUEST ->
            {

            }
        }
    }

}

