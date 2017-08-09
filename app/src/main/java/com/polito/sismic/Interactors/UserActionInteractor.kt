package com.polito.sismic.Interactors

import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.provider.MediaStore
import android.support.v7.app.AlertDialog
import com.polito.sismic.Domain.MediaType
import com.polito.sismic.Domain.ReportManager
import com.polito.sismic.Interactors.Helpers.UserActionType
import com.polito.sismic.R


/**
 * Created by Matteo on 08/08/2017.
 */
class UserActionInteractor(val reportManager : ReportManager) {

    val USER_ACTION_PIC         = 40
    val USER_ACTION_VIDEO       = 41
    val USER_ACTION_AUDIO       = 42
    val USER_ACTION_SKETCH      = 43
    val USER_ACTION_NOTE        = 44


    fun onActionRequested(caller : Activity, requestType : UserActionType)
    {
        when (requestType)
        {
            UserActionType.PicRequest ->        startPictureIntent(caller)
            UserActionType.VideoRequest ->      startVideoIntent(caller)
            UserActionType.AudioRequest ->      startAudioIntent(caller)
            UserActionType.SketchRequest->      startSketchIntent(caller)
            UserActionType.NoteRequest->        startNoteIntent(caller)
            UserActionType.BackRequest->        goBack(caller)
        }
    }

    private fun goBack(caller: Activity) {

        AlertDialog.Builder(caller)
                .setTitle(R.string.confirm_report_back)
                .setMessage(R.string.confirm_report_back_message)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.yes, DialogInterface.OnClickListener { dialog, _ -> reportManager!!.deleteAllReportMedia(); caller.finish()})
                .setNegativeButton(android.R.string.no, null)
                .show()

    }

    private fun startNoteIntent(caller: Activity) {

    }

    private fun startSketchIntent(caller: Activity) {

    }

    private fun startAudioIntent(caller: Activity) {

    }

    private fun startVideoIntent(caller: Activity) {

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
    fun onActionResponse(resultCode: Int) {

        if (resultCode == Activity.RESULT_OK)
            reportManager.confirmLastMedia()
        else
        {
            //TODO signal error to user
            reportManager.deleteLastMedia()
        }

    }
}