package com.polito.sismic.Presenters.Adapters

import android.content.Context
import android.graphics.Color
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

    val items = mHistoryInteractor.mReportHistoryItems
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
                     val mHistoryInteractor : HistoryItemInteractor,
                     val longClick: (ReportItemHistory) -> Boolean,
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
                setTextColorByDanger(reportDetails.value, itemView.history_item_value)
                itemView.setOnLongClickListener { longClick(this) }
                itemView.btn_delete_item_history.setOnClickListener { mHistoryInteractor.deleteItemById(reportDetails.id) }
                itemView.btn_print_item_history.setOnClickListener { mHistoryInteractor.printItem(reportDetails.id, reportDetails.userIdentifier) }
                itemView.btn_upload_item_history.setOnClickListener { mHistoryInteractor.uploadItem(reportDetails.id, reportDetails.userIdentifier) }
            }
        }

        //Per non mettere il mContext qui dentro, altrimenti avrei messo tutto come statico in DAngerStateProvider
        private fun setTextColorByDanger(danger : Int, textView : TextView)
        {
            when(danger)
            {
                in 0..10 -> return textView.setTextColor(Color.parseColor("#0099FF"))
                in 11..35 -> return textView.setTextColor(Color.parseColor("#33CC00"))
                in 36..50 -> return textView.setTextColor(Color.parseColor("#FF9900"))
                in 51..100 -> return textView.setTextColor(Color.parseColor("#FF0000"))
            }
        }

    }
}
