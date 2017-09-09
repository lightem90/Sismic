package com.polito.sismic.Presenters.ReportActivity.Fragments

import android.os.Bundle
import android.support.annotation.Nullable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.polito.sismic.Interactors.Helpers.UiMapper
import com.polito.sismic.R
import com.stepstone.stepper.StepperLayout
import kotlinx.android.synthetic.main.pilastri_report_layout.*

/**
 * Created by Matteo on 30/07/2017.
 */
class PilastriReportFragment : BaseReportFragment() {

    override fun onCreateView(inflater: LayoutInflater?, @Nullable container: ViewGroup?, @Nullable savedInstanceState: Bundle?): View? {
        return inflateFragment(R.layout.pilastri_report_layout, inflater, container)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        calc_classe_res_parameter_lc1.setOnCheckedChangeListener { _, flag ->
            if (flag)
            {
                calc_classe_res_parameter_lc2.isChecked = false
                calc_classe_res_parameter_lc3.isChecked = false
                calc_classe_res_parameter_lc1.isClickable = false
            }
            else
            {
                calc_classe_res_parameter_lc1.isClickable = true
            }
        }
        calc_classe_res_parameter_lc2.setOnCheckedChangeListener { _, flag ->
            if (flag)
            {
                calc_classe_res_parameter_lc1.isChecked = false
                calc_classe_res_parameter_lc3.isChecked = false
                calc_classe_res_parameter_lc2.isClickable = false
            }
            else
            {
                calc_classe_res_parameter_lc2.isClickable = true
            }
        }
        calc_classe_res_parameter_lc3.setOnCheckedChangeListener { _, flag ->
            if (flag)
            {
                calc_classe_res_parameter_lc2.isChecked = false
                calc_classe_res_parameter_lc1.isChecked = false
                calc_classe_res_parameter_lc3.isClickable = false
            }
            else
            {
                calc_classe_res_parameter_lc3.isClickable = true
            }
        }
        acc_classe_res_parameter_lc1.setOnCheckedChangeListener { _, flag ->
            if (flag)
            {
                acc_classe_res_parameter_lc2.isChecked = false
                acc_classe_res_parameter_lc3.isChecked = false
                acc_classe_res_parameter_lc1.isClickable = false
            }
            else
            {
                acc_classe_res_parameter_lc1.isClickable = true
            }
        }
        acc_classe_res_parameter_lc2.setOnCheckedChangeListener { _, flag ->
            if (flag)
            {
                acc_classe_res_parameter_lc1.isChecked = false
                acc_classe_res_parameter_lc3.isChecked = false
                acc_classe_res_parameter_lc2.isClickable = false
            }
            else
            {
                acc_classe_res_parameter_lc2.isClickable = true
            }
        }
        acc_classe_res_parameter_lc3.setOnCheckedChangeListener { _, flag ->
            if (flag)
            {
                acc_classe_res_parameter_lc2.isChecked = false
                acc_classe_res_parameter_lc1.isChecked = false
                acc_classe_res_parameter_lc3.isClickable = false
            }
            else
            {
                acc_classe_res_parameter_lc3.isClickable = true
            }
        }
        acc_classe_parameter_A.setOnCheckedChangeListener { _, flag ->
            if (flag)
            {
                acc_classe_parameter_C.isChecked = false
                acc_classe_parameter_A.isClickable = false
            }
            else
            {
                acc_classe_parameter_A.isClickable = true
            }
        }
        acc_classe_parameter_C.setOnCheckedChangeListener { _, flag ->
            if (flag)
            {
                acc_classe_parameter_A.isChecked = false
                acc_classe_parameter_C.isClickable = false
            }
            else
            {
                acc_classe_parameter_C.isClickable = true
            }
        }
    }

    fun getConoscenzaCalcestruzzo() : Int
    {
        return when
        {
            calc_classe_res_parameter_lc1.isChecked -> 0
            calc_classe_res_parameter_lc2.isChecked -> 1
            calc_classe_res_parameter_lc3.isChecked -> 2
            else -> 0
        }
    }

    fun getConoscenzaAcciaio() : Int
    {
        return when
        {
            acc_classe_res_parameter_lc1.isChecked -> 0
            acc_classe_res_parameter_lc2.isChecked -> 1
            acc_classe_res_parameter_lc3.isChecked -> 2
            else -> 0
        }
    }

    fun setConoscenzaCalcestruzzo(con : Int)
    {
        when (con)
        {
            0 -> calc_classe_res_parameter_lc1.isChecked = true
            1 -> calc_classe_res_parameter_lc2.isChecked = true
            2 -> calc_classe_res_parameter_lc3.isChecked = true
            else -> acc_classe_res_parameter_lc1.isChecked = true
        }
    }

    fun setConoscenzaAcciaio(con : Int)
    {
        when (con)
        {
            0 -> acc_classe_res_parameter_lc1.isChecked = true
            1 -> acc_classe_res_parameter_lc2.isChecked = true
            2 -> acc_classe_res_parameter_lc3.isChecked = true
            else -> acc_classe_res_parameter_lc1.isChecked = true
        }
    }

    fun setCalcestruzzoClasseByValue(classe_calcestruzzo: String) {

        if (classe_calcestruzzo.isEmpty()) return
        //iterates all spinners element looking for the category requested
        (0 until calc_classe_parameter.count)
                .firstOrNull { calc_classe_parameter.getItemAtPosition(it) == classe_calcestruzzo }
                ?.let { return calc_classe_parameter.setSelection(it)}
    }

    //callback to activity updates domain instance for activity and all existing and future fragments
    override fun onNextClicked(callback: StepperLayout.OnNextClickedCallback?) {
        getReport().reportState.buildingState.pillarState = UiMapper.createPillarStateForDomain(this)
        super.onNextClicked(callback)
    }
}