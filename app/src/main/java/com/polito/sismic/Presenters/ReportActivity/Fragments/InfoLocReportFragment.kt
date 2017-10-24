package com.polito.sismic.Presenters.ReportActivity.Fragments

import android.content.Context
import android.content.Intent
import android.location.Location
import android.os.AsyncTask
import android.os.Bundle
import android.support.annotation.Nullable
import android.view.*
import android.widget.ProgressBar
import com.google.android.gms.location.places.Place
import com.polito.sismic.Extensions.hideSoftKeyboard
import com.polito.sismic.Extensions.showSoftKeyboard
import com.polito.sismic.Extensions.toFormattedString
import com.polito.sismic.Extensions.toast
import com.polito.sismic.Interactors.Helpers.*
import com.polito.sismic.Presenters.CustomLayout.ParameterReportLayout
import com.polito.sismic.R
import com.stepstone.stepper.StepperLayout
import com.stepstone.stepper.VerificationError
import kotlinx.android.synthetic.main.info_loc_report_layout.*
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL


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

        //Signal to the activity that latitude and longitude changed, so certain data should be recalculated
        lat_parameter.attachDataConfirmedCallback {
            mLocationCallback?.onCoordinatesUpdated()
            long_parameter.requestFocus()
        }
        long_parameter.attachDataConfirmedCallback {
            mLocationCallback?.onCoordinatesUpdated()
            country_parameter.requestFocus()
            activity.showSoftKeyboard()
        }
        country_parameter.attachDataConfirmedCallback {
            region_parameter.requestFocus()
        }
        region_parameter.attachDataConfirmedCallback { newRegion ->
            mLocaliationInfoHelper.setProvinceSuggestionForRegion(province_parameter, newRegion)
            province_parameter.requestFocus()
        }
        province_parameter.attachDataConfirmedCallback { newProvince ->
            mLocaliationInfoHelper.setComuniSuggestionForProvince(comune_parameter, newProvince)
            comune_parameter.requestFocus()
        }
        comune_parameter.attachDataConfirmedCallback { newComune ->
            mLocaliationInfoHelper.setZoneCodeForComune(zona_sismica_parameter, codice_istat_parameter, newComune)
            address_parameter.requestFocus()
        }
        address_parameter.attachDataConfirmedCallback {
            cap_parameter.requestFocus()
        }
        cap_parameter.attachDataConfirmedCallback {
            zona_sismica_parameter.requestFocus()
        }
        zona_sismica_parameter.attachDataConfirmedCallback {
            codice_istat_parameter.requestFocus()
        }
        codice_istat_parameter.attachDataConfirmedCallback { if (!it.isEmpty()) activity.hideSoftKeyboard() }
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
            //with a higher precision the node calculation fails
            lat_parameter.setParameterValue("%.4f".format(place.latLng.latitude))
            long_parameter.setParameterValue("%.4f".format(place.latLng.longitude))

            askGoogleForPlaceId(place)
        }
    }

    fun updateByLocation(location: Location) {
        lat_parameter.setParameterValue("%.4f".format(location.latitude))
        long_parameter.setParameterValue("%.4f".format(location.longitude))
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


    inner class PlaceDetailsTask internal constructor(fragmentView: View, val context: Context) : AsyncTask<Place, Int, JSONObject>() {
        private var state_parameter: ParameterReportLayout? = null
        private var region_parameter: ParameterReportLayout? = null
        private var province_parameter: ParameterReportLayout? = null
        private var comune_parameter: ParameterReportLayout? = null
        private var address_parameter: ParameterReportLayout? = null
        private var cap_parameter: ParameterReportLayout? = null
        private var m_dialog: ProgressBar? = null

        init {
            state_parameter = fragmentView.findViewById(R.id.country_parameter)
            region_parameter = fragmentView.findViewById(R.id.region_parameter)
            province_parameter = fragmentView.findViewById(R.id.province_parameter)
            comune_parameter = fragmentView.findViewById(R.id.comune_parameter)
            address_parameter = fragmentView.findViewById(R.id.address_parameter)
            cap_parameter = fragmentView.findViewById(R.id.cap_parameter)
            m_dialog = fragmentView.findViewById(R.id.update_position_progressbar)
        }


        override fun onPreExecute() {
            super.onPreExecute()
            m_dialog!!.visibility = View.VISIBLE
        }

        override fun doInBackground(vararg place: Place): JSONObject? {
            val sb = StringBuilder("https://maps.googleapis.com/maps/api/place/details/json?")
            sb.append("key=")
            sb.append(context.getString(R.string.google_places_api_key))
            sb.append("&placeid=")
            sb.append(place[0].id)
            sb.append("&language=")
            sb.append("it")

            val urlUse = URL(sb.toString())
            val conn: HttpURLConnection?
            conn = urlUse.openConnection() as HttpURLConnection
            conn.requestMethod = "GET"
            conn.connectTimeout = 5000
            val status = conn.responseCode

            when (status) {
                200, 201 -> {
                    //Mi fa cagare ma..
                    val br = BufferedReader(InputStreamReader(conn.inputStream))
                    val sb2 = StringBuilder()
                    var line: String? = null
                    while ({ line = br.readLine(); line }() != null) {
                        sb2.append(line + "\n")
                    }
                    br.close()
                    return JSONObject(sb2.toString())
                }
            }
            return null
        }

        //UI thread
        override fun onPostExecute(result: JSONObject?) {

            if (result == null || result.getString("status").equals("REQUEST_DENIED")) {
                context.toast(R.string.error_parsing_place)
                m_dialog!!.visibility = View.GONE
                return super.onPostExecute(result)
            }

            val addressComponents = result.getJSONObject("result")?.getJSONArray("address_components")
            if (addressComponents != null) {
                for (i in 0 until addressComponents.length()) {
                    val obj = addressComponents.getJSONObject(i)
                    val typesArray = obj.getJSONArray("types")
                    for (j in 0 until typesArray.length()) {
                        val type = typesArray.get(j).toString()
                        if (type.equals("country"))
                            state_parameter?.setParameterValue(obj.getString("long_name"))

                        if (type.equals("administrative_area_level_1"))
                            region_parameter?.setParameterValue(obj.getString("long_name"))

                        if (type.equals("administrative_area_level_2"))
                            province_parameter?.setParameterValue(obj.getString("short_name"))

                        if (type.equals("administrative_area_level_3"))
                            comune_parameter?.setParameterValue(obj.getString("short_name"))

                        if (type.equals("route"))
                            address_parameter?.setParameterValue(obj.getString("long_name"))

                        if (type.equals("postal_code"))
                            cap_parameter?.setParameterValue(obj.getString("long_name"))
                    }
                }
            }
            m_dialog!!.visibility = View.GONE
            return super.onPostExecute(result)
        }
    }
}


