package com.polito.sismic.Interactors

import com.polito.sismic.Domain.ReportItemHistory

/**
 * Created by it0003971 on 22/09/2017.
 */
class HistoryItemInteractor (val mDatabaseInteractor: DatabaseInteractor) {

    val mReportHistoryItems = mDatabaseInteractor.getDetailsForHistory()

    fun reloadList()
    {
        mReportHistoryItems.clear()
        mReportHistoryItems.addAll(mDatabaseInteractor.getDetailsForHistory())
    }

    fun deleteItemById(id: Int) {}
    fun printItem(id: Int) {}
    fun uploadItem(id: Int) {}


}