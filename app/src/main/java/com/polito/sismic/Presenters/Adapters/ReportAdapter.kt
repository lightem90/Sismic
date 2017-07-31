package com.polito.sismic.Presenters.Adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.polito.sismic.Domain.DangerStateProvider
import com.polito.sismic.Domain.ReportDTO
import com.polito.sismic.Presenters.DangerState
import com.polito.sismic.R
import kotlinx.android.synthetic.main.history_item.view.*
import java.text.SimpleDateFormat

/**
 * Created by Matteo on 28/07/2017.
 */
class ReportAdapter(val items: List<ReportDTO>, val listener: (ReportDTO) -> Unit) :
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
        fun bindReport(report: ReportDTO, listener: (ReportDTO) -> Unit) = with(report) {

            itemView.isDuplicateParentStateEnabled = true
            itemView.history_item_title.text = report.title
            itemView.history_item_description.text = report.description
            itemView.history_item_date.text = SimpleDateFormat("MM/dd/yyyy").format(report.date).toString()
            var dangerState = DangerStateProvider.getDangerStateByValue(report.value)

            itemView.danger_layout.SetDangerState(dangerState)
            itemView.history_item_value.text = report.value.toString()
            //TODO: colore anche al value

            //TODO: Bottoni e click
            setOnClickListener { listener(report) }

        }

        fun setOnClickListener(listener: (view: View) -> Unit){}
    }
}
