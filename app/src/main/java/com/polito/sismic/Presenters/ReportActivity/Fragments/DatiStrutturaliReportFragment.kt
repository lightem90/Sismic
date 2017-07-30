package com.polito.sismic.Presenters.ReportActivity.Fragments

import android.os.Bundle
import android.support.annotation.Nullable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.polito.sismic.R

/**
 * Created by Matteo on 30/07/2017.
 */
class DatiStrutturaliReportFragment : BaseReportFragment() {

    override fun onCreateView(inflater: LayoutInflater?, @Nullable container: ViewGroup?, @Nullable savedInstanceState: Bundle?): View? {
        var view = inflateFragment(R.layout.dati_strutturali_report_layout, inflater, container);
        /*
         *  Do things related to this fragment
         */
        return view;
    }
}