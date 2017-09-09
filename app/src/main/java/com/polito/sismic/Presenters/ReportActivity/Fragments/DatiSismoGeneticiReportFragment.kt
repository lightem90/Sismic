package com.polito.sismic.Presenters.ReportActivity.Fragments

import android.os.Bundle
import android.support.annotation.Nullable
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.polito.sismic.Domain.NeighboursNodeData
import com.polito.sismic.Domain.PeriodData
import com.polito.sismic.Extensions.toMutableList
import com.polito.sismic.Interactors.Helpers.UiMapper
import com.polito.sismic.Presenters.Adapters.NodeListAdapter
import com.polito.sismic.Presenters.Adapters.PeriodListAdapter
import com.polito.sismic.R
import com.stepstone.stepper.StepperLayout
import kotlinx.android.synthetic.main.dati_sismogenetici_report_layout.*

/**
 * Created by Matteo on 29/07/2017.
 */
class DatiSismoGeneticiReportFragment : BaseReportFragment() {

    var mNodeList : MutableList<NeighboursNodeData> = mutableListOf()
    var mPeriodList : MutableList<PeriodData> = mutableListOf()

    override fun onCreateView(inflater: LayoutInflater?, @Nullable container: ViewGroup?, @Nullable savedInstanceState: Bundle?): View? {
        return inflateFragment(R.layout.dati_sismogenetici_report_layout, inflater, container)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        list_nodi.layoutManager = LinearLayoutManager(context)
        list_nodi.adapter = NodeListAdapter(context, mNodeList)

        list_periodi.layoutManager = LinearLayoutManager(context)
        list_periodi.adapter = PeriodListAdapter(context, mPeriodList)

        reloadFragment()
    }

    override fun reloadFragment()
    {
        getReport().reportState.sismicState.sismogenticState.closedNodeData.let {

            mNodeList.clear()
            mNodeList.addAll(it)
            list_nodi.adapter.notifyDataSetChanged()
        }

        getReport().reportState.sismicState.sismogenticState.periodData_list.let {
            mPeriodList.clear()
            mPeriodList.addAll(it)
            list_periodi.adapter.notifyDataSetChanged()
        }

        //Must never fail!
        getReport().reportState.localizationState.let {

            updateLabelsByCoordinate(it.latitude.toString(),
                    it.longitude.toString(),
                    it.address,
                    it.zone)
        }
    }

    private fun updateLabelsByCoordinate(latitude : String, longitude : String, address : String, zone : String)
    {
        report_datisimogenetici_coordinate_label.setValue(String.format(context.getString(R.string.coordinate_label), longitude, latitude))
        report_datisimogenetici_indirizzo.setValue(address)
        report_datisimogenetici_zonasismica.setValue(zone)
    }

    //callback to activity updates domain instance for activity and all existing and future fragments
    override fun onNextClicked(callback: StepperLayout.OnNextClickedCallback?) {
        getReport().reportState.sismicState.sismogenticState = UiMapper.createSismogeneticStateForDomain(this)
        super.onNextClicked(callback)
    }

    override fun onParametersInjectedForEdit() {

    }
}

