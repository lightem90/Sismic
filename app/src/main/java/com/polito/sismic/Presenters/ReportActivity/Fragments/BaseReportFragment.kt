package com.polito.sismic.Presenters.ReportActivity.Fragments

import android.content.Context
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.fafaldo.fabtoolbar.widget.FABToolbarLayout
import com.polito.sismic.Domain.LocationExtraInfo
import com.polito.sismic.Domain.ReportExtraInfo
import com.polito.sismic.Domain.ReportSection
import com.polito.sismic.Extensions.getFragmentState
import com.polito.sismic.Extensions.getReportExtraInfo
import com.polito.sismic.Extensions.toast
import com.polito.sismic.Interactors.Helpers.UiMapper
import com.polito.sismic.Presenters.CustomLayout.FragmentScrollableCanvas
import com.polito.sismic.R
import com.stepstone.stepper.BlockingStep
import com.stepstone.stepper.StepperLayout
import com.stepstone.stepper.VerificationError


/**
 * Created by Matteo on 29/07/2017.
 */
abstract class BaseReportFragment : Fragment(), BlockingStep {

    private var     mParametersCallback : BaseReportFragment.ParametersManager? = null
    protected var   mLocalizationInfoUser : BaseReportFragment.LocalizationInfoUser? = null
    protected var   mNodeRequestCallback: BaseReportFragment.NodeCaluclationRequest? = null
    //wrap the mapper into interactor
    protected val mUiMapper : UiMapper = UiMapper()
    protected var mFragmentState : FragmentState? = null
    protected var mExtraInfo : ReportExtraInfo? = null

    //Is the activity the handler of the dto, each fragment only passes its own
    // parameters througth the callback when the button "next" is pressed
    //Each fragment must implement the method to get their own paramter name-value
    interface ParametersManager {
        fun onParametersConfirmed(sectionParameters: ReportSection?)
        fun onParametersSaveRequest()
    }

    interface LocalizationInfoUser
    {
        fun onLocalizationDataConfirmed(locationExtraInfo: LocationExtraInfo)
    }

    interface NodeCaluclationRequest
    {
        fun onClosedNodesCalculationRequested()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        mFragmentState = arguments.getFragmentState()
        mExtraInfo = arguments.getReportExtraInfo()
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mFragmentState = arguments?.getFragmentState()
        mExtraInfo = arguments?.getReportExtraInfo()
        onParametersInjectedForEdit()
    }

    open fun reloadFragment()
    {
        return
    }

    //In this way I can make every fragment scrollable and use protected properties avoiding replicated zone
    protected fun inflateFragment(resId: Int, inflater: LayoutInflater?, container: ViewGroup?, needScrollable : Boolean = true): View? {

        //Custom view any layout with "scrollable" style
        val view = inflater!!.inflate(resId, container, false)
        //For hiding the created bottom action bar (on fab pressure)
        view.setOnClickListener({ hideBottomActions() })
        if (!needScrollable) return view

        val baseLayout = inflater.inflate(R.layout.base_report_fragment, container, false)
        val scrollableCanvas = baseLayout.findViewById<FragmentScrollableCanvas>(R.id.base_fragment_scroll_view)
        scrollableCanvas.addView(view)
        //Must be called or it crashes on scroll!!!
        scrollableCanvas.setObjectsToHideOnScroll(activity.findViewById<FABToolbarLayout>(R.id.fabtoolbar),
                activity.findViewById<FloatingActionButton>(R.id.fabtoolbar_fab),
                activity.findViewById<StepperLayout>(R.id.stepperLayout))

        //Requires api 23
        //scrollableCanvas.setOnScrollChangeListener(View.OnScrollChangeListener({
        //    view, scrollX, scrollY, oldScrollX, oldScroolY ->
        //}))
        return baseLayout
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    //maps domain values to ui (could be done by each fragment or by mapper)
    private fun onParametersInjectedForEdit() {
        mFragmentState?.mReportSectionParameters?.let {
            mUiMapper.setInjectedDomainValueForEdit(mFragmentState!!.mReportSectionParameters!!, this)
        }
    }
    //Each fragment must implement this, so the activity is in charge to save the data
    override fun onNextClicked(callback: StepperLayout.OnNextClickedCallback?) {
        mParametersCallback?.onParametersConfirmed(mUiMapper.getDomainParameterSectionFromFragment(this))
        callback!!.goToNextStep()
    }
    override fun onCompleteClicked(callback: StepperLayout.OnCompleteClickedCallback?) {
        mParametersCallback?.onParametersSaveRequest()
        callback!!.complete()
    }

    override fun onBackClicked(callback: StepperLayout.OnBackClickedCallback?) {
        callback?.goToPrevStep()
    }

    override fun onError(error: VerificationError) {
        activity.toast(error.errorMessage)
    }

    override fun onAttach(context: Context?) {

        super.onAttach(context)
        try
        {
            mParametersCallback = context as ParametersManager?
        }
        catch (e: ClassCastException) {
            throw ClassCastException(context!!.toString() + " must implement OnParametersConfirmed")
        }
        try
        {
            mLocalizationInfoUser = context as LocalizationInfoUser?
        }
        catch (e : ClassCastException){
            throw ClassCastException(context!!.toString() + " must implement LocalizationInfoUser")
        }
        try
        {
            mNodeRequestCallback = context as NodeCaluclationRequest?
        }
        catch (e : ClassCastException){
            throw ClassCastException(context!!.toString() + " must implement NodeCaluclationRequest")
        }
    }

    protected fun hideBottomActions()
    {
        activity.findViewById<FloatingActionButton>(R.id.fabtoolbar_fab)?.hide()
    }

    //Eventually in derived classes
    override fun onSelected() {    }
    override fun verifyStep(): VerificationError? { return null }
}

