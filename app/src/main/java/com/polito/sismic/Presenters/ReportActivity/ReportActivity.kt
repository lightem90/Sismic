package com.polito.sismic.Presenters.ReportActivity

import android.content.Intent
import android.location.Location
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.places.Places
import com.polito.sismic.Domain.ReportDTO
import com.polito.sismic.Domain.ReportManager
import com.polito.sismic.Domain.ReportProvider
import com.polito.sismic.Extensions.toast
import com.polito.sismic.Interactors.Helpers.UserActionType
import com.polito.sismic.Interactors.UserActionInteractor
import com.polito.sismic.Presenters.Adapters.ReportFragmentsAdapter
import com.polito.sismic.Presenters.ReportActivity.Fragments.BaseReportFragment
import com.polito.sismic.Presenters.ReportActivity.Fragments.InfoLocReportFragment
import com.polito.sismic.R
import kotlinx.android.synthetic.main.activity_report.*


class ReportActivity : AppCompatActivity(),
        InfoLocReportFragment.OnCurrentLocationProvided,
        BaseReportFragment.OnParametersConfirmed,
        GoogleApiClient.OnConnectionFailedListener {


    private var  mGoogleApiClient: GoogleApiClient? = null
    private var  mReportManager: ReportManager? = null
    private var  mUserActionInteractor: UserActionInteractor? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_report)

        var reportDTO = savedInstanceState?.getParcelable<ReportDTO>("report")
        if (reportDTO == null)      //new report
        {
            mReportManager = ReportProvider.createReport(this)
        }
        else
        {
            //Edit TODO: handle filling of data in fragments
            mReportManager = ReportProvider.createFromDTO(this, reportDTO)
        }

        mUserActionInteractor = UserActionInteractor(mReportManager!!)

        stepperLayout.adapter = ReportFragmentsAdapter(supportFragmentManager, this, mReportManager!!)
        fabtoolbar_fab.setOnClickListener { fabtoolbar.show() }
        pic.setOnClickListener{ mUserActionInteractor?.onActionRequested(this, UserActionType.PicRequest)}
        video.setOnClickListener{ mUserActionInteractor?.onActionRequested(this, UserActionType.VideoRequest)}
        audio.setOnClickListener{ mUserActionInteractor?.onActionRequested(this, UserActionType.AudioRequest)}
        draw.setOnClickListener{ mUserActionInteractor?.onActionRequested(this, UserActionType.SketchRequest)}
        note.setOnClickListener{ mUserActionInteractor?.onActionRequested(this, UserActionType.NoteRequest)}

        mGoogleApiClient = GoogleApiClient.Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .enableAutoManage(this, this)
                .build()

    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        outState?.putParcelable("report", mReportManager!!.DTO)
    }


    override fun onLocationAcquired(location: Location) {
        supportFragmentManager.fragments.filterIsInstance<InfoLocReportFragment>().first().updateByLocation(location)
    }

    //If I pass an Uri, data will be null
    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        //TODO the audio file could be saved into custom location, handle the case!
        mUserActionInteractor?.onActionResponse(resultCode)
    }

    override fun onConnectionFailed(p0: ConnectionResult) {
        toast(R.string.google_api_error)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if(item?.itemId == android.R.id.home) {
            mUserActionInteractor?.onActionRequested(this, UserActionType.BackRequest)
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    //Updates the dto
    override fun onParametersConfirmed(paramList: MutableList<Pair<String, Object>>) {
        paramList.forEach{x -> mReportManager!!.setValue(x.first, x.second)}
    }
}


