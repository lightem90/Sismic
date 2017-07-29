package com.polito.sismic.Presenters.Adapters

import android.os.Bundle
import android.support.annotation.Nullable
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.polito.sismic.R
import com.stepstone.stepper.Step
import com.stepstone.stepper.VerificationError

/**
 * Created by Matteo on 29/07/2017.
 */
class DatiSismoGeneticiFragment: Fragment(), Step {

    override fun onCreateView(inflater: LayoutInflater?, @Nullable container: ViewGroup?, @Nullable savedInstanceState: Bundle?): View? {
        val v = inflater!!.inflate(R.layout.dati_sismogenetici_fragment, container, false)


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