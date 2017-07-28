package com.polito.sismic.Interactors

import com.polito.sismic.Domain.DatabaseStub
import com.polito.sismic.Domain.IDatabase
import com.polito.sismic.Domain.ReportDTO

/**
 * Created by Matteo on 28/07/2017.
 */
class DatabaseInteractor(val db : IDatabase) : (ReportDTO) -> Unit {

    //TODO click sull'oggetto e altro
    override fun invoke(p1: ReportDTO) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    fun getExampleDTOs(): List<ReportDTO> {
        return db.getExampleDTOs()
    }
}