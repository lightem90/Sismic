package com.polito.sismic.Presenters.Adapters

import android.content.Context
import android.os.Bundle
import com.polito.sismic.Domain.ReportManager
import com.polito.sismic.Presenters.ReportActivity.Fragments.*
import com.polito.sismic.R
import com.stepstone.stepper.Step
import com.stepstone.stepper.viewmodel.StepViewModel

class ReportFragmentFactory(private val reportManager: ReportManager) {

    private val InfoLocReportFragment : InfoLocReportFragment = InfoLocReportFragment()
    private val CatastoReportFragment : CatastoReportFragment = CatastoReportFragment()
    private val DatiSismoGeneticiReportFragment : DatiSismoGeneticiReportFragment = DatiSismoGeneticiReportFragment()
    private val ParametriSismiciReportFragment : ParametriSismiciReportFragment = ParametriSismiciReportFragment()
    private val SpettriDiProgettoReportFragment : SpettriDiProgettoReportFragment = SpettriDiProgettoReportFragment()
    private val DatiGeneraliReportFragment : DatiGeneraliReportFragment = DatiGeneraliReportFragment()
    private val DatiStrutturaliReportFragment : DatiStrutturaliReportFragment = DatiStrutturaliReportFragment()
    private val PilastriReportFragment : PilastriReportFragment = PilastriReportFragment()
    private val RiepilogoReportFragment : RiepilogoReportFragment = RiepilogoReportFragment()
    private val RisultatiReportFragment : RisultatiReportFragment = RisultatiReportFragment()

    fun createFragmentForPosition(pos : Int) : Step
    {
        when (pos)
        {
            0 -> return pushFragment(InfoLocReportFragment)
            1 -> return pushFragment(CatastoReportFragment)
            2 -> return pushFragment(DatiSismoGeneticiReportFragment)
            3 -> return pushFragment(ParametriSismiciReportFragment)
            4 -> return pushFragment(SpettriDiProgettoReportFragment)
            5 -> return pushFragment(DatiGeneraliReportFragment)
            6 -> return pushFragment(DatiStrutturaliReportFragment)
            7 -> return pushFragment(PilastriReportFragment)
            8 -> return pushFragment(RiepilogoReportFragment)
            9 -> return pushFragment(RisultatiReportFragment)
        }
        //MAI
        return InvalidReportFragment()
    }

    private fun  pushFragment(fragment: BaseReportFragment): Step {

        var bundle = Bundle()
        bundle.putParcelable("report", reportManager.DTO)
        fragment.arguments = bundle
        return fragment
    }

    fun fragmentCount() : Int
    {
        return 10;
    }

    fun createDecoratorForStep(pos: Int, context: Context) : StepViewModel
    {
        when (pos) {
            0 -> return DecorStep(R.string.info_loc_report_title, context)
            1 -> return DecorStep(R.string.catasto_report_title, context)
            2 -> return DecorStep(R.string.dati_sismogenetici_report_title, context)
            3 -> return DecorStep(R.string.parametri_sismici_report_title, context)
            4 -> return DecorStep(R.string.spettri_progetto_report_title, context)
            5 -> return DecorStep(R.string.dati_generali_report_title, context)
            6 -> return DecorStep(R.string.dati_strutturali_report_title, context)
            7 -> return DecorStep(R.string.pilastri_report_title, context)
            8 -> return DecorStep(R.string.riepilogo_report_title, context)
            9 -> return DecorStep(R.string.risultati_report_title, context)
        }

        return DecorStep(R.string.error_unsupported, context)
    }

    private fun DecorStep(id : Int, context: Context, backStrId : Int = R.string.report_back, nextStrId : Int = R.string.report_next): StepViewModel
    {
        return  StepViewModel.Builder(context)
                .setBackButtonLabel(backStrId)
                .setEndButtonLabel(nextStrId)
                .setTitle(id)
                .create()
    }
}