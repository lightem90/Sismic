package com.polito.sismic.Presenters

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.polito.sismic.R
import com.stepstone.stepper.StepperLayout
import com.stepstone.stepper.VerificationError
import kotlinx.android.synthetic.main.activity_report.*

class ReportActivity : AppCompatActivity(), StepperLayout.StepperListener {

    override fun onError(verificationError: VerificationError?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
    override fun onReturn() {
        finish()
    }
    override fun onCompleted(completeButton: View?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
    override fun onStepSelected(newStepPosition: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_report)
        stepperLayout.setListener(this);

        fabtoolbar.hide()
        fabtoolbar_fab.setOnClickListener(View.OnClickListener { fabtoolbar.show() })
    }
}
