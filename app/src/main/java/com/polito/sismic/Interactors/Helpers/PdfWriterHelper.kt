package com.polito.sismic.Interactors.Helpers

import com.polito.sismic.Domain.Report

/**
 * Created by it0003971 on 22/09/2017.
 */
class PdfWriterHelper {

    fun write(report : Report) : PdfWriterHelperResult
    {
        return PdfWriterHelperResult()
    }
}


class PdfWriterHelperResult
{
    val success : Boolean = false
}