package com.polito.sismic.Domain

import android.content.Context
import android.net.Uri
import com.polito.sismic.Extensions.toast
import com.polito.sismic.Interactors.ParameterInteractor
import com.polito.sismic.R
import java.io.File
import java.util.function.Consumer


//TODO: Classe di dominio del reportManager
//Classe che contiente i dati e che viene passato tra i fragment,
//la classe "reportManager" lo wrappa solamente e deve essere creata/ricevuta (nel caso si stia editando)
class ReportManager(val id: Int, val mContext: Context, var DTO : ReportDTO ) {

    constructor(mContext: Context, dto: ReportDTO) : this(dto.id, mContext, dto)                                                //Edit
    constructor(mContext: Context, id : Int)
            : this (id, mContext, ReportDTO(id, HashMap(), HashMap(), HashMap(), HashMap(), mutableListOf<Uri>()))   //New

    //Componenti: gestore dei media e gestore dei parametri
    private val mMediaInteractor: ReportMediaInteractor
    private val mParameterInteractor : ParameterInteractor

    init {

        mMediaInteractor = ReportMediaInteractor(mContext, "REPORT_" + id)
        mParameterInteractor = ParameterInteractor(DTO, mContext)
    }

    fun deleteAllReportMedia()
    {
        mParameterInteractor.getAllMedia()?.forEach(Consumer { x -> File(x.path).delete() })
    }

    fun getUriForMedia(type : MediaType) : Uri?
    {
        return mMediaInteractor.createFileForMedia(type)
    }

    fun getMediaSize() : Int
    {
        return mParameterInteractor.mMediaSize
    }

    fun <T> setValue(paramName : String, value : T) {
        mParameterInteractor.setValue(paramName, value)
    }

    fun confirmLastMedia() {
        //confirms media file and pushes into parameters list to be viewed at the end
        mContext.toast(R.string.correctly_saving_file)
        mParameterInteractor.addMediaPath(mMediaInteractor.lastAddedTmpFile)
    }

    fun deleteLastMedia() {
        //delete temp invalid file
        mContext.toast(R.string.error_saving_file)
        File(mMediaInteractor.lastAddedTmpFile!!.path).delete()
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

