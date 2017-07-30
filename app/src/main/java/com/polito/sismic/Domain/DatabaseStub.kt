package com.polito.sismic.Domain

import java.util.*

/**
 * Created by Matteo on 28/07/2017.
 */
class DatabaseStub : IDatabase {

    override fun getExampleDTOs(): List<ReportDTO> {
        return listOf(
                ReportDTO("bla", "desc", Date(), 85),
                ReportDTO("bla1", "desc2", Date(), 95)
        )
    }
}

//TODO
interface IDatabase {
    fun getExampleDTOs(): List<ReportDTO>
}
