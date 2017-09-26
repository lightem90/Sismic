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
import com.polito.sismic.Extensions.toast
import com.polito.sismic.Interactors.Helpers.CategoriaSottosuolo
import com.polito.sismic.Interactors.Helpers.UiMapper
import com.polito.sismic.R
import com.stepstone.stepper.StepperLayout
import kotlinx.android.synthetic.main.spettri_progetto_report_layout.*
import android.widget.ArrayAdapter
import android.widget.Spinner
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.polito.sismic.Domain.ProjectSpectrumState
import com.polito.sismic.Domain.SpectrumDTO
import com.polito.sismic.Extensions.toEntryList
import com.polito.sismic.Interactors.Helpers.CategoriaTopografica


class SpettriDiProgettoReportFragment : BaseReportFragment() {

    interface SpectrumReturnTimeRequest {
        fun onReturnTimesRequested(data: ProjectSpectrumState): List<SpectrumDTO>
    }

    private var mReturnTimeRequest: SpectrumReturnTimeRequest? = null
    override fun onAttach(context: Context?) {

        super.onAttach(context)
        try {
            mReturnTimeRequest = context as SpectrumReturnTimeRequest?
        } catch (e: ClassCastException) {
            throw ClassCastException(context!!.toString() + " must implement OnParametersConfirmed")
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, @Nullable container: ViewGroup?, @Nullable savedInstanceState: Bundle?): View? {
        return inflateFragment(R.layout.spettri_progetto_report_layout, inflater, container)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        regolare_in_altezza.setOnClickListener {
            context.toast(R.string.error_not_supported)
            regolare_in_altezza.isChecked = true
        }

        categoria_classe_duttilita_parameter_cda.setOnCheckedChangeListener { _, flag ->
            if (flag) {
                categoria_classe_duttilita_parameter_cdb.isChecked = false
            } else {
                categoria_classe_duttilita_parameter_cda.isClickable = true
            }
            updateMoltiplicatoreVisibility(categoria_tipologia_parameter.selectedItemPosition)
            updateGraph()
        }

        categoria_classe_duttilita_parameter_cdb.setOnCheckedChangeListener { _, flag ->
            if (flag) {
                categoria_classe_duttilita_parameter_cda.isChecked = false
            } else {
                categoria_classe_duttilita_parameter_cdb.isClickable = true
            }
            updateMoltiplicatoreVisibility(categoria_tipologia_parameter.selectedItemPosition)
            updateGraph()
        }

        categoria_tipologia_parameter.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

            override fun onItemSelected(parent: AdapterView<*>, mView: View?, pos: Int, id: Long) {
                updateMoltiplicatoreVisibility(pos)
                updateGraph()
            }

            override fun onNothingSelected(parent: AdapterView<out Adapter>?) {}
        }

        categoria_moltiplicatore_parameter.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

            override fun onItemSelected(parent: AdapterView<*>, mView: View?, pos: Int, id: Long) {
                updateGraph()
            }

            override fun onNothingSelected(parent: AdapterView<out Adapter>?) {}
        }

        categoria_topografica_parameter.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

            override fun onItemSelected(parent: AdapterView<*>, mView: View?, pos: Int, id: Long) {
                updateGraph()
            }

            override fun onNothingSelected(parent: AdapterView<out Adapter>?) {}
        }

        categoria_suolo_parameter.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

            override fun onItemSelected(parent: AdapterView<*>, mView: View?, pos: Int, id: Long) {
                updateGraph()
            }

            override fun onNothingSelected(parent: AdapterView<out Adapter>?) {}
        }

        with(report_spettrodirisposta_chart)
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

    override fun onReload() {
        super.onReload()
        updateGraph()
    }

    private fun updateGraph()
    {
        val spectrumsDomain = mReturnTimeRequest?.onReturnTimesRequested(UiMapper.createSpectrumStateForDomain(this, getReport().reportState.sismicState.projectSpectrumState.spectrums))
        val spectrumsUi = spectrumsDomain?.map {
            val lds = LineDataSet(it.pointList.toEntryList(), String.format(context.getString(R.string.label_limit_state_format), it.name, it.year))
            lds.color = ContextCompat.getColor(context, it.color)
            lds.lineWidth = 2f
            lds.setDrawCircles(false)
            lds.axisDependency = YAxis.AxisDependency.LEFT
            lds
        }

        spectrumsDomain?.let {
            getReport().reportState.sismicState.projectSpectrumState.spectrums = it
            with(report_spettrodirisposta_chart)
            {
                data = LineData(spectrumsUi)
                invalidate()
            }
        }
    }

    private fun updateMoltiplicatoreVisibility(pos: Int) {
        when (pos) {
            0 -> {
                categoria_moltiplicatore_parameter_container.visibility = View.VISIBLE
                updateSpinnerItemsVibility(categoria_moltiplicatore_parameter, resources.getStringArray(R.array.cat_moltiplicatore_1))
                updateGraph()
            }
            1 -> {
                if (categoria_classe_duttilita_parameter_cda.isChecked) {
                    updateSpinnerItemsVibility(categoria_moltiplicatore_parameter, resources.getStringArray(R.array.cat_moltiplicatore_2))
                } else
                    categoria_moltiplicatore_parameter_container.visibility = View.GONE
                updateGraph()
            }
            else -> {
                categoria_moltiplicatore_parameter_container.visibility = View.GONE
                updateGraph()
            }
        }

    }

    private fun updateSpinnerItemsVibility(spinnerToUpdate: Spinner, newStringArray: Array<out String>) {
        // Creating adapter for spinner
        val dataAdapter = ArrayAdapter<String>(context,
                android.R.layout.simple_spinner_item, newStringArray)

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        // attaching data adapter to spinner
        spinnerToUpdate.adapter = dataAdapter
        updateGraph()
    }

    fun selectCategoriaSuolo(categoria_suolo: String) {

        //iterates all spinners element looking for the category requested
        categoria_suolo_parameter
                .setSelection(CategoriaSottosuolo.values()
                        .indexOfFirst { it.name == categoria_suolo })
    }

    fun selectCategoriaTopografica(categoria_topografica_multiplier: Double) {
        //iterates all spinners element looking for the category requested
        categoria_topografica_parameter
                .setSelection(CategoriaTopografica.values()
                        .indexOfFirst { it.multiplier == categoria_topografica_multiplier })
    }

    //callback to activity updates domain instance for activity and all existing and future fragments
    override fun onNextClicked(callback: StepperLayout.OnNextClickedCallback?) {
        getReport().reportState.sismicState.projectSpectrumState = UiMapper.createSpectrumStateForDomain(this, getReport().reportState.sismicState.projectSpectrumState.spectrums)

        super.onNextClicked(callback)
    }
}