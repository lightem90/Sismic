package com.polito.sismic.Domain

import java.util.*

/**
 * Created by Matteo on 28/07/2017.
 */
class DatabaseStub : IDatabase {

    override fun getExampleDTOs(): List<ReportDTO> {
        return listOf(
                ReportDTO("bla", "desc", 10, 85),
                ReportDTO("bla1", "desc2", 300, 95),
                ReportDTO("bla2", "desc3", 50, 5),
                ReportDTO("bla3", "desc4", 12, 20),
                ReportDTO("bla4", "desc5", 320, 45)
        )
    }
}

//TODO
interface IDatabase {
    fun getExampleDTOs(): List<ReportDTO>
}
