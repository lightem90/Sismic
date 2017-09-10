package com.polito.sismic.Presenters.ReportActivity.Fragments

import android.os.Bundle
import android.support.annotation.Nullable
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


class SpettriDiProgettoReportFragment : BaseReportFragment() {

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
            if (flag)
            {
                categoria_classe_duttilita_parameter_cdb.isChecked = false
            }
            else
            {
                categoria_classe_duttilita_parameter_cda.isClickable = true
            }
            updateMoltiplicatoreVisibility(categoria_tipologia_parameter.selectedItemPosition)
        }

        categoria_classe_duttilita_parameter_cdb.setOnCheckedChangeListener { _, flag ->
            if (flag)
            {
                categoria_classe_duttilita_parameter_cda.isChecked = false
            }
            else
            {
                categoria_classe_duttilita_parameter_cdb.isClickable = true
            }
            updateMoltiplicatoreVisibility(categoria_tipologia_parameter.selectedItemPosition)
        }

        categoria_tipologia_parameter.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

            override fun onItemSelected(parent: AdapterView<*>, mView: View?, pos: Int, id: Long) {
                updateMoltiplicatoreVisibility(pos)
            }
            override fun onNothingSelected(parent: AdapterView<out Adapter>?) {  }
        }

        categoria_moltiplicatore_parameter.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

            override fun onItemSelected(parent: AdapterView<*>, mView: View?, pos: Int, id: Long) {
            }
            override fun onNothingSelected(parent: AdapterView<out Adapter>?) {  }
        }
    }

    private fun updateMoltiplicatoreVisibility(pos: Int)
    {
        when (pos) {
            0 -> {
                categoria_moltiplicatore_parameter_container.visibility = View.VISIBLE
                updateSpinnerItemsVibility(categoria_moltiplicatore_parameter, resources.getStringArray(R.array.cat_moltiplicatore_1))

            }
            1 -> {
                if (categoria_classe_duttilita_parameter_cda.isChecked)
                {
                    updateSpinnerItemsVibility(categoria_moltiplicatore_parameter, resources.getStringArray(R.array.cat_moltiplicatore_2))
                }
                else
                    categoria_moltiplicatore_parameter_container.visibility = View.GONE
            }
            else -> {
                categoria_moltiplicatore_parameter_container.visibility = View.GONE
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
    }

    fun selectCategoriaSuolo(categoria_suolo: Double) {

        //iterates all spinners element looking for the category requested
        categoria_suolo_parameter
                .setSelection(CategoriaSottosuolo.values()
                        .indexOfFirst { it.multiplier == categoria_suolo })
    }

    fun selectCategoriaTopografica(categoria_topografica: Double)
    {
        //iterates all spinners element looking for the category requested
        categoria_topografica_parameter
                .setSelection(CategoriaSottosuolo.values()
                        .indexOfFirst { it.multiplier == categoria_topografica })
    }

    //callback to activity updates domain instance for activity and all existing and future fragments
    override fun onNextClicked(callback: StepperLayout.OnNextClickedCallback?) {
        getReport().reportState.sismicState.projectSpectrumState = UiMapper.createSpectrumStateForDomain(this)
        super.onNextClicked(callback)
    }
}