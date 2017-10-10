package com.polito.sismic.Presenters.ReportActivity.Fragments

import android.os.Bundle
import android.support.annotation.Nullable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Adapter
import android.widget.AdapterView
import com.polito.sismic.Extensions.hideSoftKeyboard
import com.polito.sismic.Extensions.toDoubleOrZero
import com.polito.sismic.Extensions.toast
import com.polito.sismic.Interactors.Helpers.UiMapper
import com.polito.sismic.R
import com.stepstone.stepper.StepperLayout
import com.stepstone.stepper.VerificationError
import kotlinx.android.synthetic.main.dati_strutturali_report_layout.*

/**
 * Created by Matteo on 30/07/2017.
 */
class DatiStrutturaliReportFragment : BaseReportFragment() {


    var qCopertura: Double = 0.0
    var qSolaio: Double = 0.0
    private lateinit var mPesiSolaio: Array<out String>
    private lateinit var mPesiCopertura: Array<out String>

    override fun onCreateView(inflater: LayoutInflater?, @Nullable container: ViewGroup?, @Nullable savedInstanceState: Bundle?): View? {
        return inflateFragment(R.layout.dati_strutturali_report_layout, inflater, container)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mPesiSolaio = context.resources.getStringArray(R.array.solaio_int_pesi)
        mPesiCopertura = context.resources.getStringArray(R.array.copertura_int_pesi)

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
                updateCoperturaQ()
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

        fondazioni_h.attachDataConfirmedCallback { if (solaio_container.visibility == View.VISIBLE) solaio_g2.requestFocus() else copertura_g2.requestFocus() }
        solaio_g2.attachDataConfirmedCallback {
            updateSolaioQ()
            solaio_qk.requestFocus()
        }
        solaio_qk.attachDataConfirmedCallback {
            updateSolaioQ()
            copertura_g2.requestFocus()
        }
        copertura_g2.attachDataConfirmedCallback {
            updateCoperturaQ()
            copertura_qk.requestFocus()
        }
        copertura_qk.attachDataConfirmedCallback {
            if (!it.isEmpty()) activity.hideSoftKeyboard()
            updateCoperturaQ()
        }

        copertura_type.setOnClickListener {
            context.toast(R.string.error_not_supported)
            copertura_type.isChecked = true
        }
        solaio_type.setOnClickListener {
            context.toast(R.string.error_not_supported)
            copertura_type.isChecked = true
        }

        setSolaioVisibility()
    }

    private fun updateSolaioQ()
    {
        qSolaio = mPesiSolaio[solaio_peso.selectedItemPosition].toDoubleOrZero() + solaio_g2.getParameterValue().toDoubleOrZero() + solaio_qk.getParameterValue().toDoubleOrZero()*0.3
        solaio_q.text = String.format(context.getString(R.string.solaio_q_format), "%.2f".format(qSolaio))
    }

    private fun updateCoperturaQ()
    {
        qCopertura = mPesiCopertura[copertura_peso.selectedItemPosition].toDoubleOrZero() + copertura_g2.getParameterValue().toDoubleOrZero() + copertura_qk.getParameterValue().toDoubleOrZero()*0.3

        copertura_q.text = String.format(context.getString(R.string.copertura_q_format), "%.2f".format(qCopertura))
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

        when (tipo) {
            fondazioni_type_platea.textOn -> fondazioni_type_platea.isChecked = true
            fondazioni_type_trave.textOn -> fondazioni_type_trave.isChecked = true
            fondazioni_type_plinti.textOn -> fondazioni_type_plinti.isChecked = true
            else -> fondazioni_type_platea.isChecked = true
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

    override fun onReload() {
        super.onReload()
        setSolaioVisibility()
    }

    private fun setSolaioVisibility()
    {
        if (getReport().reportState.buildingState.takeoverState.numero_piani <= 1)
            solaio_container.visibility = View.GONE
        else
            solaio_container.visibility = View.VISIBLE
    }

    //all parameters must have a value
    override fun verifyStep(): VerificationError? {
        if (fondazioni_h.isEmpty()) return VerificationError(String.format(resources.getString(R.string.verification_empty_field), fondazioni_h.getTitle()))
        if (solaio_container.visibility == View.VISIBLE && solaio_g2.isEmpty()) return VerificationError(String.format(resources.getString(R.string.verification_empty_field), solaio_g2.getTitle()))
        if (solaio_container.visibility == View.VISIBLE  && solaio_qk.isEmpty()) return VerificationError(String.format(resources.getString(R.string.verification_empty_field), solaio_qk.getTitle()))
        if (copertura_g2.isEmpty()) return VerificationError(String.format(resources.getString(R.string.verification_empty_field), copertura_g2.getTitle()))
        if (copertura_qk.isEmpty()) return VerificationError(String.format(resources.getString(R.string.verification_empty_field), copertura_qk.getTitle()))
        return super.verifyStep()
    }

    //callback to activity updates domain instance for activity and all existing and future fragments
    override fun onNextClicked(callback: StepperLayout.OnNextClickedCallback?) {
        getReport().reportState.buildingState.structuralState = UiMapper.createStructuralStateForDomain(this, getReport().reportState.buildingState)
        super.onNextClicked(callback)
    }
}