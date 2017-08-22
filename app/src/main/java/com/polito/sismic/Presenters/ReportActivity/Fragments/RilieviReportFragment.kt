package com.polito.sismic.Presenters.ReportActivity.Fragments

import android.os.Bundle
import android.support.annotation.Nullable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.polito.sismic.Extensions.toast
import com.polito.sismic.R
import kotlinx.android.synthetic.main.rilievi_report_layout.*

/**
 * Created by Matteo on 10/08/2017.
 */
class RilieviReportFragment : BaseReportFragment() {

    override fun onCreateView(inflater: LayoutInflater?, @Nullable container: ViewGroup?, @Nullable savedInstanceState: Bundle?): View? {
        return inflateFragment(R.layout.rilievi_report_layout, inflater, container)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {

        regolare_in_altezza.setOnClickListener { context.toast(R.string.error_not_supported) }
    }
}