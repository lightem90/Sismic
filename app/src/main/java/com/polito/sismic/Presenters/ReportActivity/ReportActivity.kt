package com.polito.sismic.Presenters.ReportActivity

import android.content.Intent
import android.location.Location
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.places.Places
import com.polito.sismic.Domain.Report
import com.polito.sismic.Domain.ReportFactory
import com.polito.sismic.Extensions.toast
import com.polito.sismic.Presenters.Adapters.ReportFragmentsAdapter
import com.polito.sismic.Presenters.ReportActivity.Fragments.InfoLocReportFragment
import com.polito.sismic.R
import kotlinx.android.synthetic.main.activity_report.*


class ReportActivity : AppCompatActivity(), InfoLocReportFragment.OnCurrentLocationProvided, GoogleApiClient.OnConnectionFailedListener {

    private var  mGoogleApiClient: GoogleApiClient? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_report)

        //TODO: non è una factory in caso di edit, si leggeranno i dati dal db e si avrà già un reportDTO da passare ai fragment
        //questo è il caso di un report da nuovo
        var factory : ReportFactory = ReportFactory()
        var report : Report = factory.createReport(this)
        savedInstanceState?.putParcelable("report", report.DTO)

        stepperLayout.adapter = ReportFragmentsAdapter(supportFragmentManager, this)
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
}


