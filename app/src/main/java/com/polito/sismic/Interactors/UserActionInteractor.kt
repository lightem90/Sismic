package com.polito.sismic.Interactors

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import android.support.v7.app.AlertDialog
import com.polito.sismic.Extensions.toast
import com.polito.sismic.Interactors.Helpers.MediaFile
import com.polito.sismic.Interactors.Helpers.MediaType
import com.polito.sismic.Interactors.Helpers.PermissionsHelper
import com.polito.sismic.Interactors.Helpers.UserActionType
import com.polito.sismic.Presenters.ReportActivity.NoteActivity
import com.polito.sismic.Presenters.ReportActivity.SketchActivity
import com.polito.sismic.R
import kotlinx.android.synthetic.main.filename_dialog.view.*

/**
 * Created by Matteo on 08/08/2017.
 */
class UserActionInteractor(private val mReportManager: ReportManager,
                           private val mCaller : Activity,
                           private val mPermissionHelper : PermissionsHelper,
                           private val reportMediaInteractor: ReportMediaInteractor = ReportMediaInteractor(mReportManager, mCaller)) {

    private val USER_ACTION_PIC         = 40
    private val USER_ACTION_VIDEO       = 41
    private val USER_ACTION_AUDIO       = 42
    private val USER_ACTION_SKETCH      = 43
    private val USER_ACTION_NOTE        = 44


    fun onActionRequested(requestType : UserActionType) = with(mCaller)
    {
        when (requestType)
        {
            UserActionType.PicRequest ->        askForFileAndLaunchIntent(MediaType.Picture)
            UserActionType.VideoRequest ->      askForFileAndLaunchIntent(MediaType.Video)
            UserActionType.AudioRequest ->      askForFileAndLaunchIntent(MediaType.Audio)
            UserActionType.SketchRequest->      askForFileAndLaunchIntent(MediaType.Sketch)
            UserActionType.NoteRequest->        startNoteIntent(this)
            UserActionType.BackRequest->        goBack(this)
        }
    }

    private fun askForFileAndLaunchIntent(type : MediaType) = with(mCaller)
    {
        with(layoutInflater.inflate(R.layout.filename_dialog, null))
        {
            android.support.v7.app.AlertDialog.Builder(mCaller)
                    .setTitle(com.polito.sismic.R.string.report_filename_dialog)
                    .setView(this)
                    .setPositiveButton(com.polito.sismic.R.string.confirm_filename,
                            {_, _ ->
                                if (filename.getParameterValue().length < 3)
                                    toast(R.string.error_media_short)
                                else
                                {
                                    startIntent(reportMediaInteractor.createFileForMedia(type, this.filename.getParameterValue()), type)
                                }
                            })
                    .setNegativeButton(com.polito.sismic.R.string.discard_filename, { _, _ -> })
                    .show()
        }
    }

    private fun startIntent(mediaFile: MediaFile, type: MediaType)
    {
        when (type)
        {
            MediaType.Picture -> startPictureIntent(mediaFile)
            MediaType.Video -> startVideoIntent(mediaFile)
            MediaType.Audio -> startAudioIntent(mediaFile)
            MediaType.Sketch -> startSketchIntent(mediaFile)
            else -> { }
        }
    }

    private fun goBack(caller: Activity) {

        AlertDialog.Builder(caller)
                .setTitle(R.string.confirm_report_back)
                .setMessage(R.string.confirm_report_back_message)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.yes, { _, _ ->
                    mReportManager.deleteTmpReport()
                    caller.finish()})
                .setNegativeButton(android.R.string.no, null)
                .show()
    }

    private fun startNoteIntent(caller: Activity) {
        val intent = Intent(caller, NoteActivity::class.java)
        intent.putExtra("username", mReportManager.getUserName())
        reportMediaInteractor.createFileForMedia(MediaType.Note, "")
        caller.startActivityForResult(intent, USER_ACTION_NOTE)
    }

    private fun startSketchIntent(file: MediaFile) = with(mCaller){

        val drawBitmap = Intent(this, SketchActivity::class.java)
        if (drawBitmap.resolveActivity(packageManager) != null)
        {
            drawBitmap.putExtra(MediaStore.EXTRA_OUTPUT, file.uri)
            drawBitmap.putExtra("username", mReportManager.getUserName())
            startActivityForResult(drawBitmap, USER_ACTION_SKETCH)
        }
    }

    private fun startAudioIntent(file: MediaFile) = with(mCaller){

        if (!mPermissionHelper.PERMISSION_AUDIO_GRANTED)
            mPermissionHelper.checkAndAskAudioPermissions(this)

        if (mPermissionHelper.PERMISSION_AUDIO_GRANTED) {
            val audioRecordIntent = Intent(MediaStore.Audio.Media.RECORD_SOUND_ACTION)
            if (audioRecordIntent.resolveActivity(packageManager) != null) {

                audioRecordIntent.putExtra(MediaStore.EXTRA_OUTPUT, file.uri)
                startActivityForResult(audioRecordIntent, USER_ACTION_AUDIO)
            }
        }
        else
            toast(R.string.permission_audio_denied)
    }

    private fun startVideoIntent(file: MediaFile) = with(mCaller){

        if (!mPermissionHelper.PERMISSION_VIDEO_GRANTED)
        {
            mPermissionHelper.checkAndAskVideoPermissions(this)
        }

        if (mPermissionHelper.PERMISSION_VIDEO_GRANTED){
            val takeVideoIntent = Intent(MediaStore.ACTION_VIDEO_CAPTURE)
            if (takeVideoIntent.resolveActivity(packageManager) != null) {

                takeVideoIntent.putExtra(MediaStore.EXTRA_OUTPUT, file.uri)
                startActivityForResult(takeVideoIntent, USER_ACTION_VIDEO)
            }
        }
        else
            toast(R.string.permission_video_denied)
    }

    private fun startPictureIntent(file: MediaFile) = with(mCaller){

        val takePictureIntent : Intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(packageManager) != null)
        {
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, file.uri)
            startActivityForResult(takePictureIntent, USER_ACTION_PIC)
        }
    }

    //Confirms or deletes last added media
    fun onActionResponse(requestCode: Int, resultCode: Int, data: Intent?) {

        when(requestCode)
        {
            USER_ACTION_PIC,
            USER_ACTION_VIDEO,
            USER_ACTION_NOTE,
            USER_ACTION_SKETCH -> noOtherActionsRequired(resultCode, data)

            USER_ACTION_AUDIO -> fixUri(data)
        }

    }

    private fun fixUri(data: Intent?) {

        data?.let {
            it.data?.let { d ->
                //The audio could be saved into custom location, in this way I reposition the file in the expected Uri
                reportMediaInteractor.fixUriForAudio(d)
            }
        }
    }


    private fun noOtherActionsRequired(resultCode: Int, data: Intent?) = with(mCaller){
        if (resultCode == Activity.RESULT_OK)
        {
            reportMediaInteractor.finalizeLastMedia(data?.getStringExtra("note"))
            toast(R.string.correctly_saving_file)
        }
        else
        {
            reportMediaInteractor.deleteLastMedia()
            toast(R.string.error_saving_file)
        }
    }

    fun saveReport() = with(mReportManager){
        saveReportToDb()
    }
}