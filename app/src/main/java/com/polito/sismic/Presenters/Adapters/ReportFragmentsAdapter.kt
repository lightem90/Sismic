package com.polito.sismic.Presenters.Adapters

import android.support.v4.app.FragmentManager
import com.polito.sismic.Presenters.InfoLocReportFragment
import com.polito.sismic.Presenters.ReportActivity
import com.stepstone.stepper.Step
import com.stepstone.stepper.adapter.AbstractFragmentStepAdapter

/**
 * Created by Matteo on 29/07/2017.
 */
class ReportFragmentsAdapter(fragmentManager: FragmentManager, reportActivity: ReportActivity)
    : AbstractFragmentStepAdapter(fragmentManager, reportActivity) {

    //TODO 12
    override fun getCount(): Int {
        return 1;
    }

    //TODO
    override fun createStep(position: Int): Step {
        when (position)
        {
            1 -> return InfoLocReportFragment()
        }
        return InfoLocReportFragment()
    }
}