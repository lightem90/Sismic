package com.polito.sismic.Presenters.ReportActivity.Fragments

import android.os.Bundle
import android.support.annotation.Nullable
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.polito.sismic.Domain.NeighboursNodeData
import com.polito.sismic.Domain.PeriodData
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
    var mPeriodList : MutableList<PeriodData> = mutableListOf ()

    private lateinit var mNodeAdapter : NodeListAdapter
    private lateinit var mPeriodAdapter : PeriodListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater?, @Nullable container: ViewGroup?, @Nullable savedInstanceState: Bundle?): View? {
        return inflateFragment(R.layout.dati_sismogenetici_report_layout, inflater, container)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(list_nodi)
        {
            layoutManager = LinearLayoutManager(context)
            mNodeAdapter = NodeListAdapter(context, mNodeList)
            adapter = mNodeAdapter
        }

        with(list_periodi)
        {
            layoutManager = LinearLayoutManager(context)
            mPeriodAdapter = PeriodListAdapter(context, mPeriodList)
            adapter = mPeriodAdapter
        }

        onReload()
    }

    override fun onReload()
    {
        with(getReport().reportState.sismicState.sismogenticState.closedNodeData)
        {
            mNodeList.clear()
            mNodeList.addAll(toList())
            mNodeAdapter.notifyDataSetChanged()
        }

        //Log.d("PeriodList", "Period list updating: " + getReport().reportState.sismicState.sismogenticState.periodData_list.toString())
        //if (getReport().reportState.sismicState.sismogenticState.periodData_list.isEmpty()) Log.e("PeriodList", "EMPTY!")

        with(getReport().reportState.sismicState.sismogenticState.periodData_list){
            mPeriodList.clear()
            mPeriodList.addAll(toList())
            mPeriodAdapter.notifyDataSetChanged()
        }

        with(getReport().reportState.localizationState) {

            updateLabelsByCoordinate(latitude.toString(),
                    longitude.toString(),
                    address,
                    zone)
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
}

