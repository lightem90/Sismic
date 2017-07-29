package com.polito.sismic.Presenters.Adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.polito.sismic.Domain.ReportDTO
import com.polito.sismic.R
import kotlinx.android.synthetic.main.history_item.view.*

/**
 * Created by Matteo on 28/07/2017.
 */
class ReportAdapter(var items: List<ReportDTO>, val listener: (ReportDTO) -> Unit) :
        RecyclerView.Adapter<ReportAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder? {
        val v = parent.inflate(R.layout.history_item)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindReport(items[position], listener)
    }

    override fun getItemCount(): Int {
        return items.size;
    }

    fun ViewGroup.inflate(layoutRes: Int): View {
        return LayoutInflater.from(context).inflate(layoutRes, this, false)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    {
        fun bindReport(report: ReportDTO, listener: (ReportDTO) -> Unit) = with(report) {

            itemView.history_item_title.text = report.title
            itemView.history_item_description.text = report.description
            itemView.history_item_date.text = report.date.toString()
            itemView.history_item_value.text = report.value.toString()
            //TODO: Bottoni e click
            setOnClickListener { listener(report) }

        }

        fun setOnClickListener(listener: (view: View) -> Unit){}
    }
}
