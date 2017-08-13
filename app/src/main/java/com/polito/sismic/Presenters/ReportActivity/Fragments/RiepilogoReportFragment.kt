package com.polito.sismic.Presenters.ReportActivity.Fragments

import android.os.Bundle
import android.support.annotation.Nullable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.polito.sismic.Domain.ReportManager
import com.polito.sismic.R

/**
 * Created by Matteo on 30/07/2017.
 */
class RiepilogoReportFragment : BaseReportFragment() {

    override fun onInitializeParametersForEdit(inflatingView: View, reportManager: ReportManager) {
        return
    }
    override fun getAllViewParameters(): MutableList<Pair<Int, String>> {
        return mutableListOf()
    }

    override fun onCreateView(inflater: LayoutInflater?, @Nullable container: ViewGroup?, @Nullable savedInstanceState: Bundle?): View? {
        return inflateFragment(R.layout.riepilogo_report_layout, inflater, container)
    }
}