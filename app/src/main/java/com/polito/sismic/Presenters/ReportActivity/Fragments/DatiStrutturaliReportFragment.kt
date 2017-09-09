package com.polito.sismic.Presenters.ReportActivity.Fragments

import android.os.Bundle
import android.support.annotation.Nullable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.polito.sismic.Extensions.toast
import com.polito.sismic.Interactors.Helpers.UiMapper
import com.polito.sismic.R
import com.stepstone.stepper.StepperLayout
import kotlinx.android.synthetic.main.dati_strutturali_report_layout.*

/**
 * Created by Matteo on 30/07/2017.
 */
class DatiStrutturaliReportFragment : BaseReportFragment() {

    override fun onCreateView(inflater: LayoutInflater?, @Nullable container: ViewGroup?, @Nullable savedInstanceState: Bundle?): View? {
        return inflateFragment(R.layout.dati_strutturali_report_layout, inflater, container)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fondazioni_type_platea.setOnCheckedChangeListener { _, flag ->
            if (flag)
            {
                fondazioni_type_plinti.isChecked = false
                fondazioni_type_trave.isChecked = false
                fondazioni_type_platea.isClickable = false
            }
            else
            {
                fondazioni_type_platea.isClickable = true
            }
        }

        fondazioni_type_plinti.setOnCheckedChangeListener { _, flag ->
            if (flag)
            {
                fondazioni_type_platea.isChecked = false
                fondazioni_type_trave.isChecked = false
                fondazioni_type_plinti.isClickable = false
            }
            else
            {
                fondazioni_type_plinti.isClickable = true
            }
        }

        fondazioni_type_trave.setOnCheckedChangeListener { _, flag ->
            if (flag)
            {
                fondazioni_type_plinti.isChecked = false
                fondazioni_type_platea.isChecked = false
                fondazioni_type_trave.isClickable = false
            }
            else
            {
                fondazioni_type_trave.isClickable = true
            }
        }

        copertura_type.setOnClickListener { context.toast(R.string.error_not_supported) }
        solaio_type.setOnClickListener { context.toast(R.string.error_not_supported) }
    }

    fun getTipoFondazioni(): Int {

        return when
        {
            fondazioni_type_platea.isChecked -> 0
            fondazioni_type_trave.isChecked -> 1
            fondazioni_type_plinti.isChecked -> 2
            else -> 0
        }
    }

    fun setTipoFondazioni(tipo : Int)
    {
        fondazioni_type_platea.isChecked = false
        fondazioni_type_trave.isChecked  = false
        fondazioni_type_plinti.isChecked  = false

        when (tipo)
        {
            0 -> fondazioni_type_platea.isChecked = true
            1 -> fondazioni_type_trave.isChecked = true
            2 -> fondazioni_type_plinti.isChecked = true
            else -> fondazioni_type_platea.isChecked = true
        }
    }

    fun setPesoSolaioByValue(peso_solaio: String) {

        //iterates all spinners element looking for the category requested
        (0..solaio_peso.count)
                .firstOrNull { solaio_peso.getItemAtPosition(it) == peso_solaio }
                ?.let { return solaio_peso.setSelection(it)}
    }

    fun setPesoCoperturaByValue(peso_copertura: String) {

        //iterates all spinners element looking for the category requested
        (0..copertura_peso.count)
                .firstOrNull { copertura_peso.getItemAtPosition(it) == peso_copertura }
                ?.let { return copertura_peso.setSelection(it)}
    }

    //callback to activity updates domain instance for activity and all existing and future fragments
    override fun onNextClicked(callback: StepperLayout.OnNextClickedCallback?) {
        getReport().reportState.buildingState.structuralState = UiMapper.createStructuralStateForDomain(this)
        super.onNextClicked(callback)
    }

    override fun onParametersInjectedForEdit() {

    }
}