package com.polito.sismic.Presenters

import android.support.v4.app.Fragment
import com.stepstone.stepper.Step
import com.stepstone.stepper.VerificationError
import android.os.Bundle
import android.support.annotation.Nullable
import android.view.ViewGroup
import android.view.LayoutInflater
import android.view.View
import com.polito.sismic.R


/**
 * Created by Matteo on 29/07/2017.
 */
class InfoLocReportFragment : Fragment(), Step {

    override fun onCreateView(inflater: LayoutInflater?, @Nullable container: ViewGroup?, @Nullable savedInstanceState: Bundle?): View? {
        val v = inflater!!.inflate(R.layout.info_loc_report_layout, container, false)


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