package com.polito.sismic.Interactors

import android.app.Activity
import android.content.Intent
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
class UserActionInteractor(val reportManager : ReportManager, val mCaller : Activity) {

    val USER_ACTION_PIC         = 40
    val USER_ACTION_VIDEO       = 41
    val USER_ACTION_AUDIO       = 42
    val USER_ACTION_SKETCH      = 43
    val USER_ACTION_NOTE        = 44


    fun onActionRequested(requestType : UserActionType)
    {
        when (requestType)
        {
            UserActionType.PicRequest ->        startPictureIntent(mCaller)
            UserActionType.VideoRequest ->      startVideoIntent(mCaller)
            UserActionType.AudioRequest ->      startAudioIntent(mCaller)
            UserActionType.SketchRequest->      startSketchIntent(mCaller)
            UserActionType.NoteRequest->        startNoteIntent(mCaller)
            UserActionType.BackRequest->        goBack(mCaller)
        }
    }

    private fun goBack(caller: Activity) {


        AlertDialog.Builder(caller)
                .setTitle(R.string.confirm_report_back)
                .setMessage(R.string.confirm_report_back_message)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.yes, { _, _ ->
                    reportManager.deleteReport()
                    caller.finish()})
                .setNegativeButton(android.R.string.no, null)
                .show()

    }

    private fun startNoteIntent(caller: Activity) {
        var intent = Intent(caller, NoteActivity::class.java)
        caller.startActivityForResult(intent, USER_ACTION_NOTE)
    }

    private fun startSketchIntent(caller: Activity) {

        var drawBitmap : Intent = Intent(caller, SketchActivity::class.java)
        if (drawBitmap.resolveActivity(caller.packageManager) != null)
        {
            var bitmapUri = reportManager.getUriForMedia(MediaType.Picture)
            drawBitmap.putExtra(MediaStore.EXTRA_OUTPUT, bitmapUri)
            caller.startActivityForResult(drawBitmap, USER_ACTION_SKETCH)
        }
    }

    private fun startAudioIntent(caller: Activity) {

        val audioRecordIntent = Intent(MediaStore.Audio.Media.RECORD_SOUND_ACTION)
        if (audioRecordIntent.resolveActivity(caller.packageManager) != null) {

            var audioUri = reportManager.getUriForMedia(MediaType.Audio)
            audioRecordIntent.putExtra(MediaStore.EXTRA_OUTPUT, audioUri)
            caller.startActivityForResult(audioRecordIntent, USER_ACTION_AUDIO)
        }
    }

    private fun startVideoIntent(caller: Activity) {
        val takeVideoIntent = Intent(MediaStore.ACTION_VIDEO_CAPTURE)
        if (takeVideoIntent.resolveActivity(caller.packageManager) != null) {

            var videoUri = reportManager.getUriForMedia(MediaType.Video)
            takeVideoIntent.putExtra(MediaStore.EXTRA_OUTPUT, videoUri)
            caller.startActivityForResult(takeVideoIntent, USER_ACTION_VIDEO)
        }
    }

    private fun startPictureIntent(caller: Activity) {

        var takePictureIntent : Intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(caller.packageManager) != null)
        {
            var photoUri = reportManager.getUriForMedia(MediaType.Picture)
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
            caller.startActivityForResult(takePictureIntent, USER_ACTION_PIC)
        }
    }

    //Confirms or deletes last added media
    fun onActionResponse(requestCode: Int, resultCode: Int, data: Intent?) {

        when(requestCode)
        {
            USER_ACTION_PIC,
            USER_ACTION_VIDEO,
            USER_ACTION_SKETCH -> noOtherActionsRequired(resultCode)

            USER_ACTION_AUDIO -> fixUri(data)
            USER_ACTION_NOTE  -> addNote(data)
        }

    }

    private fun addNote(data: Intent?) {

        var plainTextNote = data?.getStringExtra("note")
        if (plainTextNote == null)
        {
            mCaller.toast(R.string.note_error_empty)
            return
        }
        reportManager.addNote(plainTextNote)
    }

    private fun fixUri(data: Intent?) {

        if (data == null ||  data.data == null)
        {
            mCaller.toast(R.string.error_saving_file)
            return
        }
        reportManager.fixUriForAudio(data.data)
    }

    fun noOtherActionsRequired(resultCode: Int) {
        if (resultCode == Activity.RESULT_OK)
        {
            mCaller.toast(R.string.correctly_saving_file)
            reportManager.confirmLastMedia()
        }
        else
        {
            mCaller.toast(R.string.error_saving_file)
            reportManager.deleteLastMedia()
        }
    }
}