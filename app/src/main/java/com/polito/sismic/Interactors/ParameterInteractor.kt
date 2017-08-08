package com.polito.sismic.Interactors

import android.net.Uri
import com.polito.sismic.Domain.ReportDTO

class ParameterInteractor(val dto: ReportDTO?) {

    fun <T> setValue(paramName : String, value : T) {
        if (dto == null) return //too soon
        when (value)
        {
            is Boolean -> dto.boolHashMap.put(paramName, value)
            is Double -> dto.doubleHashMap.put(paramName, value)
            is Int -> dto.intHashMap.put(paramName, value)
            is String ->dto.stringHashMap.put(paramName, value)
        }
    }

    fun addMediaPath(path : Uri)
    {
        if (dto == null) return //too soon
        dto.mediaList.add(path)
    }
}