package com.polito.sismic.Presenters.ReportActivity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.polito.sismic.Presenters.Adapters.ReportFragmentsAdapter
import com.polito.sismic.R
import kotlinx.android.synthetic.main.activity_report.*
import android.view.MotionEvent



class ReportActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_report)
        //stepperLayout.setListener(this);
        stepperLayout.setAdapter(ReportFragmentsAdapter(supportFragmentManager, this))

        fabtoolbar_fab.setOnClickListener { fabtoolbar.show() }
    }
}


