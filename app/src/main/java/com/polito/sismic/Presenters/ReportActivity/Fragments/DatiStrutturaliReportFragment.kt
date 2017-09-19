package com.polito.sismic.Presenters.ReportActivity.Fragments

import android.os.Bundle
import android.support.annotation.Nullable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Adapter
import android.widget.AdapterView
import com.polito.sismic.Extensions.toDoubleOrZero
import com.polito.sismic.Extensions.toast
import com.polito.sismic.Interactors.Helpers.UiMapper
import com.polito.sismic.R
import com.stepstone.stepper.StepperLayout
import kotlinx.android.synthetic.main.dati_strutturali_report_layout.*

/**
 * Created by Matteo on 30/07/2017.
 */
class DatiStrutturaliReportFragment : BaseReportFragment() {

    private lateinit var mPesiSolaio: Array<out String>
    private lateinit var mPesiCopertura: Array<out String>

    override fun onCreateView(inflater: LayoutInflater?, @Nullable container: ViewGroup?, @Nullable savedInstanceState: Bundle?): View? {
        return inflateFragment(R.layout.dati_strutturali_report_layout, inflater, container)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mPesiSolaio = context.resources.getStringArray(R.array.solaio_int_pesi)
        mPesiCopertura = context.resources.getStringArray(R.array.copertura_int_pesi)

        //TODO logic to hide container when piani <= 1
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

        //Set g1 according to weigth (Foreach copertura/solaio there's the specified weigth at position)
        solaio_peso.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

            override fun onItemSelected(parent: AdapterView<*>, mView: View?, pos: Int, id: Long) {
                solaio_g1.text = context.resources.getStringArray(R.array.solaio_int_pesi)[pos]
            }
            override fun onNothingSelected(parent: AdapterView<out Adapter>?) {  }
        }

        copertura_peso.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

            override fun onItemSelected(parent: AdapterView<*>, mView: View?, pos: Int, id: Long) {
                val peso = mPesiCopertura[pos]
                copertura_g1.text = String.format(context.getString(R.string.g1_format), peso)
            }
            override fun onNothingSelected(parent: AdapterView<out Adapter>?) {  }
        }

        solaio_peso.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

            override fun onItemSelected(parent: AdapterView<*>, mView: View?, pos: Int, id: Long) {
                val peso = mPesiSolaio[pos]
                solaio_g1.text = String.format(context.getString(R.string.g1_format), peso)
                updateSolaioQ()
            }
            override fun onNothingSelected(parent: AdapterView<out Adapter>?) {  }
        }
        solaio_g2.attachDataConfirmedCallback { updateSolaioQ() }
        solaio_qk.attachDataConfirmedCallback { updateSolaioQ() }

        copertura_g2.attachDataConfirmedCallback { updateCoperturaQ() }
        copertura_qk.attachDataConfirmedCallback { updateCoperturaQ() }

        copertura_type.setOnClickListener { context.toast(R.string.error_not_supported) }
        solaio_type.setOnClickListener { context.toast(R.string.error_not_supported) }
    }

    private fun updateSolaioQ()
    {
        val q = mPesiSolaio[solaio_peso.selectedItemPosition].toDoubleOrZero() + solaio_g2.getParameterValue().toDoubleOrZero() + solaio_qk.getParameterValue().toDoubleOrZero()*0.3
        solaio_q.text = q.toString()
    }

    private fun updateCoperturaQ()
    {
        val q = mPesiCopertura[copertura_peso.selectedItemPosition].toDoubleOrZero() + copertura_g2.getParameterValue().toDoubleOrZero() + copertura_qk.getParameterValue().toDoubleOrZero()*0.3
        copertura_q.text = q.toString()
    }

    fun getTipoFondazioni(): String {

        return when
        {
            fondazioni_type_platea.isChecked -> fondazioni_type_platea.textOn.toString()
            fondazioni_type_trave.isChecked -> fondazioni_type_trave.textOn.toString()
            fondazioni_type_plinti.isChecked -> fondazioni_type_plinti.textOn.toString()
            else -> fondazioni_type_platea.textOn.toString()
        }
    }

    fun setTipoFondazioni(tipo : String)
    {
        fondazioni_type_platea.isChecked = false
        fondazioni_type_trave.isChecked  = false
        fondazioni_type_plinti.isChecked  = false

        when {
            fondazioni_type_platea.textOn.equals(tipo) -> fondazioni_type_platea.isChecked = true
            fondazioni_type_trave.textOn.equals(tipo) -> fondazioni_type_trave.isChecked = true
            fondazioni_type_plinti.textOn.equals(tipo) -> fondazioni_type_plinti.isChecked = true
        }
    }

    fun setPesoSolaioByValue(peso_solaio: String) {

        //iterates all spinners element looking for the category requested
        (0 until solaio_peso.count)
                .firstOrNull { solaio_peso.getItemAtPosition(it) == peso_solaio }
                ?.let { return solaio_peso.setSelection(it)}
    }

    fun setPesoCoperturaByValue(peso_copertura: String) {

        //iterates all spinners element looking for the category requested
        (0 until copertura_peso.count)
                .firstOrNull { copertura_peso.getItemAtPosition(it) == peso_copertura }
                ?.let { return copertura_peso.setSelection(it)}
    }

    //callback to activity updates domain instance for activity and all existing and future fragments
    override fun onNextClicked(callback: StepperLayout.OnNextClickedCallback?) {
        getReport().reportState.buildingState.structuralState = UiMapper.createStructuralStateForDomain(this, getReport().reportState.buildingState)
        super.onNextClicked(callback)
    }
}