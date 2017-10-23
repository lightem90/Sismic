package com.polito.sismic.Presenters.ReportActivity.Fragments

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.support.annotation.Nullable
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.LegendEntry
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.polito.sismic.Domain.PillarDomain
import com.polito.sismic.Domain.PillarState
import com.polito.sismic.Extensions.hideSoftKeyboard
import com.polito.sismic.Interactors.Helpers.UiMapper
import com.polito.sismic.Interactors.PillarLayoutInteractor
import com.polito.sismic.Presenters.Helpers.TakeoverValueFormatter
import com.polito.sismic.R
import com.stepstone.stepper.StepperLayout
import com.stepstone.stepper.VerificationError
import kotlinx.android.synthetic.main.maglia_strutt_report_layout.*

class MagliaStrutturaleReportFragment : BaseReportFragment(), OnChartValueSelectedListener {

    private val mPillarLayoutInteractor : PillarLayoutInteractor by lazy {
        PillarLayoutInteractor(context)
    }
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

                updateLabels()
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, @Nullable container: ViewGroup?, @Nullable savedInstanceState: Bundle?): View? {
        return inflateFragment(R.layout.maglia_strutt_report_layout, inflater, container)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        num_x.attachDataConfirmedCallback { dist_x.requestFocus() }
        dist_x.attachDataConfirmedCallback { num_y.requestFocus() }
        num_y.attachDataConfirmedCallback { dist_y.requestFocus() }
        dist_y.attachDataConfirmedCallback { if (!it.isEmpty()) activity.hideSoftKeyboard() }

        with(plant_graph)
        {
            val formatter = TakeoverValueFormatter()
            xAxis.valueFormatter = formatter.xValueFormatter
            getAxis(YAxis.AxisDependency.LEFT).valueFormatter = formatter.yValueFormatter

            xAxis.position = XAxis.XAxisPosition.BOTTOM
            legend.form = Legend.LegendForm.DEFAULT
            legend.setCustom(listOf(LegendEntry(context.getString(R.string.rilievo_esterno), Legend.LegendForm.DEFAULT, 8f, 1f, null, Color.BLACK),
                    LegendEntry(context.getString(R.string.pilastri_validi), Legend.LegendForm.DEFAULT, 8f, 1f, null, ContextCompat.getColor(context, R.color.pillar_on)),
                    LegendEntry(context.getString(R.string.pilastri_non_validi), Legend.LegendForm.DEFAULT, 8f, 1f, null, ContextCompat.getColor(context, R.color.pillar_off))))
            description.isEnabled = false
            getAxis(YAxis.AxisDependency.LEFT).axisMinimum = 0f
            getAxis(YAxis.AxisDependency.RIGHT).isEnabled = false

        }

        //To delete pillars
        plant_graph.setOnChartValueSelectedListener(this)
        calculate.setOnClickListener{ updateGraph() }
    }

    private fun updateGraph(){
        val numPillars = countPillars()
        mPillarLayoutInteractor.convertListForGraph(UiMapper.createPillarLayoutStateForDomain(this,
                getReport().reportState.buildingState.takeoverState.area / numPillars, numPillars),
                getReport().reportState.buildingState.takeoverState.figure)?.let {
            plant_graph.data = it
            plant_graph.invalidate()
            updateLabels()
        }
    }

    override fun onReload() {
        super.onReload()
        updateGraph()
    }

    override fun verifyStep(): VerificationError? {
        if (num_x.isEmpty()) return VerificationError(String.format(resources.getString(R.string.verification_empty_field), num_x.getTitle()))
        if (dist_x.isEmpty()) return VerificationError(String.format(resources.getString(R.string.verification_empty_field), dist_x.getTitle()))
        if (num_y.isEmpty()) return VerificationError(String.format(resources.getString(R.string.verification_empty_field), num_y.getTitle()))
        if (dist_y.isEmpty()) return VerificationError(String.format(resources.getString(R.string.verification_empty_field), dist_y.getTitle()))
        if (getReport().reportState.buildingState.takeoverState.area == 0.0) return VerificationError(resources.getString(R.string.verification_empty_area))
        if (countPillars() == 0) return VerificationError(resources.getString(R.string.verification_no_pillars))
        return null
    }

    override fun onNeedReload(): Boolean {
        return true
    }

    //callback to activity updates domain instance for activity and all existing and future fragments
    override fun onNextClicked(callback: StepperLayout.OnNextClickedCallback?) {
        val numPillars = countPillars()
        getReport().reportState.buildingState.pillarLayoutState = UiMapper.createPillarLayoutStateForDomain(this,
                getReport().reportState.buildingState.takeoverState.area / numPillars, numPillars)
        getReport().reportState.buildingState.pillarState.pillar_domain.limitStatePoints =  mPillarLayoutInteractor.calculateLimitStatePointsInGraph(getReport().reportState)
        super.onNextClicked(callback)
    }

    private fun updateLabels()
    {
        val numPillars = countPillars()
        count_pillar_label.setValue(numPillars.toString())

        area_pillar_label.setValue(String.format(context.getString(R.string.area_label),
                if (numPillars > 0) "%.2f".format((getReport().reportState.buildingState.takeoverState.area / numPillars))
                else context.getString(R.string.invalid_area))
        )
    }

    //Count available pillars
    private fun countPillars(): Int {
        return plant_graph.data?.dataSets?.count { it.entryCount == 1 && it.label == "MS" && it.getCircleColor(0) == ContextCompat.getColor(context, R.color.pillar_on)} ?: 0
    }
}