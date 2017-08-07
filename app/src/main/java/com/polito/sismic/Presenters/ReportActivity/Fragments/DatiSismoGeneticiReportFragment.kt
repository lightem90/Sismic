package com.polito.sismic.Presenters.ReportActivity.Fragments

import android.os.Bundle
import android.support.annotation.Nullable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.polito.sismic.R
import kotlinx.android.synthetic.main.dati_sismogenetici_report_layout.*

/**
 * Created by Matteo on 29/07/2017.
 */
class DatiSismoGeneticiReportFragment : BaseReportFragment() {

    override fun onCreateView(inflater: LayoutInflater?, @Nullable container: ViewGroup?, @Nullable savedInstanceState: Bundle?): View? {
        return inflateFragment(R.layout.dati_sismogenetici_report_layout, inflater, container)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //TODO lista vuota, al click del pulsante parte l'acquisizione
//        list_nodi.adapter =

        nodi_request_button.setOnClickListener(
                {
                    fillListAsync()
                    list_nodi.invalidate()
                    buildGraph()
                }
        )
    }

    private fun buildGraph() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        //report_spettrodirisposta_chart
    }

    private fun fillListAsync() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}