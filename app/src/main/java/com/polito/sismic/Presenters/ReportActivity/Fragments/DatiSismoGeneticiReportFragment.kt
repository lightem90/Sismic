package com.polito.sismic.Presenters.ReportActivity.Fragments

import android.content.Context
import android.os.Bundle
import android.support.annotation.Nullable
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.polito.sismic.Domain.NeighboursNodeData
import com.polito.sismic.Domain.PeriodData
import com.polito.sismic.Domain.SpectrumDTO
import com.polito.sismic.Extensions.toEntryList
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

    private lateinit var mNodeAdapter: NodeListAdapter
    private lateinit var mPeriodAdapter: PeriodListAdapter

    var mNodeList: MutableList<NeighboursNodeData> = mutableListOf()
    var mPeriodList: MutableList<PeriodData> = mutableListOf()

    interface DefaultReturnTimeRequest {
        fun onDefaultReturnTimesRequested(): List<SpectrumDTO>
    }

    private var mDefaultReturnTimeRequest: DefaultReturnTimeRequest? = null
    override fun onAttach(context: Context?) {

        super.onAttach(context)
        try {
            mDefaultReturnTimeRequest = context as DefaultReturnTimeRequest?
        } catch (e: ClassCastException) {
            throw ClassCastException(context!!.toString() + " must implement OnParametersConfirmed")
        }
    }

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

        with(report_spettrodirisposta_chart)
        {
            xAxis.position = XAxis.XAxisPosition.BOTTOM
            xAxis.axisMaximum = 4.0f
            xAxis.axisMinimum = 0.0f
            legend.verticalAlignment = Legend.LegendVerticalAlignment.BOTTOM
            legend.setDrawInside(false)
            legend.isWordWrapEnabled = true
            description.isEnabled = false
            getAxis(YAxis.AxisDependency.RIGHT).isEnabled = false
        }

        calculate.setOnClickListener {

            mDefaultReturnTimeRequest?.onDefaultReturnTimesRequested()?.let {
                getReport().reportState.sismicState.sismogenticState.default_spectrum = it
                reloadGraph()
            }
        }
    }

    private fun reloadGraph()
    {
        with(report_spettrodirisposta_chart)
        {
            val spectrumsUi = getReport().reportState.sismicState.sismogenticState.default_spectrum.map {
                LineDataSet(it.pointList.toEntryList(), String.format(context.getString(R.string.label_year_format), it.name)).apply {
                    color = ContextCompat.getColor(context, it.color)
                    setDrawCircles(false)
                    lineWidth = 2f
                    axisDependency = YAxis.AxisDependency.LEFT
                }
            }

            data = LineData(spectrumsUi)
            invalidate()
        }
    }

    //Called each time the existing view is updated
    override fun onReload() {
        getReport().reportState.sismicState.sismogenticState.closedNodeData.let {
            mNodeList.clear()
            mNodeList.addAll(it)
            mNodeAdapter.notifyDataSetChanged()
        }

        //Log.d("PeriodList", "Period list updating: " + getReport().reportState.sismicState.sismogenticState.default_periods.toString())
        //if (getReport().reportState.sismicState.sismogenticState.default_periods.isEmpty()) Log.e("PeriodList", "EMPTY!")

        getReport().reportState.sismicState.sismogenticState.default_periods.let {
            mPeriodList.clear()
            mPeriodList.addAll(it)
            mPeriodAdapter.notifyDataSetChanged()
        }

        getReport().reportState.localizationState.let {

            updateLabelsByCoordinate(it.latitude.toString(),
                    it.longitude.toString(),
                    it.address,
                    it.zone)
        }

        reloadGraph()
    }

    private fun updateLabelsByCoordinate(latitude: String, longitude: String, address: String, zone: String) {
        report_datisimogenetici_coordinate_label.setValue(String.format(context.getString(R.string.coordinate_label), longitude, latitude))
        report_datisimogenetici_indirizzo.setValue(address)
        report_datisimogenetici_zonasismica.setValue(zone)
    }

    //callback to activity updates domain instance for activity and all existing and future fragments
    override fun onNextClicked(callback: StepperLayout.OnNextClickedCallback?) {
        getReport().reportState.sismicState.sismogenticState = UiMapper.createSismogeneticStateForDomain(this, getReport().reportState.sismicState.sismogenticState.default_spectrum)
        super.onNextClicked(callback)
    }
}

