package com.polito.sismic.Presenters.ReportActivity.Fragments

import android.graphics.Color
import android.os.Bundle
import android.support.annotation.Nullable
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
class RilieviReportFragment : BaseReportFragment() {

    val mSismicPlantBuildingInteractor : SismicPlantBuildingInteractor by lazy {
        SismicPlantBuildingInteractor(getReport().reportState.buildingState.takeoverState)
    }

    override fun onCreateView(inflater: LayoutInflater?, @Nullable container: ViewGroup?, @Nullable savedInstanceState: Bundle?): View? {
        return inflateFragment(R.layout.rilievi_report_layout, inflater, container)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {

        piani_numero_parameter.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

            override fun onItemSelected(parent: AdapterView<*>, mView: View?, pos: Int, id: Long) {
                if (pos > 0)
                    altezza_piani_sup_parameter.visibility = View.VISIBLE
                else
                    altezza_piani_sup_parameter.visibility = View.GONE

                updateAltezzaTotale()
            }
            override fun onNothingSelected(parent: AdapterView<out Adapter>?) {  }
        }

        altezza_piano_tr_parameter.attachDataConfirmedCallback {
            updateAltezzaTotale()
            if (altezza_piani_sup_parameter.visibility == View.VISIBLE)
                altezza_piani_sup_parameter.requestFocus()
        }
        altezza_piani_sup_parameter.attachDataConfirmedCallback { updateAltezzaTotale() }


        plant_point_list.layoutManager = LinearLayoutManager(activity)
        val adapter = PlantPointsAdapter(activity, mSismicPlantBuildingInteractor){ plant_point_list?.adapter?.notifyDataSetChanged() }
        plant_point_list.adapter = adapter

        with(plant_graph)
        {
            xAxis.position = XAxis.XAxisPosition.BOTTOM
            legend.form = Legend.LegendForm.DEFAULT
            legend.setCustom(listOf(LegendEntry(context.getString(R.string.rilievo_esterno), Legend.LegendForm.DEFAULT, 8f, 1f, null, Color.BLACK),
                    LegendEntry(context.getString(R.string.centro_di_massa), Legend.LegendForm.DEFAULT, 8f, 1f, null, Color.RED),
                    LegendEntry(context.getString(R.string.maglia_strutturale), Legend.LegendForm.DEFAULT, 8f, 1f, null, Color.GREEN)))
            description.isEnabled = false
            getAxis(YAxis.AxisDependency.RIGHT).isEnabled = false
        }

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
            notifyDataSetChanged()
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
        area_pillar_label.setValue(String.format(context.getString(R.string.area_label),
                "%.2f".format(getReport().reportState.buildingState.pillarLayoutState.area)))
    }

    private fun updateAltezzaTotale()
    {
        altezza_tot.text  = if (piani_numero_parameter.selectedItemPosition > 0)
            (altezza_piano_tr_parameter.getParameterValue().toDoubleOrZero() + piani_numero_parameter.selectedItemPosition * altezza_piani_sup_parameter.getParameterValue().toDoubleOrZero()).toString()
            else altezza_piano_tr_parameter.getParameterValue()
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
        getReport().reportState.buildingState.takeoverState = UiMapper.createTakeoverStateForDomain(this)
        super.onNextClicked(callback)
    }
}