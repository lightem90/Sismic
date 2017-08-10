package com.polito.sismic.Presenters.ReportActivity.Fragments

import android.os.Bundle
import android.support.annotation.Nullable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.polito.sismic.R

/**
 * Created by Matteo on 10/08/2017.
 */
class RilieviReportFragment : BaseReportFragment() {


    override fun getAllViewParameters(): MutableList<Pair<String, Object>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onCreateView(inflater: LayoutInflater?, @Nullable container: ViewGroup?, @Nullable savedInstanceState: Bundle?): View? {
        return inflateFragment(R.layout.rilievi_report_layout, inflater, container)
    }
}