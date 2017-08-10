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
class DatiGeneraliReportFragment : BaseReportFragment() {

    override fun getAllViewParameters(): MutableList<Pair<String, Any>> {
        return mutableListOf()
    }

    override fun onCreateView(inflater: LayoutInflater?, @Nullable container: ViewGroup?, @Nullable savedInstanceState: Bundle?): View? {
        return inflateFragment(R.layout.dati_generali_report_layout, inflater, container)
    }
}