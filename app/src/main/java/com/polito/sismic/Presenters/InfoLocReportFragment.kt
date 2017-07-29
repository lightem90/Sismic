package com.polito.sismic.Presenters

import android.os.Bundle
import android.support.annotation.Nullable
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.fafaldo.fabtoolbar.widget.FABToolbarLayout
import com.polito.sismic.R
import com.stepstone.stepper.Step
import com.stepstone.stepper.VerificationError
import kotlinx.android.synthetic.main.info_loc_report_layout.*


/**
 * Created by Matteo on 29/07/2017.
 */
class InfoLocReportFragment : Fragment(), Step {


    override fun onCreateView(inflater: LayoutInflater?, @Nullable container: ViewGroup?, @Nullable savedInstanceState: Bundle?): View? {
        val v = inflater!!.inflate(R.layout.info_loc_report_layout, container, false)
        v.setOnClickListener({ activity.findViewById<FABToolbarLayout>(R.id.fabtoolbar).hide() })

        return v
    }

    override fun onSelected() {

    }

    override fun verifyStep(): VerificationError? {
        return null
    }

    override fun onError(error: VerificationError) {

    }

}

