package com.polito.sismic.Interactors

import com.polito.sismic.Domain.IDatabase
import com.polito.sismic.Domain.ReportDTO

/**
 * Created by Matteo on 28/07/2017.
 */
class DatabaseInteractor(val db : IDatabase){

    fun getExampleDTOs(): List<ReportDTO> {
        return db.getExampleDTOs()
    }
}