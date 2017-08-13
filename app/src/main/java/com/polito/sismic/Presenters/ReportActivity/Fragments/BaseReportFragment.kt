package com.polito.sismic.Presenters.ReportActivity.Fragments

import android.content.Context
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.fafaldo.fabtoolbar.widget.FABToolbarLayout
import com.polito.sismic.Domain.ReportDTO
import com.polito.sismic.Domain.ReportManager
import com.polito.sismic.Presenters.CustomLayout.FragmentScrollableCanvas
import com.polito.sismic.R
import com.stepstone.stepper.BlockingStep
import com.stepstone.stepper.StepperLayout
import com.stepstone.stepper.VerificationError


/**
 * Created by Matteo on 29/07/2017.
 */
abstract class BaseReportFragment : Fragment(), BlockingStep {

    private var mParametersCallback : BaseReportFragment.ParametersManager? = null
    protected var mReportManager : ReportManager? = null

    //Is' the activity the handler of the dto, each fragment only passes its own
    // parameters througth the callback when the button "next" is pressed
    //Each fragment must implement the method to get their own paramter name-value
    interface ParametersManager {
        fun onParametersConfirmed(paramList : MutableList<Pair<Int, String>>)
        fun onParametersSaveRequest()
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    //In this way I can make every fragment scrollable and use protected properties avoiding replicated code
    protected fun  inflateFragment(resId: Int, inflater: LayoutInflater?, container: ViewGroup?): View? {

        //Custom view any layout with "scrollable" style
        var view = inflater!!.inflate(resId, container, false)
        val baseLayout = inflater.inflate(R.layout.base_report_fragment, container, false)
        val scrollableCanvas = baseLayout.findViewById<FragmentScrollableCanvas>(R.id.base_fragment_scroll_view)

        //For hiding the created bottom action bar (on fab pressure)
        view.setOnClickListener({ hideBottomActions() })
        scrollableCanvas.addView(view)
        //Must be called or it crashes on scroll!!!
        scrollableCanvas.setObjectsToHideOnScroll(activity.findViewById<FABToolbarLayout>(R.id.fabtoolbar),
                activity.findViewById<FloatingActionButton>(R.id.fabtoolbar_fab),
                activity.findViewById<StepperLayout>(R.id.stepperLayout))

        //In the case I'm editing, it must visualize the data already saved!
        var dtoForHeader = arguments.getParcelable<ReportDTO>("report")
        mReportManager = ReportManager(context, dtoForHeader)
        if (dtoForHeader != null) onInitializeParametersForEdit(view, mReportManager!!)

        //Requires api 23
        //scrollableCanvas.setOnScrollChangeListener(View.OnScrollChangeListener({
        //    view, scrollX, scrollY, oldScrollX, oldScroolY ->
        //}))
        return baseLayout
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    abstract fun onInitializeParametersForEdit(inflatingView: View, reportManager: ReportManager)
    //Each fragment must implement this, so the activity is in charge to save the data
    abstract fun getAllViewParameters() : MutableList<Pair<Int, String>>
    override fun onNextClicked(callback: StepperLayout.OnNextClickedCallback?) {
        mParametersCallback?.onParametersConfirmed(getAllViewParameters())
        callback!!.goToNextStep()
    }
    override fun onCompleteClicked(callback: StepperLayout.OnCompleteClickedCallback?) {
        callback!!.complete()
    }

    override fun onBackClicked(callback: StepperLayout.OnBackClickedCallback?) {
        callback?.goToPrevStep()
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
    }

    protected fun hideBottomActions()
    {
        activity.findViewById<FloatingActionButton>(R.id.fabtoolbar_fab)?.hide()
    }

    //Eventually in derived classes
    override fun onSelected() {    }
    override fun verifyStep(): VerificationError? { return null }
    override fun onError(error: VerificationError) { }
}

