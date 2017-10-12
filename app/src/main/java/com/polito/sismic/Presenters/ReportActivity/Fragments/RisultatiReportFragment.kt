package com.polito.sismic.Presenters.ReportActivity.Fragments

import android.os.Bundle
import android.support.annotation.Nullable
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.polito.sismic.Domain.PillarDomainPoint
import com.polito.sismic.Interactors.Helpers.StatiLimite
import com.polito.sismic.Presenters.Adapters.ResultsAdapter
import com.polito.sismic.R
import com.stepstone.stepper.StepperLayout
import kotlinx.android.synthetic.main.risultati_report_layout.*

/**
 * Created by Matteo on 30/07/2017.
 */
class RisultatiReportFragment : BaseReportFragment() {

    private var mPointList: MutableList<PillarDomainPoint> = mutableListOf()
    private lateinit var mAdapter: ResultsAdapter
    private val mList = StatiLimite.values().toList()

    override fun onCreateView(inflater: LayoutInflater?, @Nullable container: ViewGroup?, @Nullable savedInstanceState: Bundle?): View? {
        return inflateFragment(R.layout.risultati_report_layout, inflater, container)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getReport().reportState.buildingState.pillarState.pillar_domain?.points?.let {
            mPointList.clear()
            mPointList.addAll(it)
        }
        results_container.layoutManager = LinearLayoutManager(context)
        mAdapter = ResultsAdapter(mList, context, mPointList)
        results_container.adapter = mAdapter
    }

    override fun onReload() {
        super.onReload()
        mPointList.clear()
        getReport().reportState.buildingState.pillarState.pillar_domain?.points?.let {
            mPointList.clear()
            mPointList.addAll(it)
        }
        results_container.adapter.notifyDataSetChanged()
    }

    override fun onCompleteClicked(callback: StepperLayout.OnCompleteClickedCallback?) {

        mAdapter.results.values.max()?.let {
            getReport().reportState.result.result = it
        }
        super.onCompleteClicked(callback)
    }
}