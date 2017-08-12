package com.polito.sismic.Presenters.ReportActivity.Fragments

import android.os.Bundle
import android.support.annotation.Nullable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.polito.sismic.Domain.ReportManager
import com.polito.sismic.R
import kotlinx.android.synthetic.main.catasto_report_layout.*

/**
 * Created by Matteo on 29/07/2017.
 */
class CatastoReportFragment : BaseReportFragment() {

    override fun onInitializeParametersForEdit(inflatingView: View, reportManager: ReportManager) {

    }

    override fun getAllViewParameters(): MutableList<Pair<String, String>> {
        return mutableListOf(
                Pair(foglio_parameter.id.toString(), foglio_parameter.getParameterValue()),
                Pair(mappale_parameter.id.toString(), mappale_parameter.getParameterValue()),
                Pair(particella_parameter.id.toString(), particella_parameter.getParameterValue()),
                Pair(foglio_cart_parameter.id.toString(), foglio_cart_parameter.getParameterValue()),
                Pair(edificio_parameter.id.toString(), edificio_parameter.getParameterValue()),
                Pair(aggr_str_parameter.id.toString(), aggr_str_parameter.getParameterValue()),
                Pair(zona_urb_parameter.id.toString(), zona_urb_parameter.getParameterValue()),
                Pair(piano_urb_parameter.id.toString(), piano_urb_parameter.getParameterValue()),
                Pair(vincoli_urb_parameter.id.toString(), vincoli_urb_parameter.getParameterValue())
        )
    }

    override fun onCreateView(inflater: LayoutInflater?, @Nullable container: ViewGroup?, @Nullable savedInstanceState: Bundle?): View? {
        return inflateFragment(R.layout.catasto_report_layout, inflater, container)
    }


}