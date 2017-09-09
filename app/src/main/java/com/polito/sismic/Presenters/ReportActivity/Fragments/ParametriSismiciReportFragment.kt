package com.polito.sismic.Presenters.ReportActivity.Fragments

import android.os.Bundle
import android.support.annotation.Nullable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Adapter
import android.widget.AdapterView
import com.polito.sismic.Interactors.Helpers.UiMapper
import com.polito.sismic.R
import com.stepstone.stepper.StepperLayout
import kotlinx.android.synthetic.main.parametri_sismici_report_layout.*

/**
 * Created by Matteo on 29/07/2017.
 */
class ParametriSismiciReportFragment : BaseReportFragment() {

    override fun onCreateView(inflater: LayoutInflater?, @Nullable container: ViewGroup?, @Nullable savedInstanceState: Bundle?): View? {
        return inflateFragment(R.layout.parametri_sismici_report_layout, inflater, container)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        vita_nominale_30.setOnCheckedChangeListener { _, flag ->
            if (flag)
            {
                vita_nominale_50.isChecked = false
                vita_nominale_100.isChecked = false
                setVitaReale(classe_parameter.selectedItemPosition)
                vita_nominale_30.isClickable = false
            }
            else
            {
                vita_nominale_30.isClickable = true
            }
        }

        vita_nominale_50.setOnCheckedChangeListener { _, flag ->
            if (flag)
            {
                vita_nominale_30.isChecked = false
                vita_nominale_100.isChecked = false
                setVitaReale(classe_parameter.selectedItemPosition)
                vita_nominale_50.isClickable = false
            }
            else
            {
                vita_nominale_50.isClickable = true
            }
        }

        vita_nominale_100.setOnCheckedChangeListener { _, flag ->
            if (flag)
            {
                vita_nominale_30.isChecked = false
                vita_nominale_50.isChecked = false
                setVitaReale(classe_parameter.selectedItemPosition)
                vita_nominale_100.isClickable = false
            }
            else
            {
                vita_nominale_100.isClickable = true
            }
        }

        classe_parameter.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

            override fun onItemSelected(parent: AdapterView<*>, mView: View?, pos: Int, id: Long) {
                setVitaReale(pos)
            }
            override fun onNothingSelected(parent: AdapterView<out Adapter>?) {  }
        }
    }

    private fun setVitaReale(classeUso : Int)
    {
        vita_reale.setValue((getClasseUsoForIndex(classeUso) * getVitaNominale()).toString())
    }

    fun getClasseUsoForIndex(index : Int) : Double
    {
        when(index)
        {
            0 -> return 0.7
            1 -> return 1.0
            2 -> return 1.5
            3 -> return 2.0
        }

        return -1.0
    }

    fun getClasseUsoIndexByValue(classeValue : Double) : Int
    {
        return when (classeValue)
        {
            0.7 -> 0
            1.0 -> 1
            1.5 -> 2
            2.0 -> 3
            else -> 0
        }
    }

    fun getVitaNominale() : Int
    {
        if (vita_nominale_30.isChecked) return 30
        if (vita_nominale_50.isChecked) return 50
        if (vita_nominale_100.isChecked) return 100
        return 30
    }

    fun setVitaNominale(vita : Int)
    {
        vita_nominale_30.isChecked = false
        vita_nominale_50.isChecked = false
        vita_nominale_100.isChecked = false

        when (vita) {
            30 ->  vita_nominale_30.isChecked = true
            50 ->  vita_nominale_50.isChecked = true
            100 -> vita_nominale_100.isChecked = true
            else -> vita_nominale_30.isChecked = true
        }
    }

    override fun onNextClicked(callback: StepperLayout.OnNextClickedCallback?) {
        getReport().reportState.sismicState.sismicParametersState = UiMapper.createSismicStateForDomain(this)
        super.onNextClicked(callback)
    }
}

