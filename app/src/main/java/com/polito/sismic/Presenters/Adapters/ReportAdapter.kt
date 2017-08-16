package com.polito.sismic.Presenters.Adapters

import android.graphics.Color
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.polito.sismic.Domain.Report
import com.polito.sismic.Interactors.Helpers.DangerStateProvider
import com.polito.sismic.R
import kotlinx.android.synthetic.main.history_item.view.*

/**
 * Created by Matteo on 28/07/2017.
 */

//Manager of report list
class ReportAdapter(val items: List<Report>, val listener: (Report) -> Unit) :
        RecyclerView.Adapter<ReportAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder? {
        val v = parent.inflate(R.layout.history_item)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindReport(items[position], listener)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun ViewGroup.inflate(layoutRes: Int): View {
        return LayoutInflater.from(context).inflate(layoutRes, this, false)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    {
        fun bindReport(report: Report, listener: (Report) -> Unit) = with(report) {

            itemView.isDuplicateParentStateEnabled = true
            itemView.history_item_title.text = report.title
            itemView.history_item_description.text = report.description
            itemView.history_item_size.text = report.size.toString() + " MB"
            var dangerState = DangerStateProvider.getDangerStateByValue(report.value)

            itemView.danger_layout.SetDangerState(dangerState)
            itemView.history_item_value.text = report.value.toString()
            setTextColorByDanger(report.value, itemView.history_item_value)

            //TODO: Bottoni e click (edit sul click della view)
            //setOnClickListener { listener(mReportManager) }

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
