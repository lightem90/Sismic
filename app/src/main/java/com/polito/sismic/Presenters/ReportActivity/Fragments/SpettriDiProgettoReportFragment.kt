package com.polito.sismic.Presenters.ReportActivity.Fragments

import android.os.Bundle
import android.support.annotation.Nullable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Adapter
import android.widget.AdapterView
import com.polito.sismic.Extensions.toast
import com.polito.sismic.R
import kotlinx.android.synthetic.main.spettri_progetto_report_layout.*

class SpettriDiProgettoReportFragment : BaseReportFragment() {

    var q0 : Double = 1.0
    var alfa : Double = 1.0
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
    }

    private fun updateMoltiplicatoreVisibility(pos: Int)
    {
        when (pos) {
            0 -> {
                categoria_moltiplicatore_parameter_container.visibility = View.VISIBLE
            }
            1 -> {
                if (categoria_classe_duttilita_parameter_cda.isChecked)
                    categoria_moltiplicatore_parameter_container.visibility = View.VISIBLE
                else
                    categoria_moltiplicatore_parameter_container.visibility = View.GONE
            }
            else -> {
                categoria_moltiplicatore_parameter_container.visibility = View.GONE
            }
        }
    }

    fun selectCategoriaSuolo(categoria_suolo: String) {
        //iterates all spinners element looking for the category requested
        (0..categoria_suolo_parameter.count)
                .firstOrNull { categoria_suolo_parameter.getItemAtPosition(it).equals(categoria_suolo) }
                ?.let { return categoria_suolo_parameter.setSelection(it)}
    }

    fun selectCategoriaTopografica(categoria_topografica: String)
    {
        //iterates all spinners element looking for the category requested
        (0..categoria_suolo_parameter.count)
                .firstOrNull { categoria_topografica_parameter.getItemAtPosition(it) == categoria_topografica }
                ?.let { return categoria_suolo_parameter.setSelection(it)}
    }
}