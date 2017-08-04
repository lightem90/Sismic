package com.polito.sismic.Presenters.ReportActivity.Fragments

import android.content.Intent
import android.os.Bundle
import android.support.annotation.Nullable
import android.view.*
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true);
    }



    override fun onCreateView(inflater: LayoutInflater?, @Nullable container: ViewGroup?, @Nullable savedInstanceState: Bundle?): View? {

        var view = inflateFragment(R.layout.info_loc_report_layout, inflater, container)

        //Da inizializzare coi bottoni
        mLatParameter = view?.findViewById<ParameterReportLayout>(R.id.lat_parameter)
        mLonParameter = view?.findViewById<ParameterReportLayout>(R.id.long_parameter)

        //Da inizializzare accedendo al server
        mZonaSismicaParameter = view?.findViewById<ParameterReportLayout>(R.id.zona_sismica_parameter)
        mCodiceIstatParameter = view?.findViewById<ParameterReportLayout>(R.id.codice_istat_parameter)


        return view;
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item != null) {
            when (item.itemId)
            {
                R.id.reverseGeolocalization -> return true
                R.id.geolocalization -> return true
                R.id.fromMap -> return true
            }
        }
        return false;
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.localization_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        //TODO Gestire le varie azioni da toolbar
    }

}

