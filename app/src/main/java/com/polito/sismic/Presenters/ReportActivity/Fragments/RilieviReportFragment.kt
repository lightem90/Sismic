package com.polito.sismic.Presenters.ReportActivity.Fragments

import android.os.Bundle
import android.support.annotation.Nullable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Adapter
import android.widget.AdapterView
import com.polito.sismic.Extensions.toast
import com.polito.sismic.Interactors.Helpers.UiMapper
import com.polito.sismic.R
import com.stepstone.stepper.StepperLayout
import kotlinx.android.synthetic.main.rilievi_report_layout.*

/**
 * Created by Matteo on 10/08/2017.
 */
class RilieviReportFragment : BaseReportFragment() {

    override fun onCreateView(inflater: LayoutInflater?, @Nullable container: ViewGroup?, @Nullable savedInstanceState: Bundle?): View? {
        return inflateFragment(R.layout.rilievi_report_layout, inflater, container)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {

        regolare_in_altezza.setOnClickListener {
            context.toast(R.string.error_not_supported)
            regolare_in_altezza.isChecked = true
        }

        piani_numero_parameter.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

            override fun onItemSelected(parent: AdapterView<*>, mView: View?, pos: Int, id: Long) {
                if (pos > 0)
                    altezza_piani_sup_parameter.visibility = View.VISIBLE
                else
                    altezza_piani_sup_parameter.visibility = View.GONE

                updateAltezzaTotale()
            }
            override fun onNothingSelected(parent: AdapterView<out Adapter>?) {  }
        }

        altezza_piano_tr_parameter.attachDataConfirmedCallback { updateAltezzaTotale() }
        altezza_piani_sup_parameter.attachDataConfirmedCallback { updateAltezzaTotale() }

    }

    private fun updateAltezzaTotale()
    {
        altezza_tot.text  = if (piani_numero_parameter.selectedItemPosition > 0)
            (altezza_piano_tr_parameter.getParameterValue().toDouble() + piani_numero_parameter.selectedItemPosition * altezza_piani_sup_parameter.getParameterValue().toDouble()).toString()
            else altezza_piano_tr_parameter.getParameterValue()
    }

    //callback to activity updates domain instance for activity and all existing and future fragments
    override fun onNextClicked(callback: StepperLayout.OnNextClickedCallback?) {
        getReport().reportState.buildingState.takeoverState = UiMapper.createTakeoverStateForDomain(this)
        super.onNextClicked(callback)
    }
}