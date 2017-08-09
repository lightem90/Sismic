package com.polito.sismic.Presenters.ReportActivity.Fragments

import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ScrollView
import com.github.fafaldo.fabtoolbar.widget.FABToolbarLayout
import com.polito.sismic.Domain.ReportDTO
import com.polito.sismic.Domain.ReportManager
import com.polito.sismic.Domain.ReportProvider
import com.polito.sismic.Presenters.CustomLayout.FragmentScrollableCanvas
import com.polito.sismic.R
import com.stepstone.stepper.Step
import com.stepstone.stepper.StepperLayout
import com.stepstone.stepper.VerificationError


/**
 * Created by Matteo on 29/07/2017.
 */
open class BaseReportFragment : Fragment(), Step {

    protected var mReportManager : ReportManager? = null

    //I need a report manager in every fragment to update parameters on step confirmation
    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var reportDTO = arguments.getParcelable<ReportDTO>("report")
        mReportManager = ReportProvider.createFromDTO(context, reportDTO!!)
    }

    //In this way I can make every fragment scrollable and use protected properties avoiding replicated code
    protected fun  inflateFragment(resId: Int, inflater: LayoutInflater?, container: ViewGroup?): View? {

        //Custom view any layout with "scrollable" style
        var view = inflater!!.inflate(resId, container, false)
        val baseLayout = inflater.inflate(R.layout.base_report_fragment, container, false)
        val scrollableCanvas = baseLayout.findViewById<FragmentScrollableCanvas>(R.id.base_fragment_scroll_view)

        view.setOnClickListener({ hideFab() })
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

    protected fun hideStepper() {
        activity.findViewById<StepperLayout>(R.id.stepperLayout)?.hideProgress()
    }

    protected fun hideFabToolbar() {
        activity.findViewById<FABToolbarLayout>(R.id.fabtoolbar)?.hide()
    }

    protected fun hideFab()
    {
        activity.findViewById<FloatingActionButton>(R.id.fabtoolbar_fab)?.hide()
    }

    protected fun showFab()
    {
        activity.findViewById<FloatingActionButton>(R.id.fabtoolbar_fab)?.show()
    }

    //Eventually in derived classes
    override fun onSelected() {    }
    override fun verifyStep(): VerificationError? { return null }
    override fun onError(error: VerificationError) { }
}

