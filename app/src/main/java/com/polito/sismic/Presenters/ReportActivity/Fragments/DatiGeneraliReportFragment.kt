package com.polito.sismic.Presenters.ReportActivity.Fragments

import android.os.Bundle
import android.support.annotation.Nullable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.polito.sismic.Interactors.Helpers.UiMapper
import com.polito.sismic.R
import com.stepstone.stepper.StepperLayout
import kotlinx.android.synthetic.main.dati_generali_report_layout.*

/**
 * Created by Matteo on 30/07/2017.
 */
class DatiGeneraliReportFragment : BaseReportFragment() {

    override fun onCreateView(inflater: LayoutInflater?, @Nullable container: ViewGroup?, @Nullable savedInstanceState: Bundle?): View? {
        return inflateFragment(R.layout.dati_generali_report_layout, inflater, container)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    //callback to activity updates domain instance for activity and all existing and future fragments
    override fun onNextClicked(callback: StepperLayout.OnNextClickedCallback?) {
        getReport().reportState.buildingState.buildingGeneralState = UiMapper.createBuildingGeneralStateForDomain(this)
        super.onNextClicked(callback)
    }

    fun setSpinnersByValues(anno_costruzione: String, tipologia_strutturale: String, stato_edificio: String, totale_unita: String)
    {
        val anniArray = context.resources.getStringArray(R.array.anno_costruzione)
        anno_costruzione_parameter.setSelection(anniArray.indexOf(anno_costruzione))

        val tipArray = context.resources.getStringArray(R.array.tipologia_strutturale)
        tipologia_strutturale_parameter.setSelection(tipArray.indexOf(tipologia_strutturale))

        val statoArray = context.resources.getStringArray(R.array.stato_edificio_parameter)
        stato_parameter.setSelection(statoArray.indexOf(stato_edificio))

        val totUniArray = context.resources.getStringArray(R.array.totale_unita_parameter)
        totale_unita_parameter.setSelection(totUniArray.indexOf(totale_unita))
    }
}