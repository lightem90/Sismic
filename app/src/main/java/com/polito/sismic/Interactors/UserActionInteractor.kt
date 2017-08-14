package com.polito.sismic.Interactors

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import android.support.v7.app.AlertDialog
import com.polito.sismic.Domain.ReportManager
import com.polito.sismic.Extensions.toast
import com.polito.sismic.Interactors.Helpers.UserActionType
import com.polito.sismic.Presenters.ReportActivity.NoteActivity
import com.polito.sismic.Presenters.ReportActivity.SketchActivity
import com.polito.sismic.R

/**
 * Created by Matteo on 08/08/2017.
 */
class UserActionInteractor(val mReportManager: ReportManager,
                           val mCaller : Activity,
                           val reportMediaInteractor: ReportMediaInteractor = ReportMediaInteractor(mReportManager, mCaller)) {

    val USER_ACTION_PIC         = 40
    val USER_ACTION_VIDEO       = 41
    val USER_ACTION_AUDIO       = 42
    val USER_ACTION_SKETCH      = 43
    val USER_ACTION_NOTE        = 44


    fun onActionRequested(requestType : UserActionType) = with(mCaller)
    {
        when (requestType)
        {
            UserActionType.PicRequest ->        startPictureIntent(this)
            UserActionType.VideoRequest ->      startVideoIntent(this)
            UserActionType.AudioRequest ->      startAudioIntent(this)
            UserActionType.SketchRequest->      startSketchIntent(this)
            UserActionType.NoteRequest->        startNoteIntent(this)
            UserActionType.BackRequest->        goBack(this)
        }
    }

    private fun goBack(caller: Activity) {


        AlertDialog.Builder(caller)
                .setTitle(R.string.confirm_report_back)
                .setMessage(R.string.confirm_report_back_message)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.yes, { _, _ ->
                    mReportManager.deleteReport()
                    caller.finish()})
                .setNegativeButton(android.R.string.no, null)
                .show()

    }

    private fun startNoteIntent(caller: Activity) {
        var intent = Intent(caller, NoteActivity::class.java)
        reportMediaInteractor.createFileForMedia(MediaType.Note)
        caller.startActivityForResult(intent, USER_ACTION_NOTE)
    }

    private fun startSketchIntent(caller: Activity) {

        var drawBitmap : Intent = Intent(caller, SketchActivity::class.java)
        if (drawBitmap.resolveActivity(caller.packageManager) != null)
        {
            var bitmap = reportMediaInteractor.createFileForMedia(MediaType.Picture)
            drawBitmap.putExtra(MediaStore.EXTRA_OUTPUT, Uri.parse(bitmap?.url))
            caller.startActivityForResult(drawBitmap, USER_ACTION_SKETCH)
        }
    }

    private fun startAudioIntent(caller: Activity) {

        val audioRecordIntent = Intent(MediaStore.Audio.Media.RECORD_SOUND_ACTION)
        if (audioRecordIntent.resolveActivity(caller.packageManager) != null) {

            var audio = reportMediaInteractor.createFileForMedia(MediaType.Audio)
            audioRecordIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.parse(audio?.url))
            caller.startActivityForResult(audioRecordIntent, USER_ACTION_AUDIO)
        }
    }

    private fun startVideoIntent(caller: Activity) {
        val takeVideoIntent = Intent(MediaStore.ACTION_VIDEO_CAPTURE)
        if (takeVideoIntent.resolveActivity(caller.packageManager) != null) {

            var video = reportMediaInteractor.createFileForMedia(MediaType.Video)
            takeVideoIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.parse(video?.url))
            caller.startActivityForResult(takeVideoIntent, USER_ACTION_VIDEO)
        }
    }

    private fun startPictureIntent(caller: Activity) {

        var takePictureIntent : Intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(caller.packageManager) != null)
        {
            var photo = reportMediaInteractor.createFileForMedia(MediaType.Picture)
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.parse(photo?.url))
            caller.startActivityForResult(takePictureIntent, USER_ACTION_PIC)
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

        if (data == null ||  data.data == null)
        {
            mCaller.toast(R.string.error_saving_file)
            return
        }

        //The audio could be saved into custom location, in this way I reposition the file in the expected Uri
        reportMediaInteractor.fixUriForAudio(data.data)
    }


    fun noOtherActionsRequired(resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK)
        {
            reportMediaInteractor.finalizeLastMedia(data?.getStringExtra("note"))
            mCaller.toast(R.string.correctly_saving_file)
        }
        else
        {
            reportMediaInteractor.deleteLastMedia()
            mCaller.toast(R.string.error_saving_file)
        }
    }

    fun saveReport() {
        mReportManager.saveReportToDb()
    }
}