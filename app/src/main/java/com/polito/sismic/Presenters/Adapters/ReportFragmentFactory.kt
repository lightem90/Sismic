package com.polito.sismic.Presenters.Adapters

import android.content.Context
import android.os.Bundle
import com.polito.sismic.Domain.ReportManager
import com.polito.sismic.Presenters.ReportActivity.Fragments.*
import com.polito.sismic.R
import com.stepstone.stepper.Step
import com.stepstone.stepper.viewmodel.StepViewModel

class ReportFragmentFactory(private val reportManager: ReportManager) {

    private val mInfoLocReportFragment : InfoLocReportFragment = InfoLocReportFragment()
    private val mCatastoReportFragment : CatastoReportFragment = CatastoReportFragment()
    private val mDatiSismoGeneticiReportFragment : DatiSismoGeneticiReportFragment = DatiSismoGeneticiReportFragment()
    private val mParametriSismiciReportFragment : ParametriSismiciReportFragment = ParametriSismiciReportFragment()
    private val mSpettriDiProgettoReportFragment : SpettriDiProgettoReportFragment = SpettriDiProgettoReportFragment()
    private val mDatiGeneraliReportFragment : DatiGeneraliReportFragment = DatiGeneraliReportFragment()
    private val mDatiStrutturaliReportFragment : DatiStrutturaliReportFragment = DatiStrutturaliReportFragment()
    private val mRilieviReportFragment : RilieviReportFragment = RilieviReportFragment()
    private val mPilastriReportFragment : PilastriReportFragment = PilastriReportFragment()
    private val mRiepilogoReportFragment : RiepilogoReportFragment = RiepilogoReportFragment()
    private val mRisultatiReportFragment : RisultatiReportFragment = RisultatiReportFragment()

    fun createFragmentForPosition(pos : Int) : Step
    {
        when (pos)
        {
            0 -> return pushFragment(mInfoLocReportFragment)
            1 -> return pushFragment(mCatastoReportFragment)
            2 -> return pushFragment(mDatiSismoGeneticiReportFragment)
            3 -> return pushFragment(mParametriSismiciReportFragment)
            4 -> return pushFragment(mSpettriDiProgettoReportFragment)
            5 -> return pushFragment(mDatiGeneraliReportFragment)
            6 -> return pushFragment(mDatiStrutturaliReportFragment)
            //TODO: sezione 6? Chiarimenti sulle restanti!
            7 -> return pushFragment(mRilieviReportFragment)
            8 -> return pushFragment(mPilastriReportFragment)
            9 -> return pushFragment(mRiepilogoReportFragment)
            10 -> return pushFragment(mRisultatiReportFragment)
        }
        //MAI
        return InvalidReportFragment()
    }

    private fun pushFragment(fragment: BaseReportFragment): Step {

        //TODO: handle edit!
        //var bundle = Bundle()
        //bundle.putParcelable("report", reportManager.getParcelable())
        //fragment.arguments = bundle

        return fragment
    }

    fun fragmentCount() : Int
    {
        return 11;
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
            7 -> return DecorStep(R.string.rilievi_report_title, context)
            8 -> return DecorStep(R.string.pilastri_report_title, context)
            9 -> return DecorStep(R.string.riepilogo_report_title, context)
            10 -> return DecorStep(R.string.risultati_report_title, context)
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