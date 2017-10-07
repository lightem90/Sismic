package com.polito.sismic.Presenters.ReportActivity.Fragments

import android.os.Bundle
import android.support.annotation.Nullable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.polito.sismic.Interactors.Helpers.StatiLimite
import com.polito.sismic.Presenters.Adapters.ResultsAdapter
import com.polito.sismic.R

/**
 * Created by Matteo on 30/07/2017.
 */
class RisultatiReportFragment : BaseReportFragment() {

    private lateinit var mAdapter: ResultsAdapter

    override fun onCreateView(inflater: LayoutInflater?, @Nullable container: ViewGroup?, @Nullable savedInstanceState: Bundle?): View? {
        return inflateFragment(R.layout.risultati_report_layout, inflater, container)
    }

    override fun onReload() {
        super.onReload()

        val mrd = getReport().reportState.buildingState.pillarState.pillar_domain?.points?.find { it.label == "MRD" }
        mAdapter = ResultsAdapter(StatiLimite.values().toList(), mrd)
    }
}