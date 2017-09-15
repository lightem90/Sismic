package com.polito.sismic.Presenters.ReportActivity

import android.content.Intent
import android.location.Location
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.places.Places
import com.polito.sismic.Domain.Report
import com.polito.sismic.Extensions.getCustomAdapter
import com.polito.sismic.Extensions.toast
import com.polito.sismic.Interactors.*
import com.polito.sismic.Interactors.SismicActionParametersInteractor
import com.polito.sismic.Interactors.Helpers.UserActionType
import com.polito.sismic.Presenters.Adapters.ReportFragmentsAdapter
import com.polito.sismic.Presenters.ReportActivity.Fragments.*
import com.polito.sismic.R
import kotlinx.android.synthetic.main.activity_report.*


//Activity is in charge of handling all data through interactors, then it signals fragment to update their copy of domain value and its grafic
class ReportActivity : AppCompatActivity(),
        BaseReportFragment.ParametersManager,
        InfoLocReportFragment.CurrentLocationProvided,
        CatastoReportFragment.NodeCaluclationRequest,
        DatiSismoGeneticiReportFragment.DefaultReturnTimeRequest,
        ParametriSismiciReportFragment.LimitStateRequest,
        SpettriDiProgettoReportFragment.SpectrumReturnTimeRequest,
        GoogleApiClient.OnConnectionFailedListener
{

    private lateinit var mGoogleApiClient: GoogleApiClient
    private lateinit var mUserActionInteractor: UserActionInteractor
    private lateinit var mSismicParameterInteractor: SismicActionParametersInteractor
    private var mReportManager: ReportManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_report)

        //Creating row for report in db if user is logged in
        mReportManager = ReportProvider(this).getOrCreateReportManager(checkLogin(), intent)
        mReportManager?.let {
            //means i'm editing
            initializeFromManager(it)
        }
    }

    private fun checkLogin(): String {
        val userName = intent.getStringExtra("username")
        if (userName == null || userName.isEmpty()) {
            toast(R.string.no_login)
            finish()
        }
        return userName
    }

    //User requested help to input coordinates
    override fun onLocationAcquired(location: Location) {
        //Can do this, called into this very same fragment, so I'm sure it lives
        supportFragmentManager.fragments
                .filterIsInstance<InfoLocReportFragment>()
                .firstOrNull()
                ?.updateByLocation(location)
    }

    private fun updateStateFromCallback() {
        updateStateForFragments(true)
    }

    //in this way sismic data are not recalculated if latitude or longitude doesn't change
    override fun onCoordinatesUpdated() {
        mSismicParameterInteractor.mustRecalcReturnTimesParameters()
    }

    //requested sismic data by each fragment. ui update is useless since data dont go into ui
    //TODO: add data to "note" so the user can check
    override fun onClosedNodesCalculationRequested() {
        mSismicParameterInteractor.calculateReturnPeriodsParameters()
        updateStateFromCallback()
    }

    override fun onDefaultReturnTimesRequested(): List<ILineDataSet> = with(mSismicParameterInteractor){
        getDefaultSpectrumLines(mReportManager.report.reportState)
    }

    override fun onReturnTimesRequested(): List<ILineDataSet> = with(mSismicParameterInteractor){
        getLimitStateLines(mReportManager.report.reportState)
    }

    override fun onLimitStatesRequested(): List<ILineDataSet> = with(mSismicParameterInteractor){
        getSpectrumLines(mReportManager.report.reportState)
    }

    //Updates the state for all fragments
    override fun onParametersConfirmed(report: Report) {
        mReportManager?.updateReportState(report)
        updateStateForFragments(false)
    }

    //creates a new fragment state foreach active fragment, so everyone is updated
    //the fragment not created will have the right arguments on creation
    private fun updateStateForFragments(callback: Boolean) {

        stepperLayout.adapter.getCustomAdapter().updateStateForFragments()

        //Needs update because is the activity who changed some data
        if (callback) supportFragmentManager.fragments
                        .filterIsInstance<BaseReportFragment>()
                        .forEach { it.reloadFragmentFromCallback(mReportManager!!.report) }
    }

    //If I pass an Uri, data will be null
    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        mUserActionInteractor.onActionResponse(requestCode, resultCode, data)
        updateStateForFragments(false)
    }

    override fun onConnectionFailed(p0: ConnectionResult) {
        toast(R.string.google_api_error)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == android.R.id.home) {
            mUserActionInteractor.onActionRequested(UserActionType.BackRequest)
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onParametersSaveRequest() {
        mUserActionInteractor.saveReport()
        finish()
    }

    fun onNewReportConfirmed(createFromNew: ReportManager): ReportManager? {
        initializeFromManager(createFromNew)
        return createFromNew
    }

    private fun initializeFromManager(reportManager: ReportManager) {
        //To handle user action, it uses other interactor to pilot the ui changes to the domain
        mReportManager = reportManager
        mUserActionInteractor = UserActionInteractor(reportManager, this)
        mSismicParameterInteractor = SismicActionParametersInteractor(reportManager, this)
        stepperLayout.adapter = ReportFragmentsAdapter(supportFragmentManager, this, reportManager)
        fabtoolbar_fab.setOnClickListener { fabtoolbar.show() }
        pic.setOnClickListener { mUserActionInteractor.onActionRequested(UserActionType.PicRequest) }
        video.setOnClickListener { mUserActionInteractor.onActionRequested(UserActionType.VideoRequest) }
        audio.setOnClickListener { mUserActionInteractor.onActionRequested(UserActionType.AudioRequest) }
        draw.setOnClickListener { mUserActionInteractor.onActionRequested(UserActionType.SketchRequest) }
        note.setOnClickListener { mUserActionInteractor.onActionRequested(UserActionType.NoteRequest) }

        mGoogleApiClient = GoogleApiClient.Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .enableAutoManage(this, this)
                .build()
    }
}


