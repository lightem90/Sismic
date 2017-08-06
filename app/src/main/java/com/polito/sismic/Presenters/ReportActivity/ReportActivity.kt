package com.polito.sismic.Presenters.ReportActivity

import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.polito.sismic.Interactors.Helpers.PermissionsHelper
import com.polito.sismic.Presenters.Adapters.ReportFragmentsAdapter
import com.polito.sismic.R
import kotlinx.android.synthetic.main.activity_report.*


class ReportActivity : AppCompatActivity() {


    private var  mPermissionHelper = PermissionsHelper()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_report)
        //stepperLayout.setListener(this);
        stepperLayout.setAdapter(ReportFragmentsAdapter(supportFragmentManager, this))

        fabtoolbar_fab.setOnClickListener { fabtoolbar.show() }
        mPermissionHelper.checkPermissions(this)
    }

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            mPermissionHelper.PERMISSION_POSITION -> {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! do the
                    // calendar task you need to do.

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return
            }

            mPermissionHelper.PERMISSION_INTERNET -> {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! do the
                    // calendar task you need to do.

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return
            }
        }
    }
}


