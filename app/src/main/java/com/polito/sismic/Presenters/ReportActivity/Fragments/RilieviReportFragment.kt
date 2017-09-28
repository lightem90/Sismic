package com.polito.sismic.Presenters.ReportActivity.Fragments

import android.os.Bundle
import android.support.annotation.Nullable
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Adapter
import android.widget.AdapterView
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.polito.sismic.Domain.PlantPoint
import com.polito.sismic.Extensions.toDoubleOrZero
import com.polito.sismic.Interactors.Helpers.UiMapper
import com.polito.sismic.Interactors.SismicPlantBuildingInteractor
import com.polito.sismic.Presenters.Adapters.PlantPointsAdapter
import com.polito.sismic.R
import com.stepstone.stepper.StepperLayout
import kotlinx.android.synthetic.main.rilievi_report_layout.*

/**
 * Created by Matteo on 10/08/2017.
 */
class RilieviReportFragment : BaseReportFragment() {

    val mSismicPlantBuildingInteractor : SismicPlantBuildingInteractor by lazy {
        SismicPlantBuildingInteractor(context)
    }

    val mPointList : MutableList<PlantPoint> by lazy {
        mSismicPlantBuildingInteractor.getPlantPointList()
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

        altezza_piano_tr_parameter.attachDataConfirmedCallback { updateAltezzaTotale() }
        altezza_piani_sup_parameter.attachDataConfirmedCallback { updateAltezzaTotale() }


        plant_point_list.layoutManager = LinearLayoutManager(activity)
        val adapter = PlantPointsAdapter(activity, mSismicPlantBuildingInteractor, mPointList){
            invalidateAndReload()
        }
        plant_point_list.adapter = adapter
        adapter.somethingChanged()

        with(plant_graph)
        {
            xAxis.position = XAxis.XAxisPosition.BOTTOM
            legend.form = Legend.LegendForm.DEFAULT
            description.isEnabled = false
            getAxis(YAxis.AxisDependency.RIGHT).isEnabled = false
        }
    }

    fun invalidateAndReload()
    {
        invalidate()
        updateGraph()
    }

    private fun updateGraph() = with(plant_graph)
    {
        data = mSismicPlantBuildingInteractor.convertListForGraph()
        invalidate()
    }

    fun invalidate()
    {
        mPointList.clear()
        mPointList.addAll(mSismicPlantBuildingInteractor.getPlantPointList())
        plant_point_list?.adapter?.notifyDataSetChanged()
    }

    private fun updateAltezzaTotale()
    {
        altezza_tot.text  = if (piani_numero_parameter.selectedItemPosition > 0)
            (altezza_piano_tr_parameter.getParameterValue().toDoubleOrZero() + piani_numero_parameter.selectedItemPosition * altezza_piani_sup_parameter.getParameterValue().toDoubleOrZero()).toString()
            else altezza_piano_tr_parameter.getParameterValue()
    }

    //callback to activity updates domain instance for activity and all existing and future fragments
    override fun onNextClicked(callback: StepperLayout.OnNextClickedCallback?) {
        getReport().reportState.buildingState.takeoverState = UiMapper.createTakeoverStateForDomain(this)
        super.onNextClicked(callback)
    }
}