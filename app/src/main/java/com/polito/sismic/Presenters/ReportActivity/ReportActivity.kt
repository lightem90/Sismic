package com.polito.sismic.Presenters.ReportActivity

import android.content.Intent
import android.location.Location
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.places.Places
import com.polito.sismic.Domain.ReportManager
import com.polito.sismic.Domain.ReportProvider
import com.polito.sismic.Extensions.toast
import com.polito.sismic.Presenters.Adapters.ReportFragmentsAdapter
import com.polito.sismic.Presenters.ReportActivity.Fragments.InfoLocReportFragment
import com.polito.sismic.R
import kotlinx.android.synthetic.main.activity_report.*
import android.content.DialogInterface
import android.support.v7.app.AlertDialog
import android.view.KeyEvent
import android.view.KeyEvent.KEYCODE_BACK
import android.view.MenuItem


class ReportActivity : AppCompatActivity(),
        InfoLocReportFragment.OnCurrentLocationProvided,
        GoogleApiClient.OnConnectionFailedListener {

    private var  mGoogleApiClient: GoogleApiClient? = null
    private var mReportManager: ReportManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_report)

        //questo è il caso di un mReportManager da nuovo
        mReportManager = ReportProvider.createReport(this)
        savedInstanceState?.putParcelable("mReportManager", mReportManager!!.DTO)

        stepperLayout.adapter = ReportFragmentsAdapter(supportFragmentManager, this, mReportManager!!)
        fabtoolbar_fab.setOnClickListener { fabtoolbar.show() }

        mGoogleApiClient = GoogleApiClient.Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .enableAutoManage(this, this)
                .build()

    }

    override fun onLocationAcquired(location: Location) {
        supportFragmentManager.fragments.filterIsInstance<InfoLocReportFragment>().first().updateByLocation(location)
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onConnectionFailed(p0: ConnectionResult) {
        toast(R.string.google_api_error)
    }

    fun BackPressed() {
        AlertDialog.Builder(this)
                .setTitle(R.string.confirm_report_back)
                .setMessage(R.string.confirm_report_back_message)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.yes, DialogInterface.OnClickListener { dialog, _ -> mReportManager!!.deleteReport(); finish()})
                .setNegativeButton(android.R.string.no, null)
                .show()
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if(item?.itemId == android.R.id.home) {
            BackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }


}


