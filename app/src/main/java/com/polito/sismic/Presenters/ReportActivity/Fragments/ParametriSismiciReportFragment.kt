package com.polito.sismic.Presenters.ReportActivity.Fragments

import android.content.Context
import android.os.Bundle
import android.support.annotation.Nullable
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Adapter
import android.widget.AdapterView
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.polito.sismic.Domain.SismicParametersState
import com.polito.sismic.Domain.SpectrumDTO
import com.polito.sismic.Extensions.toEntryList
import com.polito.sismic.Interactors.Helpers.ClasseUso
import com.polito.sismic.Interactors.Helpers.UiMapper
import com.polito.sismic.R
import com.stepstone.stepper.StepperLayout
import kotlinx.android.synthetic.main.parametri_sismici_report_layout.*

/**
 * Created by Matteo on 29/07/2017.
 */
class ParametriSismiciReportFragment : BaseReportFragment() {

    interface LimitStateRequest {
        fun onLimitStatesRequested(data : SismicParametersState) : List<SpectrumDTO>
    }

    private var mLimitStateRequest: LimitStateRequest? = null
    override fun onAttach(context: Context?) {

        super.onAttach(context)
        try {
            mLimitStateRequest = context as LimitStateRequest?
        } catch (e: ClassCastException) {
            throw ClassCastException(context!!.toString() + " must implement OnParametersConfirmed")
        }
    }

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
            updateGraph()
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
            updateGraph()
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
            updateGraph()
        }


        classe_parameter.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

            override fun onItemSelected(parent: AdapterView<*>, mView: View?, pos: Int, id: Long) {
                setVitaReale(pos)
                updateGraph()
            }
            override fun onNothingSelected(parent: AdapterView<out Adapter>?) {  }
        }
        with (report_spettrodirisposta_chart)
        {
            xAxis.position = XAxis.XAxisPosition.BOTTOM
            xAxis.axisMaximum = 4.0f
            xAxis.axisMinimum = 0.0f
            legend.verticalAlignment = Legend.LegendVerticalAlignment.BOTTOM
            legend.setDrawInside(false)
            description.isEnabled = false
            getAxis(YAxis.AxisDependency.RIGHT).isEnabled = false
        }
    }

    private fun updateGraph()
    {
        val spectrumsDomain = mLimitStateRequest?.onLimitStatesRequested(UiMapper.createSismicStateForDomain(this, listOf()))
        val spectrumsUi = spectrumsDomain?.map {
            val lds = LineDataSet(it.pointList.toEntryList(), String.format(context.getString(R.string.label_limit_state_format), it.name, it.year))
            lds.color = ContextCompat.getColor(context, it.color)
            lds.lineWidth = 2f
            lds.setDrawCircles(false)
            lds.axisDependency = YAxis.AxisDependency.LEFT
            lds
        }

        spectrumsDomain?.let {
            getReport().reportState.sismicState.sismicParametersState.spectrums = it
            with(report_spettrodirisposta_chart)
            {
                data = LineData(spectrumsUi)
                invalidate()
            }
        }

    }

    private fun setVitaReale(classeUso : Int)
    {
        vita_reale.setValue((getClasseUsoForIndex(classeUso) * getVitaNominale()).toString())
    }

    fun getClasseUsoForIndex(index : Int) : Double
    {
        return ClasseUso.values()[index].multiplier
    }

    fun getClasseUsoIndexByValue(classeValue : Double) : Int
    {
        return ClasseUso.values().indexOfFirst { it.multiplier == classeValue }
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
        getReport().reportState.sismicState.sismicParametersState = UiMapper.createSismicStateForDomain(this, getReport().reportState.sismicState.sismicParametersState.spectrums)
        super.onNextClicked(callback)
    }
}

