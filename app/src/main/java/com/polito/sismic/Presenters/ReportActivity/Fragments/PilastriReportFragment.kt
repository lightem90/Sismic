package com.polito.sismic.Presenters.ReportActivity.Fragments

import android.os.Bundle
import android.support.annotation.Nullable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.polito.sismic.R
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
}