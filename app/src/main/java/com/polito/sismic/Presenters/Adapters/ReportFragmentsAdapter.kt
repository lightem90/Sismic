package com.polito.sismic.Presenters.Adapters

import android.support.v4.app.FragmentManager
import com.polito.sismic.Presenters.CatastoFragment
import com.polito.sismic.Presenters.InfoLocReportFragment
import com.polito.sismic.Presenters.ReportActivity
import com.stepstone.stepper.Step
import com.stepstone.stepper.adapter.AbstractFragmentStepAdapter
import com.stepstone.stepper.viewmodel.StepViewModel
import com.polito.sismic.Presenters.DatiSismoGeneticiFragment
import com.polito.sismic.R


/**
 * Created by Matteo on 29/07/2017.
 */
class ReportFragmentsAdapter(fragmentManager: FragmentManager, reportActivity: ReportActivity)
    : AbstractFragmentStepAdapter(fragmentManager, reportActivity) {

    //TODO 12
    override fun getCount(): Int {
        return 3;
    }

    //TODO
    override fun createStep(position: Int): Step {
        when (position)
        {
            1 -> return InfoLocReportFragment()
            2 -> return CatastoFragment()
            3 -> return DatiSismoGeneticiFragment()
        }
        return InfoLocReportFragment()
    }

    override fun getViewModel(position: Int): StepViewModel {
        when (position)        {

            0 -> return StepViewModel.Builder(context)
                    .setTitle(R.string.info_loc_report_title)
                    .create()

            1 -> return StepViewModel.Builder(context)
                    .setTitle(R.string.catasto_report_title)
                    .create()

            2 -> return StepViewModel.Builder(context)
                    .setTitle(R.string.sismogenetici_report_title)
                    .create()
        }

        return StepViewModel.Builder(context)
                .setTitle("Unsupported")
                .create()
    }
}