package com.polito.sismic.Presenters.ReportActivity.Fragments

import android.graphics.Color
import android.os.Bundle
import android.support.annotation.Nullable
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Adapter
import android.widget.AdapterView
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.LegendEntry
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.polito.sismic.Extensions.toDoubleOrZero
import com.polito.sismic.Interactors.Helpers.UiMapper
import com.polito.sismic.Interactors.SismicPlantBuildingInteractor
import com.polito.sismic.Presenters.Adapters.PlantPointsAdapter
import com.polito.sismic.R
import com.stepstone.stepper.StepperLayout
import com.stepstone.stepper.VerificationError
import kotlinx.android.synthetic.main.rilievi_report_layout.*

/**
 * Created by Matteo on 10/08/2017.
 */
class RilieviReportFragment : BaseReportFragment(), OnChartValueSelectedListener {

    override fun onNothingSelected() {  return   }
    override fun onValueSelected(e: Entry?, h: Highlight?) {

        e?.let { entryClicked->

            //Take the dataset that contains the clicked point
            var changes = false
            val selectedDataset = plant_graph.data.dataSets
                    .filter { dataset -> dataset.entryCount == 1
                            && dataset.label == "MS"}
                    .firstOrNull { dataset -> dataset.contains(e)} ?: return

            when {
                selectedDataset.getCircleColor(0) == ContextCompat.getColor(context, R.color.pillar_on) ->
                    //If the entry clicked was one of a green pillar, i set a new dataset at the same position "gray", meaning the
                    //pillar wont be counted
                    selectedDataset.let {
                        plant_graph.data.removeDataSet(it)
                        plant_graph.data.addDataSet(LineDataSet(listOf(entryClicked), "MS").apply {
                            circleRadius = 4f
                            circleColors = listOf(ContextCompat.getColor(context, R.color.pillar_off))
                            setDrawCircles(true)
                            axisDependency = YAxis.AxisDependency.LEFT }
                        )
                        changes = true
                    }
                selectedDataset.getCircleColor(0) == ContextCompat.getColor(context, R.color.pillar_off) -> {
                    //if the user clicks a gray pillar, the pillar will become green
                    selectedDataset.let {
                        plant_graph.data.removeDataSet(it)
                        plant_graph.data.addDataSet(LineDataSet(listOf(entryClicked), "MS").apply {
                            circleRadius = 5f
                            circleColors = listOf(ContextCompat.getColor(context, R.color.pillar_on))
                            setDrawCircles(true)
                            axisDependency = YAxis.AxisDependency.LEFT }
                        )
                    }
                    changes = true
                }
                else -> changes = false
            }

            //If changes have been made, update all the things
            if (changes)
            {
                plant_graph.notifyDataSetChanged()
                plant_graph.invalidate()

                count_pillar_label.setValue(countPillars().toString())
            }
        }
    }

    val mSismicPlantBuildingInteractor : SismicPlantBuildingInteractor by lazy {
        SismicPlantBuildingInteractor(getReport().reportState.buildingState.takeoverState, context)
    }

    override fun onCreateView(inflater: LayoutInflater?, @Nullable container: ViewGroup?, @Nullable savedInstanceState: Bundle?): View? {
        return inflateFragment(R.layout.rilievi_report_layout, inflater, container)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        piani_numero_parameter.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

            override fun onItemSelected(parent: AdapterView<*>, mView: View?, pos: Int, id: Long) {
                if (pos > 0)
                    altezza_piani_sup_parameter.visibility = View.VISIBLE
                else
                    altezza_piani_sup_parameter.visibility = View.GONE
            }
            override fun onNothingSelected(parent: AdapterView<out Adapter>?) {  }
        }

        altezza_piano_tr_parameter.attachDataConfirmedCallback {
            if (altezza_piani_sup_parameter.visibility == View.VISIBLE)
                altezza_piani_sup_parameter.requestFocus()
        }


        plant_point_list.layoutManager = LinearLayoutManager(activity)
        val adapter = PlantPointsAdapter(activity, mSismicPlantBuildingInteractor){ plant_point_list?.adapter?.notifyDataSetChanged() }
        plant_point_list.adapter = adapter

        with(plant_graph)
        {
            xAxis.position = XAxis.XAxisPosition.BOTTOM
            legend.form = Legend.LegendForm.DEFAULT
            legend.setCustom(listOf(LegendEntry(context.getString(R.string.rilievo_esterno), Legend.LegendForm.DEFAULT, 8f, 1f, null, Color.BLACK),
                    LegendEntry(context.getString(R.string.centro_di_massa), Legend.LegendForm.DEFAULT, 8f, 1f, null, Color.RED),
                    LegendEntry(context.getString(R.string.pilastri_validi), Legend.LegendForm.DEFAULT, 8f, 1f, null, ContextCompat.getColor(context, R.color.pillar_on)),
                    LegendEntry(context.getString(R.string.pilastri_non_validi), Legend.LegendForm.DEFAULT, 8f, 1f, null, ContextCompat.getColor(context, R.color.pillar_off))))
            description.isEnabled = false
            getAxis(YAxis.AxisDependency.RIGHT).isEnabled = false
        }

        //To delete pillars
        plant_graph.setOnChartValueSelectedListener(this)
        calculate.setOnClickListener{ updateGraph() }
    }

    override fun onReload() {
        super.onReload()
        updateGraph()
    }

    private fun updateGraph() = with(plant_graph)
    {
        plant_point_list?.adapter?.notifyDataSetChanged()
        mSismicPlantBuildingInteractor.convertListForGraph(context, getReport().reportState.buildingState.pillarLayoutState)?.let {
            data = it
            invalidate()
            updateLabels()
        }
    }

    private fun updateLabels()
    {
        area_label.setValue(String.format(context.getString(R.string.area_label), "%.2f".format(mSismicPlantBuildingInteractor.area)))
        perimeter_label.setValue(String.format(context.getString(R.string.perimeter_label), "%.2f".format(mSismicPlantBuildingInteractor.perimeter)))
        barycenter_label.setValue(String.format(context.getString(R.string.barycenter_label),
                "%.2f".format(mSismicPlantBuildingInteractor.mCenter.x),
                "%.2f".format(mSismicPlantBuildingInteractor.mCenter.y)))
        count_pillar_label.setValue(countPillars().toString())

        area_pillar_label.setValue(String.format(context.getString(R.string.area_label),
                "%.2f".format(getReport().reportState.buildingState.pillarLayoutState.area)))

    }

    //Count available pillars
    private fun countPillars(): Int {
        return plant_graph.data?.dataSets?.count { it.entryCount == 1 && it.label == "MS" && it.getCircleColor(0) == ContextCompat.getColor(context, R.color.pillar_on)} ?: 0
    }

    override fun verifyStep(): VerificationError? {
        if (altezza_piano_tr_parameter.isEmpty()) return VerificationError(String.format(resources.getString(R.string.verification_empty_field), altezza_piano_tr_parameter.getTitle()))
        if (altezza_piani_sup_parameter.visibility == View.VISIBLE && altezza_piani_sup_parameter.isEmpty()) return VerificationError(String.format(resources.getString(R.string.verification_empty_field), altezza_piani_sup_parameter.getTitle()))
        if (mSismicPlantBuildingInteractor.mCenter == mSismicPlantBuildingInteractor.mOrigin) return VerificationError(resources.getString(R.string.verification_barycenter))
        if (!mSismicPlantBuildingInteractor.isClosed()) return VerificationError(resources.getString(R.string.verification_takeover_invalid))
        return null
    }

    //callback to activity updates domain instance for activity and all existing and future fragments
    override fun onNextClicked(callback: StepperLayout.OnNextClickedCallback?) {
        //Fixes the pillar count with the one that really exists in the graph
        getReport().reportState.buildingState.takeoverState = UiMapper.createTakeoverStateForDomain(this)
        getReport().reportState.buildingState.pillarLayoutState.pillarCount = countPillars()
        super.onNextClicked(callback)
    }
}