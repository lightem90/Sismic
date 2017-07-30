package com.polito.sismic.Presenters.ReportActivity.Fragments

import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ScrollView
import com.github.fafaldo.fabtoolbar.widget.FABToolbarLayout
import com.polito.sismic.R
import com.stepstone.stepper.Step
import com.stepstone.stepper.VerificationError


/**
 * Created by Matteo on 29/07/2017.
 */
open class BaseReportFragment : Fragment(), Step {

    //in questo modo riesco a inserire il layout dei fragment "figli" nella scrollview base in modo da rendere tutto scrollable
    //e gestisco il click sulla view che resetta lo stato della toolbar delle azioni
    //in questo caso composition over inheritance non è valido
    protected fun  inflateFragment(resId: Int, inflater: LayoutInflater?, container: ViewGroup?): View? {

        var view = inflater!!.inflate(resId, container, false)
        val baseLayout = inflater!!.inflate(R.layout.base_report_fragment, container, false)

        val scrollableCanvas = baseLayout.findViewById<ScrollView>(R.id.base_fragment_scroll_view)

        view.setOnClickListener({ activity.findViewById<FABToolbarLayout>(R.id.fabtoolbar).hide() })
        scrollableCanvas.addView(view)

        return baseLayout
    }

    //Da overridare nei figli
    override fun onSelected() {    }
    override fun verifyStep(): VerificationError? { return null }
    override fun onError(error: VerificationError) { }
}