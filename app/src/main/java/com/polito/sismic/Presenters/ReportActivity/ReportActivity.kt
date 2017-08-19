package com.polito.sismic.Presenters.ReportActivity

import android.content.Intent
import android.location.Location
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.places.Places
import com.polito.sismic.Interactors.ReportManager
import com.polito.sismic.Interactors.ReportProvider
import com.polito.sismic.Domain.ReportSection
import com.polito.sismic.Extensions.toast
import com.polito.sismic.Interactors.DomainInteractor
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

    private lateinit var  mGoogleApiClient: GoogleApiClient
    private lateinit var  mUserActionInteractor: UserActionInteractor
    private lateinit var  mDomainInteractor : DomainInteractor
    private var  mReportManager : ReportManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_report)

        //Creating row for report in db if user is logged in
        mReportManager = ReportProvider(this).getOrCreateReportManager(checkLogin(), intent)

        mReportManager?.let {
            //means i'm editing
            initializeFromManager(mReportManager!!)
        }

    }

    private fun checkLogin() : String {
        val userName = intent.getStringExtra("username")
        if (userName == null || userName.isEmpty())
        {
            toast(R.string.no_login)
            finish()
        }
        return userName
    }

    override fun onLocationAcquired(location: Location) {
        supportFragmentManager.fragments.filterIsInstance<InfoLocReportFragment>().first().updateByLocation(location)
    }

    //If I pass an Uri, data will be null
    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        mUserActionInteractor.onActionResponse(requestCode, resultCode, data)
    }

    override fun onConnectionFailed(p0: ConnectionResult) {
        toast(R.string.google_api_error)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if(item?.itemId == android.R.id.home) {
            mUserActionInteractor.onActionRequested(UserActionType.BackRequest)
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onParametersSaveRequest() {
        mUserActionInteractor.saveReport()
        finish()
    }

    override fun onParametersConfirmed(sectionParameters: ReportSection?) {
        sectionParameters?.let { mDomainInteractor.addDomainReportSection(sectionParameters) }
    }

    fun onNewReportConfirmed(createFromNew: ReportManager): ReportManager? {
        initializeFromManager(createFromNew)
        return createFromNew
    }

    private fun initializeFromManager(reportManager: ReportManager)
    {
        //To handle user action, it uses other interactor to pilot the ui changes to the domain
        mUserActionInteractor = UserActionInteractor(reportManager, this)
        mDomainInteractor = DomainInteractor(reportManager)
        stepperLayout.adapter = ReportFragmentsAdapter(supportFragmentManager, this, reportManager)
        fabtoolbar_fab.setOnClickListener { fabtoolbar.show() }
        pic.setOnClickListener{ mUserActionInteractor.onActionRequested(UserActionType.PicRequest)}
        video.setOnClickListener{ mUserActionInteractor.onActionRequested(UserActionType.VideoRequest)}
        audio.setOnClickListener{ mUserActionInteractor.onActionRequested(UserActionType.AudioRequest)}
        draw.setOnClickListener{ mUserActionInteractor.onActionRequested(UserActionType.SketchRequest)}
        note.setOnClickListener{ mUserActionInteractor.onActionRequested(UserActionType.NoteRequest)}

        mGoogleApiClient = GoogleApiClient.Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .enableAutoManage(this, this)
                .build()
    }
}


