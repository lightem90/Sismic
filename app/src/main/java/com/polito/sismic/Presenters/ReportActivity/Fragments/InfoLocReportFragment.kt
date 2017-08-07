package com.polito.sismic.Presenters.ReportActivity.Fragments

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.location.Location
import android.os.AsyncTask
import android.os.Bundle
import android.support.annotation.Nullable
import android.view.*
import android.widget.ProgressBar
import com.google.android.gms.location.places.Place
import com.polito.sismic.Extensions.toast
import com.polito.sismic.Interactors.Helpers.ActionHelper
import com.polito.sismic.Interactors.Helpers.ActionType
import com.polito.sismic.Interactors.Helpers.PermissionsHelper
import com.polito.sismic.Presenters.CustomLayout.ParameterReportLayout
import com.polito.sismic.R
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

    private var  mLocationCallback: InfoLocReportFragment.OnCurrentLocationProvided? = null
    private var  mActionHelper = ActionHelper()
    private var  mPermissionHelper = PermissionsHelper()

    // Container Activity must implement this interface
    interface OnCurrentLocationProvided {
        fun onLocationAcquired(location: Location)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mPermissionHelper.checAndAskLocationPermissions(activity, this)
        setHasOptionsMenu(true)
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
                    if (havePermission()) mActionHelper.handleActionRequest(ActionType.ReverseLocalization, this, null)
                    return true
                }

                R.id.geolocalization ->
                {
                    if (havePermission()) mActionHelper.handleActionRequest(ActionType.Localization, this, mLocationCallback)
                    return true
                }

                R.id.fromMap ->
                {
                    if (havePermission()) mActionHelper.handleActionRequest(ActionType.PlacePicker, this, null)
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

    fun updateByPlace(place : Place?)
    {
        if (place != null)
        {
            lat_parameter.setParameterValue("%.4f".format(place.latLng.latitude))
            long_parameter.setParameterValue("%.4f".format(place.latLng.longitude))

            //TODO asynctask per le altre info?
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

    class PlaceDetailsTask(fragmentView: View, val context: Context) : AsyncTask<Place, Int, JSONObject>()
    {
        private var  state_parameter: ParameterReportLayout? = null
        private var  region_parameter: ParameterReportLayout? = null
        private var  province_parameter: ParameterReportLayout? = null
        private var  comune_parameter: ParameterReportLayout? = null
        private var  address_parameter: ParameterReportLayout? = null
        private var  cap_parameter: ParameterReportLayout? = null
        private var  m_dialog : ProgressBar? = null

        init {
            state_parameter = fragmentView.findViewById<ParameterReportLayout>(R.id.state_parameter)
            region_parameter = fragmentView.findViewById<ParameterReportLayout>(R.id.region_parameter)
            province_parameter = fragmentView.findViewById<ParameterReportLayout>(R.id.province_parameter)
            comune_parameter = fragmentView.findViewById<ParameterReportLayout>(R.id.comune_parameter)
            address_parameter = fragmentView.findViewById<ParameterReportLayout>(R.id.address_parameter)
            cap_parameter = fragmentView.findViewById<ParameterReportLayout>(R.id.cap_parameter)
            m_dialog = fragmentView.findViewById<ProgressBar>(R.id.update_position_progressbar)
        }


        override fun onPreExecute() {
            super.onPreExecute()
            m_dialog!!.visibility = View.VISIBLE
        }

        override fun doInBackground(vararg place: Place): JSONObject? {
            val sb = StringBuilder("https://maps.googleapis.com/maps/api/place/details/json?")
            sb.append("key=")
            //RICORDATI! LA CHIAVE WEB VA CON L'INDIRIZZO DEL SERVER! (da https://console.developers.google.com/)
            sb.append(context.getString(R.string.google_places_api_key))
            sb.append("&placeid=")
            sb.append(place[0].id)
            sb.append("&language=")
            sb.append("it")

            val urlUse = URL(sb.toString())
            var conn: HttpURLConnection?
            conn = urlUse.openConnection() as HttpURLConnection
            conn!!.setRequestMethod("GET")
            val status = conn.responseCode

            when (status) {
                200, 201 -> {
                    //Mi fa cagare ma..
                    val br = BufferedReader(InputStreamReader(conn.inputStream))
                    val sb2 = StringBuilder()
                    var line: String? = null;
                    while ({ line = br.readLine(); line }() != null) {
                        sb2.append(line + "\n")
                    }
                    br.close()
                    return JSONObject(sb2.toString())
                }
            }
            return null;
        }

        //UI thread
        override fun onPostExecute(result: JSONObject?) {

            if (result == null || result.getString("status").equals("REQUEST_DENIED"))
            {
                context.toast(R.string.error_parsing_place)
                return super.onPostExecute(result)
            }

            var addressComponents = result.getJSONObject("result")?.getJSONArray("address_components")
            if (addressComponents != null) {
                for(i in 0 until addressComponents.length())
                {
                    var obj = addressComponents.getJSONObject(i)
                    val typesArray = obj.getJSONArray("types")
                    for(j in 0 until typesArray.length()) {
                        var type = typesArray.get(j).toString()
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



