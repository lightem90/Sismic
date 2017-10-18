package com.polito.sismic.Presenters.Adapters

import android.content.Context
import android.graphics.Color
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.polito.sismic.Domain.ReportItemHistory
import com.polito.sismic.Extensions.inflate
import com.polito.sismic.Extensions.toFormattedString
import com.polito.sismic.Interactors.DangerStateProvider
import com.polito.sismic.Interactors.HistoryItemInteractor
import com.polito.sismic.R
import kotlinx.android.synthetic.main.history_item.view.*

/**
 * Created by Matteo on 28/07/2017.
 */

//Manager of report list
class ReportAdapter(val mContext: Context,
                    val mHistoryInteractor: HistoryItemInteractor,
                    val longClick: (ReportItemHistory) -> Boolean) :
        RecyclerView.Adapter<ReportAdapter.ViewHolder>() {

    private val items = mHistoryInteractor.mReportHistoryItems
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder? {
        val v = parent.inflate(R.layout.history_item)
        return ViewHolder(v, mHistoryInteractor, longClick, mContext)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindReport(items[position])
    }

    override fun getItemCount(): Int {
        return items.size
    }

    class ViewHolder(itemView: View,
                     private val mHistoryInteractor : HistoryItemInteractor,
                     private val longClick: (ReportItemHistory) -> Boolean,
                     val mContext: Context) : RecyclerView.ViewHolder(itemView)
    {
        fun bindReport(reportDetails: ReportItemHistory) {
            with(reportDetails)
            {
                itemView.isDuplicateParentStateEnabled = true
                itemView.history_item_title.text = reportDetails.title
                itemView.history_item_description.text = reportDetails.date.toFormattedString()
                itemView.history_item_size.text = String.format(mContext.getString(R.string.size_string), reportDetails.size)

                val dangerState = DangerStateProvider.getDangerStateByValue(reportDetails.value)
                itemView.danger_layout.SetDangerState(dangerState)
                itemView.history_item_value.text = reportDetails.value.toString()
                itemView.history_item_value.setTextColor(ContextCompat.getColor(mContext, dangerState.color))

                itemView.setOnLongClickListener { longClick(this) }
                itemView.btn_delete_item_history.setOnClickListener {
                    mHistoryInteractor.deleteItemById(reportDetails.id)
                }
                itemView.btn_upload_item_history.setOnClickListener { mHistoryInteractor.uploadItem(reportDetails.id, reportDetails.userIdentifier) }
            }
        }

    }
}
