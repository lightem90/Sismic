package com.polito.sismic.Presenters.Adapters

import android.support.v4.app.FragmentManager
import com.polito.sismic.Presenters.ReportActivity.*
import com.polito.sismic.Presenters.ReportActivity.Fragments.*
import com.stepstone.stepper.Step
import com.stepstone.stepper.adapter.AbstractFragmentStepAdapter
import com.stepstone.stepper.viewmodel.StepViewModel
import com.polito.sismic.R


/**
 * Created by Matteo on 29/07/2017.
 */
class ReportFragmentsAdapter(fragmentManager: FragmentManager, reportActivity: ReportActivity)
    : AbstractFragmentStepAdapter(fragmentManager, reportActivity) {

    override fun getCount(): Int {
        return 5
    }

    //TODO
    //Non serve la factory / provider (in teoria)
    override fun createStep(position: Int): Step {
        when (position)
        {
            0 -> return InfoLocReportFragment()
            1 -> return CatastoFragment()
            2 -> return DatiSismoGeneticiFragment()
            3 -> return ParametriSismiciFragment()
            4 -> return SpettriDiProgettoFragment()
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

            3 -> return StepViewModel.Builder(context)
                    .setTitle(R.string.parametri_sismici_title)
                    .create()

            4 -> return StepViewModel.Builder(context)
                    .setTitle(R.string.spettri_progetto_title)
                    .create()

        }

        return StepViewModel.Builder(context)
                .setTitle("Unsupported")
                .create()
    }
}