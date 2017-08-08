package com.polito.sismic.Domain

import android.content.Context
import com.polito.sismic.Interactors.ParameterInteractor
import java.io.File
import java.net.URI

//TODO: Classe di dominio del reportManager
//Classe che contiente i dati e che viene passato tra i fragment,
//la classe "reportManager" lo wrappa solamente e deve essere creata/ricevuta (nel caso si stia editando)
class ReportManager(val id: Int, mContext: Context, var DTO : ReportDTO ) {

    constructor(mContext: Context, dto: ReportDTO) : this(dto.id, mContext, dto)                                                //Edit
    constructor(mContext: Context, id : Int) : this (id, mContext, ReportDTO(id, HashMap(), HashMap(), HashMap(), HashMap()))   //New

    val reportDir : File = mContext.getDir("REPORT_" + id, Context.MODE_PRIVATE)
    //Componenti: gestore dei media e gestore dei parametri
    private val mMediaInteractor: ReportMediaInteractor = ReportMediaInteractor(reportDir)
    private val mParameterInteractor : ParameterInteractor = ParameterInteractor(DTO)

    fun deleteReport() : Boolean
    {
        return reportDir.deleteRecursively()
    }

    fun getUriForMedia(type : MediaType) : URI?
    {
        return mMediaInteractor.getFileNameForMedia(type)
    }

    fun getMediaFolderSize() : Int
    {
        return mMediaInteractor.getMediaSizeMb()
    }

    fun <T> setValue(paramName : String, value : T) {
        mParameterInteractor.setValue(paramName, value)
    }
}

