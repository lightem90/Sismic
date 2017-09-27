package com.polito.sismic.Presenters.ReportActivity.Fragments

import android.os.Bundle
import android.support.annotation.Nullable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.polito.sismic.Extensions.hideSoftKeyboard
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

        anno_costruzione_parameter.attachDataConfirmedCallback {
            tipologia_strutturale_parameter.requestFocus()
        }

        tipologia_strutturale_parameter.attachDataConfirmedCallback {
            stato_parameter.requestFocus()
        }

        stato_parameter.attachDataConfirmedCallback {
            totale_unita_parameter.requestFocus()
        }

        totale_unita_parameter.attachDataConfirmedCallback {
            activity.hideSoftKeyboard()
        }
    }

    //callback to activity updates domain instance for activity and all existing and future fragments
    override fun onNextClicked(callback: StepperLayout.OnNextClickedCallback?) {
        getReport().reportState.buildingState.buildingGeneralState = UiMapper.createBuildingGeneralStateForDomain(this)
        super.onNextClicked(callback)
    }
}