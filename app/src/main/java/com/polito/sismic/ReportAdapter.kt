package com.polito.sismic

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.history_item.view.*
import java.util.*

/**
 * Created by Matteo on 28/07/2017.
 */
class ReportAdapter(val items: List<ReportDTO>, val itemClick: (ReportDTO) -> Unit) :
        RecyclerView.Adapter<ReportAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.history_item, parent, false), itemClick)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindReport(items[position])
    }

    override fun getItemCount(): Int {
        return items.size;
    }

    class ViewHolder(itemView: View, val itemClick: (ReportDTO) -> Unit) : RecyclerView.ViewHolder(itemView)
    {
        fun bindReport(report: ReportDTO) {
            with(report) {
                itemView.history_item_title.text = report.title
                itemView.history_item_description.text = report.description
                itemView.history_item_date.text = report.date.toString()
                itemView.history_item_value.text = report.value.toString()
                //TODO
                itemView.btn_edit_item_history.setOnClickListener(View.OnClickListener {
                    fun onClick(v:View){
                    }})
                itemView.btn_delete_item_history.setOnClickListener(View.OnClickListener {
                    fun onClick(v:View){
                    }})
                itemView.btn_print_item_history.setOnClickListener(View.OnClickListener {
                    fun onClick(v:View){
                    }})
                itemView.setOnClickListener { itemClick(this) }
            }
        }
    }
}

data class ReportDTO(val title: String, val description: String, val date : Date, val value : Int)
