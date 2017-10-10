package com.polito.sismic.Interactors

import android.content.Context
import com.polito.sismic.Extensions.toast
import com.polito.sismic.Interactors.Helpers.UploadHelper
import com.polito.sismic.R

/**
 * Created by it0003971 on 22/09/2017.
 */

enum class ReorderType
{
    az,
    za,
    asc,
    desc
}


class HistoryItemInteractor (val mContext: Context,
                             val mDatabaseInteractor: DatabaseInteractor,
                             val reloadCallback: () -> Unit) {

    val mReportHistoryItems = mDatabaseInteractor.getDetailsForHistory()
    val mUploadHelper = UploadHelper()

    fun reloadList()
    {
        mReportHistoryItems.clear()
        mReportHistoryItems.addAll(mDatabaseInteractor.getDetailsForHistory())
        reloadCallback.invoke()
    }

    fun reorder(type : ReorderType)
    {
        mReportHistoryItems.clear()
        val listToSort = mDatabaseInteractor.getDetailsForHistory()
        when(type)
        {
            ReorderType.az -> listToSort.sortBy { reportItemHistory ->  reportItemHistory.title}
            ReorderType.za -> listToSort.sortByDescending { reportItemHistory ->  reportItemHistory.title}
            ReorderType.asc -> listToSort.sortBy { reportItemHistory ->  reportItemHistory.date}
            ReorderType.desc -> listToSort.sortByDescending { reportItemHistory ->  reportItemHistory.date}
        }
        mReportHistoryItems.addAll(listToSort)
    }

    fun deleteItemById(id: Int) = with(mDatabaseInteractor){
        delete(id)
        reloadList()
        mContext.toast(String.format(mContext.getString(R.string.report_delete_succeded), id))
    }

    fun uploadItem(id: Int, userName: String) = with(mDatabaseInteractor){
        val report = getReportForId(id.toString(), userName)
        report?.let {
            if (mUploadHelper.upload(it).success)
            {

            }
        }
    }


}