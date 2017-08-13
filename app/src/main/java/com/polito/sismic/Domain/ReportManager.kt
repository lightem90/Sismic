package com.polito.sismic.Domain

import android.content.Context
import android.net.Uri
import com.polito.sismic.Interactors.MediaType
import com.polito.sismic.Interactors.ParameterInteractor
import com.polito.sismic.Interactors.ReportMediaInteractor
import com.polito.sismic.Presenters.CustomLayout.ParameterReportLayout
import java.io.File
import java.util.*


//TODO: Classe di dominio
//Wrapper of the DTO that contains the managers for parameters and media
class ReportManager(val id: Int, mContext: Context, private val DTO : ReportDTO ) {

    //TODO: i toast da errore vanno controllati prima di arrivare quà
    constructor(mContext: Context, dto: ReportDTO) : this(dto.id, mContext, dto)                                                //Edit
    constructor(mContext: Context, id : Int, userIdentifier : String)
            : this (id, mContext, ReportDTO(id, "", "", userIdentifier, Date(), HashMap(), HashMap(), HashMap(), HashMap(), mutableListOf<String>(), mutableListOf<Uri>(), 0.0, 0))   //New


    private val mReportPrefix : String = "REPORT_" + id + "_"
    //Two components to handle parameters and media
    private val mMediaInteractor: ReportMediaInteractor = ReportMediaInteractor(mContext, mReportPrefix)
    //Need the second underscore, otherwise REPORT_1 equals REPORT_11, I have to search REPORT_+ID+_
    private val mParameterInteractor : ParameterInteractor = ParameterInteractor(DTO, mContext)

    val userID : String = DTO.userIdentifier
    val Date : Date = DTO.reportDate
    val isNew : Boolean = DTO.isNew
    val title : String = DTO.title
    val description : String = DTO.description
    val dangerLevel : Int = DTO.value

    fun deleteAllReportMedia()
    {
        mParameterInteractor.getAllMedia()?.forEach{ x -> File(x.path).delete() }
    }

    fun getUriForMedia(type : MediaType) : Uri?
    {
        return mMediaInteractor.createFileForMedia(type)
    }

    fun getMediaSizeMb() : String
    {
        return "%.2f".format(mParameterInteractor.mMediaSize)
    }

    fun setValue(paramId : Int, value : String) {
        mParameterInteractor.setValue(paramId, value)
    }

    fun confirmLastMedia() {
        //confirms media file and pushes into parameters list to be viewed at the end
        mParameterInteractor.addMediaPath(mMediaInteractor.lastAddedTmpFile)
    }

    fun addNote(noteToAdd: String)
    {
        mParameterInteractor.addNote(noteToAdd)
    }

    fun deleteLastMedia() {
        //delete temp invalid file
        File(mMediaInteractor.lastAddedTmpFile!!.path).delete()
    }

    //The audio could be saved into custom location, in this way I reposition the file in the expected Uri
    fun fixUriForAudio(newUri: Uri) {

        if (mMediaInteractor.lastAddedTmpFile != newUri)
        {
            mParameterInteractor.saveMp3FromSourceUri(newUri.path, mMediaInteractor.lastAddedTmpFile)
        }

        confirmLastMedia()
    }

    fun <T> getValue(valueId : Int) : T
    {
        var toReturn = mParameterInteractor.getValue<T>(valueId)
        if (toReturn == null)
            throw Exception()
        return toReturn
    }

    fun  <T> prepareForEdit(viewToInitialize: ParameterReportLayout?) {
        if (viewToInitialize == null) return
        val newValue = mParameterInteractor.getValue<T>(viewToInitialize.id)
        if (newValue != null) viewToInitialize.setParameterValue(newValue.toString())
    }

    fun getParcelable() : ReportDTO
    {
        return DTO
    }

    //For add to gallery (not needed now)
    //private fun galleryAddPic() {
    //    val mediaScanIntent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
    //    val f = File(mCurrentPhotoPath)
    //    val contentUri = Uri.fromFile(f)
    //    mediaScanIntent.data = contentUri
    //    this.sendBroadcast(mediaScanIntent)
    //}
}

