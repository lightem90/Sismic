package com.polito.sismic.Presenters.ReportActivity

import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.polito.sismic.Interactors.Helpers.PermissionsHelper
import com.polito.sismic.Presenters.Adapters.ReportFragmentsAdapter
import com.polito.sismic.Presenters.ReportActivity.Fragments.InfoLocReportFragment
import com.polito.sismic.R
import kotlinx.android.synthetic.main.activity_report.*


class ReportActivity : AppCompatActivity(), InfoLocReportFragment.OnCurrentLocationProvided {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_report)

        stepperLayout.adapter = ReportFragmentsAdapter(supportFragmentManager, this)
        fabtoolbar_fab.setOnClickListener { fabtoolbar.show() }
    }

    override fun onLocationAcquired(location: Location) {
        supportFragmentManager.fragments.filterIsInstance<InfoLocReportFragment>().first().updateByLocation(location)
    }

}


