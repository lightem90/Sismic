package com.polito.sismic.Presenters.ReportActivity.Fragments

import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ScrollView
import com.github.fafaldo.fabtoolbar.widget.FABToolbarLayout
import com.polito.sismic.R
import com.stepstone.stepper.Step
import com.stepstone.stepper.StepperLayout
import com.stepstone.stepper.VerificationError


/**
 * Created by Matteo on 29/07/2017.
 */
open class BaseReportFragment : Fragment(), Step {

    //in questo modo riesco a inserire il layout dei fragment "figli" nella scrollview base in modo da rendere tutto scrollable
    //e gestisco il click sulla view che resetta lo stato della toolbar delle azioni
    //in questo caso composition over inheritance non è valido
    protected fun  inflateFragment(resId: Int, inflater: LayoutInflater?, container: ViewGroup?): View? {

        //Custom view (qualsiasi tipo di layout LL, RL, Coordinator etc.., basta che utilizzi lo style "scrollableLayout")
        var view = inflater!!.inflate(resId, container, false)
        //Base view (intero layout)
        val baseLayout = inflater.inflate(R.layout.base_report_fragment, container, false)
        //Elemento padre della custom view
        val scrollableCanvas = baseLayout.findViewById<ScrollView>(R.id.base_fragment_scroll_view)

        //TODO detect scrolling e apertura keyboard
        view.setOnClickListener({ hideFab() })
        scrollableCanvas.addView(view)

        scrollableCanvas.viewTreeObserver.addOnScrollChangedListener {
            hideStepper()
            hideFab()
        }

        return baseLayout
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    protected fun hideStepper() {
        activity.findViewById<StepperLayout>(R.id.stepperLayout)?.hideProgress()
    }

    protected fun hideFab() {
        activity.findViewById<FABToolbarLayout>(R.id.fabtoolbar)?.hide()
    }

    //Da overridare nei figli
    override fun onSelected() {    }
    override fun verifyStep(): VerificationError? { return null }
    override fun onError(error: VerificationError) { }
}