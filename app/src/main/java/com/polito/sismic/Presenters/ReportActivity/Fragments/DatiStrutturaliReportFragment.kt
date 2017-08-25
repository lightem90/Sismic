package com.polito.sismic.Presenters.ReportActivity.Fragments

import android.os.Bundle
import android.support.annotation.Nullable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.polito.sismic.Extensions.toast
import com.polito.sismic.R
import kotlinx.android.synthetic.main.dati_strutturali_report_layout.*

/**
 * Created by Matteo on 30/07/2017.
 */
class DatiStrutturaliReportFragment : BaseReportFragment() {

    override fun onCreateView(inflater: LayoutInflater?, @Nullable container: ViewGroup?, @Nullable savedInstanceState: Bundle?): View? {
        return inflateFragment(R.layout.dati_strutturali_report_layout, inflater, container)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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

        copertura_type.setOnClickListener { context.toast(R.string.error_not_supported) }
        solaio_type.setOnClickListener { context.toast(R.string.error_not_supported) }
    }
}