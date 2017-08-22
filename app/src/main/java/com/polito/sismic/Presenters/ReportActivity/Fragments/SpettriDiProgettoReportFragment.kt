package com.polito.sismic.Presenters.ReportActivity.Fragments

import android.os.Bundle
import android.support.annotation.Nullable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.polito.sismic.Extensions.toast
import com.polito.sismic.R
import kotlinx.android.synthetic.main.spettri_progetto_report_layout.*

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
        }
    }
}