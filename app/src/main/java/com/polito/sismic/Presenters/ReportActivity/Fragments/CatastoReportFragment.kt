package com.polito.sismic.Presenters.ReportActivity.Fragments

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.support.annotation.Nullable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.polito.sismic.Interactors.Helpers.UiMapper
import com.polito.sismic.R
import com.stepstone.stepper.StepperLayout

/**
 * Created by Matteo on 29/07/2017.
 */
class CatastoReportFragment : BaseReportFragment() {

    private var mNodeRequestCallback: CatastoReportFragment.NodeCaluclationRequest? = null
    interface NodeCaluclationRequest {
        fun onClosedNodesCalculationRequested()
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try
        {
            mNodeRequestCallback = context as NodeCaluclationRequest?
        }
        catch (e : ClassCastException){
            throw ClassCastException(context!!.toString() + " must implement NodeCaluclationRequest")
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, @Nullable container: ViewGroup?, @Nullable savedInstanceState: Bundle?): View? {
        return inflateFragment(R.layout.catasto_report_layout, inflater, container)
    }

    override fun onNextClicked(callback: StepperLayout.OnNextClickedCallback?) {
        //these 2 lines update the state with "catasto" data
        getReport().reportState.generalState.catastoState = UiMapper.createCatastoStateForDomain(this)

        //callback to activity to update "sismogenetic" data
        callback?.stepperLayout?.showProgress(getString(R.string.calculating_neighbours_node))
        hideBottomActions()

        Handler().postDelayed({
            mNodeRequestCallback?.onClosedNodesCalculationRequested()
            super.onNextClicked(callback)
            callback?.stepperLayout?.hideProgress()
        }, 5000L)       //TODO this is just for ui/ux, so it displays that the calculation is going (in reality it is much faster)
    }
}