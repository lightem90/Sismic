package com.polito.sismic.Presenters.ReportActivity.Fragments

import android.os.Bundle
import android.support.annotation.Nullable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.polito.sismic.Extensions.hideSoftKeyboard
import com.polito.sismic.Interactors.Helpers.UiMapper
import com.polito.sismic.R
import com.stepstone.stepper.StepperLayout
import com.stepstone.stepper.VerificationError
import kotlinx.android.synthetic.main.maglia_strutt_report_layout.*

class MagliaStrutturaleReportFragment : BaseReportFragment() {

    override fun onCreateView(inflater: LayoutInflater?, @Nullable container: ViewGroup?, @Nullable savedInstanceState: Bundle?): View? {
        return inflateFragment(R.layout.maglia_strutt_report_layout, inflater, container)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        num_x.attachDataConfirmedCallback { dist_x.requestFocus() }
        dist_x.attachDataConfirmedCallback { num_y.requestFocus() }
        num_y.attachDataConfirmedCallback { dist_y.requestFocus() }
        dist_y.attachDataConfirmedCallback { if (!it.isEmpty()) activity.hideSoftKeyboard() }
    }

    override fun verifyStep(): VerificationError? {
        if (num_x.isEmpty()) return VerificationError(String.format(resources.getString(R.string.verification_empty_field), num_x.getTitle()))
        if (dist_x.isEmpty()) return VerificationError(String.format(resources.getString(R.string.verification_empty_field), dist_x.getTitle()))
        if (num_y.isEmpty()) return VerificationError(String.format(resources.getString(R.string.verification_empty_field), num_y.getTitle()))
        if (dist_y.isEmpty()) return VerificationError(String.format(resources.getString(R.string.verification_empty_field), dist_y.getTitle()))
        return null
    }

    //callback to activity updates domain instance for activity and all existing and future fragments
    override fun onNextClicked(callback: StepperLayout.OnNextClickedCallback?) {
        getReport().reportState.buildingState.pillarLayoutState = UiMapper.createPillarLayoutStateForDomain(this)
        super.onNextClicked(callback)
    }
}