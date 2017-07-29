package com.polito.sismic.Presenters

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.polito.sismic.Presenters.Adapters.ReportFragmentsAdapter
import com.polito.sismic.R
import com.stepstone.stepper.StepperLayout
import com.stepstone.stepper.VerificationError
import kotlinx.android.synthetic.main.activity_report.*

class ReportActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_report)
        //stepperLayout.setListener(this);
        stepperLayout.setAdapter(ReportFragmentsAdapter(supportFragmentManager, this))

        fabtoolbar.hide()
        fabtoolbar_fab.setOnClickListener { fabtoolbar.show() }
    }
}

