package com.polito.sismic.Presenters.ReportActivity.Fragments

import android.os.Bundle
import android.support.annotation.Nullable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.polito.sismic.R
import com.stepstone.stepper.StepperLayout
import kotlinx.android.synthetic.main.catasto_report_layout.*

/**
 * Created by Matteo on 29/07/2017.
 */
class CatastoReportFragment : BaseReportFragment() {

    override fun onCreateView(inflater: LayoutInflater?, @Nullable container: ViewGroup?, @Nullable savedInstanceState: Bundle?): View? {
        return inflateFragment(R.layout.catasto_report_layout, inflater, container)
    }

    private fun showProgress()
    {
        progress.visibility = View.VISIBLE
        catasto_container.visibility = View.GONE
    }

    private fun hideProgress()
    {
        progress.visibility = View.GONE
        catasto_container.visibility = View.VISIBLE
    }

    override fun onNextClicked(callback: StepperLayout.OnNextClickedCallback?) {
        showProgress()
        mNodeRequestCallback?.onClosedNodesCalculationRequested()
        hideProgress()
        super.onNextClicked(callback)
    }
}