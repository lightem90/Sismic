package com.polito.sismic.Presenters.ReportActivity

import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.polito.sismic.Interactors.Helpers.PermissionsHelper
import com.polito.sismic.Presenters.Adapters.ReportFragmentsAdapter
import com.polito.sismic.Presenters.ReportActivity.Fragments.InfoLocReportFragment
import com.polito.sismic.R
import kotlinx.android.synthetic.main.activity_report.*


class ReportActivity : AppCompatActivity() {


    private var  mPermissionHelper = PermissionsHelper()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_report)

        stepperLayout.adapter = ReportFragmentsAdapter(supportFragmentManager, this)
        fabtoolbar_fab.setOnClickListener { fabtoolbar.show() }
    }

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        val fragments = supportFragmentManager.fragments
        fragments
                //L'unico che richiede i permessi
                .filterIsInstance<InfoLocReportFragment>()
                .forEach { it.onRequestPermissionsResult(requestCode, permissions, grantResults) }
    }
}


