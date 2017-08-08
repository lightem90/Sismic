package com.polito.sismic.Interactors

import com.polito.sismic.Domain.IDatabase
import com.polito.sismic.Domain.ReportDTO
import com.polito.sismic.Domain.ReportItemListDTO

/**
 * Created by Matteo on 28/07/2017.
 */
class DatabaseInteractor(val db : IDatabase){

    fun getExampleDTOs(): List<ReportItemListDTO> {
        return db.getExampleDTOs()
    }
}