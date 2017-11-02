package com.polito.sismic.Presenters.ReportActivity

import android.content.Intent
import android.content.pm.ActivityInfo
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import android.view.View
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.places.Places
import com.polito.sismic.Domain.*
import com.polito.sismic.Extensions.getCustomAdapter
import com.polito.sismic.Extensions.getReport
import com.polito.sismic.Extensions.putReport
import com.polito.sismic.Extensions.toast
import com.polito.sismic.Interactors.*
import com.polito.sismic.Interactors.Helpers.PermissionsHelper
import com.polito.sismic.Interactors.Helpers.UserActionType
import com.polito.sismic.Presenters.Adapters.ReportFragmentsAdapter
import com.polito.sismic.Presenters.ReportActivity.Fragments.*
import com.polito.sismic.R
import kotlinx.android.synthetic.main.activity_report.*


//Activity is in charge of handling all data through interactors, then it signals fragment to update their copy of domain value and its grafic
class ReportActivity : AppCompatActivity(),
        BaseReportFragment.ParametersManager,
        BaseReportFragment.PdfWriterManager,
        InfoLocReportFragment.CurrentLocationProvided,
        CatastoReportFragment.NodeCaluclationRequest,
        DatiSismoGeneticiReportFragment.DefaultReturnTimeRequest,
        ParametriSismiciReportFragment.LimitStateRequest,
        SpettriDiProgettoReportFragment.SpectrumReturnTimeRequest,
        PilastriReportFragment.PillarDomainGraphRequest,
        GoogleApiClient.OnConnectionFailedListener {

    private val mPermissionHelper: PermissionsHelper = PermissionsHelper()
    private lateinit var mGoogleApiClient: GoogleApiClient
    private lateinit var mUserActionInteractor: UserActionInteractor
    private lateinit var mSismicParameterInteractor: SismicActionInteractor
    private lateinit var mSismicBuildingInteractor: SismicBuildingInteractor

    //Creating row for report in db if user is logged in
    private val mReportManager: ReportManager by lazy {
        ReportProvider(this).getOrCreateReportManager(checkLogin(), intent)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_report)
        initializeFromManager(mReportManager)

        if (resources.getBoolean(R.bool.portrait_only)) requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
    }

    private fun checkLogin(): String {
        val userID = intent.getStringExtra("email")
        if (userID == null || userID.isEmpty()) {
            toast(R.string.no_login)
            finish()
        }
        return userID
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
    override fun onClosedNodesCalculationRequested() {
        mSismicParameterInteractor.calculateReturnPeriodsParameters()
        updateStateFromCallback()
    }

    //the next 3 methods act on the current fragment, they dont need to call "reload"
    override fun onDefaultReturnTimesRequested(): List<SpectrumDTO> = with(mSismicParameterInteractor) {
        getDefaultSpectrumLines(mReportManager.report.reportState)
    }

    //data is not aligned until confirmation, so i have to pass it
    override fun onSpectrumReturnTimeRequest(data: ProjectSpectrumState): List<SpectrumDTO> = with(mSismicParameterInteractor) {
        getSpectrumLines(mReportManager.report.reportState, data)
    }

    //data is not aligned until confirmation, so i have to pass it
    override fun onLimitStatesRequested(data: SismicParametersState): List<SpectrumDTO> = with(mSismicParameterInteractor) {
        getLimitStateLines(mReportManager.report.reportState, data)
    }

    override fun onPillarDomainGraphRequest(pillarState: PillarState): PillarDomain = with(mSismicBuildingInteractor) {
        return getPillarDomainForGraph(pillarState)
    }

    //Updates the state for all fragments
    override fun onParametersConfirmed(report: Report, needReload: Boolean) {
        mReportManager.updateReportState(report)
        updateStateForFragments(needReload)
    }

    //creates a new fragment state foreach active fragment, so everyone is updated
    //the fragment not created will have the right arguments on creation
    private fun updateStateForFragments(callback: Boolean) {

        stepperLayout.adapter.getCustomAdapter().updateStateForFragments()

        //Needs update because is the activity who changed some data
        if (callback) supportFragmentManager.fragments
                .filterIsInstance<BaseReportFragment>()
                .forEach { it.reloadFragmentFromCallback(mReportManager.report) }
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

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        mPermissionHelper.handlePermissionResult(requestCode, grantResults)
    }

    override fun onBackPressed() {
        mUserActionInteractor.onActionRequested(UserActionType.BackRequest)
    }

    override fun onSavePageRequest(fragmentView: View?, fragName: String) {
        mReportManager.addPdfPageFromView(fragmentView, fragName)
    }

    private fun initializeFromManager(reportManager: ReportManager) {
        //To handle user action, it uses other interactor to pilot the ui changes to the domain

        mUserActionInteractor = UserActionInteractor(reportManager, this, mPermissionHelper)
        mSismicParameterInteractor = SismicActionInteractor(reportManager, this)
        mSismicBuildingInteractor = SismicBuildingInteractor(this)
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

    //Handles configuration changes
    override fun onSaveInstanceState(outState: Bundle?) {
        outState?.putReport(mReportManager.report)
        super.onSaveInstanceState(outState)
    }

    //Reload the fragments??
    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        super.onRestoreInstanceState(savedInstanceState)
        savedInstanceState?.getReport()?.let {
            mReportManager.report = it
        }
    }
    override fun onParametersSaveRequest() {

        var pdfFileName : String? = null
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            pdfFileName = mReportManager.printPdf()
        }

        mUserActionInteractor.saveReport(pdfFileName)
        finish()
    }
}


