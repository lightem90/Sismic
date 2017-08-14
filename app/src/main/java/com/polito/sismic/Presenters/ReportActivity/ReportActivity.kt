package com.polito.sismic.Presenters.ReportActivity

import android.content.Intent
import android.location.Location
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.places.Places
import com.polito.sismic.Domain.ReportManager
import com.polito.sismic.Domain.ReportProvider
import com.polito.sismic.Domain.ReportSection
import com.polito.sismic.Extensions.toast
import com.polito.sismic.Interactors.Helpers.UserActionType
import com.polito.sismic.Interactors.UserActionInteractor
import com.polito.sismic.Presenters.Adapters.ReportFragmentsAdapter
import com.polito.sismic.Presenters.ReportActivity.Fragments.BaseReportFragment
import com.polito.sismic.Presenters.ReportActivity.Fragments.InfoLocReportFragment
import com.polito.sismic.R
import kotlinx.android.synthetic.main.activity_report.*


class ReportActivity : AppCompatActivity(),
        InfoLocReportFragment.CurrentLocationProvided,
        BaseReportFragment.ParametersManager,
        GoogleApiClient.OnConnectionFailedListener {

    private var  mGoogleApiClient: GoogleApiClient? = null
    private var  mUserActionInteractor: UserActionInteractor? = null
    private var  mReportManager : ReportManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_report)

        //Creating row for report in db if user is logged in
        mReportManager = ReportProvider().getOrCreateReportManager(checkLogin(), intent)
        if (mReportManager == null)
        {
            toast(R.string.error_creating_report)
            finish()
        }


        mUserActionInteractor = UserActionInteractor(mReportManager!!, this)

        stepperLayout.adapter = ReportFragmentsAdapter(supportFragmentManager, this, mReportManager!!)
        fabtoolbar_fab.setOnClickListener { fabtoolbar.show() }
        pic.setOnClickListener{ mUserActionInteractor?.onActionRequested(UserActionType.PicRequest)}
        video.setOnClickListener{ mUserActionInteractor?.onActionRequested(UserActionType.VideoRequest)}
        audio.setOnClickListener{ mUserActionInteractor?.onActionRequested(UserActionType.AudioRequest)}
        draw.setOnClickListener{ mUserActionInteractor?.onActionRequested(UserActionType.SketchRequest)}
        note.setOnClickListener{ mUserActionInteractor?.onActionRequested(UserActionType.NoteRequest)}

        mGoogleApiClient = GoogleApiClient.Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .enableAutoManage(this, this)
                .build()

    }

    private fun checkLogin() : String {
        var userName = intent.getStringExtra("USER_NAME")
        if (userName == null || userName.isEmpty())
        {
            toast(R.string.no_login)
            finish()
        }
        return userName
    }

    //override fun onSaveInstanceState(outState: Bundle?) {
    //    super.onSaveInstanceState(outState)
    //    outState?.putParcelable("report", mReportManager!!.getParcelable())
    //}

    override fun onLocationAcquired(location: Location) {
        supportFragmentManager.fragments.filterIsInstance<InfoLocReportFragment>().first().updateByLocation(location)
    }

    //If I pass an Uri, data will be null
    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        mUserActionInteractor?.onActionResponse(requestCode, resultCode, data)
    }

    override fun onConnectionFailed(p0: ConnectionResult) {
        toast(R.string.google_api_error)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if(item?.itemId == android.R.id.home) {
            mUserActionInteractor?.onActionRequested(UserActionType.BackRequest)
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    //Updates the dto
    override fun onParametersConfirmed(sectionParametr : ReportSection) {
        //todo
        mReportManager!!.tmpSectionList.add(sectionParametr)
    }

    override fun onParametersSaveRequest() {
        mReportManager!!.saveReportToDb()
    }
}


