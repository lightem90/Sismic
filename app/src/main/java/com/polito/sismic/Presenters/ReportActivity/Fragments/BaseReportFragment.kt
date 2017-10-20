package com.polito.sismic.Presenters.ReportActivity.Fragments

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineDataSet
import com.polito.sismic.Domain.PillarDomain
import com.polito.sismic.Domain.Report
import com.polito.sismic.Extensions.getReport
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

//base class for all report fragment to hide communication with activity and make every fragment scrollable
abstract class BaseReportFragment : Fragment(), BlockingStep {

    private var mPdfWriterCallback: BaseReportFragment.PdfWriterManager? = null
    private var mParametersCallback: BaseReportFragment.ParametersManager? = null
    private var mReport: Report? = null

    fun getReport(): Report {
        return mReport!!        //ALWAYS TRUE!
    }

    //Is the activity the handler of the dto, each fragment only passes its own
    // parameters througth the callback when the button "next" is pressed
    //Each fragment must implement the method to get their own paramter name-value
    interface ParametersManager {
        fun onParametersConfirmed(report: Report, needReload : Boolean)
        fun onParametersSaveRequest()
    }

    interface PdfWriterManager {
        fun onSavePageRequest(fragmentView: View?, fragName: String)
    }

    //domain report from bundle
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mReport = arguments.getReport()
    }

    //injects parameters to view from domain (needs some fixing, for example the user should not update or re / import data when editing,
    //the problem is that the activity doesn't know when a value is changed and needs to update the view (could this be done just always?)
    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mReport = arguments?.getReport()
        onParametersInjectedForEdit()
    }

    //binds domain values to ui (done by each fragment by mapper)
    protected open fun onParametersInjectedForEdit() {
        UiMapper.bindToDomain(this, getReport().reportState)
    }

    //In this way I can make every fragment scrollable and use protected properties avoiding replicated zone
    protected fun inflateFragment(resId: Int, inflater: LayoutInflater?, container: ViewGroup?, needScrollable: Boolean = true): View? {

        //Custom view any layout with "scrollable" style
        val view = inflater!!.inflate(resId, container, false)
        //For hiding the created bottom action bar (on fab pressure)
        view.setOnClickListener({ hideBottomActions() })
        if (!needScrollable) return view

        val baseLayout = inflater.inflate(R.layout.base_report_fragment, container, false)
        val scrollableCanvas = baseLayout.findViewById<FragmentScrollableCanvas>(R.id.base_fragment_scroll_view)
        scrollableCanvas.addView(view)
        //Must be called or it crashes on scroll!!!
        scrollableCanvas.setObjectsToHideOnScroll(activity.findViewById(R.id.fabtoolbar),
                activity.findViewById(R.id.fabtoolbar_fab),
                activity.findViewById<StepperLayout>(R.id.stepperLayout))

        //Requires api 23
        //scrollableCanvas.setOnScrollChangeListener(View.OnScrollChangeListener({
        //    view, scrollX, scrollY, oldScrollX, oldScroolY ->
        //}))
        return baseLayout
    }

    //callback to activity updates domain instance of each fragment.
    //in this way activity and fragments work on the same data
    override fun onNextClicked(callback: StepperLayout.OnNextClickedCallback?) {
        mParametersCallback?.onParametersConfirmed(getReport(), onNeedReload())
        mPdfWriterCallback?.onSavePageRequest(view?.findViewById(R.id.base_fragment_scroll_view), javaClass.canonicalName)
        callback!!.goToNextStep()
    }

    //utility
    protected open fun onNeedReload(): Boolean
    {
        return false
    }

    //actions to update user interface when domain parameters change
    protected open fun onReload()    {    }
    fun reloadFragmentFromCallback(newReportState: Report) {
        mReport = newReportState
        onReload()
    }

    override fun onBackClicked(callback: StepperLayout.OnBackClickedCallback?) {
        callback?.goToPrevStep()
    }

    override fun onError(error: VerificationError) {
        activity.toast(error.errorMessage)
    }


    override fun onAttach(context: Context?) {

        super.onAttach(context)
        try {
            mParametersCallback = context as ParametersManager?
        } catch (e: ClassCastException) {
            throw ClassCastException(context!!.toString() + " must implement OnParametersConfirmed")
        }

        try {
            mPdfWriterCallback = context as PdfWriterManager?
        } catch (e: ClassCastException) {
            throw ClassCastException(context!!.toString() + " must implement OnParametersConfirmed")
        }

    }

    //utility
    protected fun hideBottomActions() {
        activity.findViewById<FloatingActionButton>(R.id.fabtoolbar_fab)?.hide()
    }

    //utility
    protected fun showBottomActions() {
        activity.findViewById<FloatingActionButton>(R.id.fabtoolbar_fab)?.show()
    }

    //Eventually in derived classes
    override fun onSelected() {}

    //every child fragment has its own
    override fun verifyStep(): VerificationError? {
        return null
    }

    //save report
    override fun onCompleteClicked(callback: StepperLayout.OnCompleteClickedCallback?) {
        mParametersCallback?.onParametersSaveRequest()
        callback!!.complete()
    }

    protected fun buildPillarDomainForUi(pillarDomain: PillarDomain, addSismicStatePoints: Boolean = false): MutableList<LineDataSet> {
        val upPointList = mutableListOf<Entry>()
        pillarDomain.domainPoints.forEach { point ->
            upPointList.add(Entry(point.n.toFloat(), point.m.toFloat()))
        }

        val domainGraph = mutableListOf<LineDataSet>()

        if (addSismicStatePoints) {
            domainGraph.addAll(pillarDomain.limitStatePoints.map {
                LineDataSet(listOf(Entry(it.n.toFloat(), it.m.toFloat())), it.label).apply {
                    setDrawCircles(true)
                    circleRadius = 10f
                    circleColors = listOf(ContextCompat.getColor(context, it.color))
                    axisDependency = YAxis.AxisDependency.LEFT
                }
            }
            )
        }


        val UiDomainUp = LineDataSet(upPointList, "").apply {
            color = Color.BLUE
            setDrawCircles(false)
            lineWidth = 3f
            axisDependency = YAxis.AxisDependency.LEFT
        }

        //Just ui stuff to show the simmetric
        val downList = mutableListOf<Entry>()
        pillarDomain.domainPoints.forEach { point ->
            downList.add(Entry(point.n.toFloat(), -point.m.toFloat()))
        }

        val UiDomainDown = LineDataSet(downList, "").apply {
            color = Color.BLUE
            setDrawCircles(false)
            lineWidth = 3f
            axisDependency = YAxis.AxisDependency.LEFT
        }

        domainGraph.add(UiDomainUp)
        domainGraph.add(UiDomainDown)
        return domainGraph
    }
}

