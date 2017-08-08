package com.polito.sismic.Domain

import java.util.*

/**
 * Created by Matteo on 28/07/2017.
 */
class DatabaseStub : IDatabase {
    override fun getRecordNumber(): Int {
        return 6
    }

    override fun getExampleDTOs(): List<ReportItemListDTO> {
        return listOf(
                ReportItemListDTO("bla", "desc", 10, 85),
                ReportItemListDTO("bla1", "desc2", 300, 95),
                ReportItemListDTO("bla2", "desc3", 50, 5),
                ReportItemListDTO("bla3", "desc4", 12, 20),
                ReportItemListDTO("bla4", "desc5", 320, 45)
        )
    }
}

//TODO
interface IDatabase {
    fun getExampleDTOs(): List<ReportItemListDTO>
    fun  getRecordNumber(): Int
}
