package com.polito.sismic.Presenters.Adapters

import android.support.v4.app.FragmentManager
import com.polito.sismic.Presenters.ReportActivity.*
import com.polito.sismic.Presenters.ReportActivity.Fragments.*
import com.stepstone.stepper.Step
import com.stepstone.stepper.adapter.AbstractFragmentStepAdapter
import com.stepstone.stepper.viewmodel.StepViewModel
import com.polito.sismic.R
import com.stepstone.stepper.VerificationError


/**
 * Created by Matteo on 29/07/2017.
 */
class ReportFragmentsAdapter(fragmentManager: FragmentManager, reportActivity: ReportActivity)
    : AbstractFragmentStepAdapter(fragmentManager, reportActivity) {

    override fun getCount(): Int {
        return 10
    }

    //Non serve la factory / provider (in teoria)
    override fun createStep(position: Int): Step {
        when (position)
        {
            0 -> return InfoLocReportFragment()
            1 -> return CatastoReportFragment()
            2 -> return DatiSismoGeneticiReportFragment()
            3 -> return ParametriSismiciReportFragment()
            4 -> return SpettriDiProgettoReportFragment()
            5 -> return DatiGeneraliReportFragment()
            6 -> return DatiStrutturaliReportFragment()
            7 -> return PilastriReportFragment()
            8 -> return RiepilogoReportFragment()
            9 -> return RisultatiReportFragment()
        }
        //MAI
        return InvalidReportFragment()
    }

    override fun getViewModel(position: Int): StepViewModel {
        when (position) {
            0 -> return DecorStep(R.string.info_loc_report_title)
            1 -> return DecorStep(R.string.catasto_report_title)
            2 -> return DecorStep(R.string.dati_sismogenetici_report_title)
            3 -> return DecorStep(R.string.parametri_sismici_report_title)
            4 -> return DecorStep(R.string.spettri_progetto_report_title)
            5 -> return DecorStep(R.string.dati_generali_report_title)
            6 -> return DecorStep(R.string.dati_strutturali_report_title)
            7 -> return DecorStep(R.string.pilastri_report_title)
            8 -> return DecorStep(R.string.riepilogo_report_title)
            9 -> return DecorStep(R.string.risultati_report_title)
        }

        return DecorStep(R.string.error_unsupported)
    }

    private fun DecorStep(id : Int, backStrId : Int = R.string.report_back, nextStrId : Int = R.string.report_next): StepViewModel
    {
       return  StepViewModel.Builder(context)
               .setBackButtonLabel(backStrId)
               .setNextButtonLabel(nextStrId)
               .setTitle(id)
               .create()
    }
}

class InvalidReportFragment : Step {

    override fun verifyStep(): VerificationError {
        throw ExceptionInInitializerError()
    }

    override fun onError(error: VerificationError) {
        throw ExceptionInInitializerError()
    }

    override fun onSelected() {
        throw ExceptionInInitializerError()
    }

}
