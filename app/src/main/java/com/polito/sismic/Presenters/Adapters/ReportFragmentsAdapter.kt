package com.polito.sismic.Presenters.Adapters

import android.support.v4.app.FragmentManager
import com.polito.sismic.Interactors.ReportManager
import com.polito.sismic.Presenters.ReportActivity.ReportActivity
import com.polito.sismic.Presenters.ReportFragmentFactory
import com.stepstone.stepper.Step
import com.stepstone.stepper.VerificationError
import com.stepstone.stepper.adapter.AbstractFragmentStepAdapter
import com.stepstone.stepper.viewmodel.StepViewModel


/**
 * Created by Matteo on 29/07/2017.
 */
class ReportFragmentsAdapter(fragmentManager: FragmentManager, reportActivity: ReportActivity, val reportManager: ReportManager)
    : AbstractFragmentStepAdapter(fragmentManager, reportActivity) {

    private val fragmentFactory = ReportFragmentFactory(reportManager)

    override fun getCount(): Int {
        return fragmentFactory.fragmentCount()
    }

    //Non serve la factory / provider (in teoria)
    override fun createStep(position: Int): Step {
        return fragmentFactory.createFragmentForPosition(position)
    }

    override fun getViewModel(position: Int): StepViewModel {
        return fragmentFactory.createDecoratorForStep(position, context)
    }
}

class InvalidReportFragment : Step {

    override fun verifyStep(): VerificationError {
        throw ExceptionInInitializerError()
    }

    override fun onError(error: VerificationError) {
        throw ExceptionInInitializerError()
    }

    override fun onSelected() {
        throw ExceptionInInitializerError()
    }

}

