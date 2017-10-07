package com.polito.sismic.AsyncTasks

import android.content.Context
import android.os.AsyncTask
import android.view.View
import android.widget.ProgressBar
import com.google.android.gms.location.places.Place
import com.polito.sismic.Extensions.toast
import com.polito.sismic.Presenters.CustomLayout.ParameterReportLayout
import com.polito.sismic.R
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

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

        if (result == null || result.getString("status").equals("REQUEST_DENIED"))
        {
            context.toast(R.string.error_parsing_place)
            m_dialog!!.visibility = View.GONE
            return super.onPostExecute(result)
        }

        val addressComponents = result.getJSONObject("result")?.getJSONArray("address_components")
        if (addressComponents != null) {
            for(i in 0 until addressComponents.length())
            {
                val obj = addressComponents.getJSONObject(i)
                val typesArray = obj.getJSONArray("types")
                for(j in 0 until typesArray.length()) {
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