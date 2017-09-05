package com.polito.sismic.Presenters.Adapters

import android.content.Context
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.polito.sismic.Domain.NeighboursNodeData
import com.polito.sismic.Domain.PeriodData
import com.polito.sismic.Extensions.inflate
import com.polito.sismic.R
import kotlinx.android.synthetic.main.close_points_layout.view.*
import kotlinx.android.synthetic.main.period_data_layout.view.*

class NodeListAdapter(val mContext : Context, val mNodeData : List<NeighboursNodeData>)
    : RecyclerView.Adapter<NodeListAdapter.ViewHolder>()
{

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NodeListAdapter.ViewHolder? {
        val v = parent.inflate(R.layout.close_points_layout)
        return NodeListAdapter.ViewHolder(v, mContext)
    }

    override fun onBindViewHolder(holder: NodeListAdapter.ViewHolder, position: Int) {
        if (position == 0) holder.bindHeader()
        else holder.bindNodeData(mNodeData[position-1])
    }

    override fun getItemCount(): Int {
        return mNodeData.size+1
    }

    class ViewHolder(itemView: View, val mContext: Context) : RecyclerView.ViewHolder(itemView){

        fun bindNodeData(neighboursNodeData: NeighboursNodeData) = with(neighboursNodeData){
            itemView.node_id.text = id
            itemView.node_longitude.text = longitude.toString()
            itemView.node_latitude.text = latitude.toString()
            itemView.node_distance.text = String.format(mContext.resources.getText(R.string.km_format).toString(), distance)
        }

        fun bindHeader()
        {
            itemView.node_id.text = mContext.resources.getText(R.string.nodelist_id_header)
            itemView.node_longitude.text = mContext.resources.getText(R.string.nodelist_longitude_header)
            itemView.node_latitude.text = mContext.resources.getText(R.string.nodelist_latitude_header)
            itemView.node_distance.text = mContext.resources.getText(R.string.nodelist_distance_header)
            itemView.setBackgroundColor(ContextCompat.getColor(mContext, R.color.light_grey))
        }
    }
}

class PeriodListAdapter(val mContext : Context, val mPeriodDataList : List<PeriodData>)
    : RecyclerView.Adapter<PeriodListAdapter.ViewHolder>()
{

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PeriodListAdapter.ViewHolder? {
        val v = parent.inflate(R.layout.period_data_layout)
        return PeriodListAdapter.ViewHolder(v, mContext)
    }

    override fun onBindViewHolder(holder: PeriodListAdapter.ViewHolder, position: Int) {
        if (position == 0) holder.bindHeader()
        else holder.bindPeriodData(mPeriodDataList[position-1])
    }

    override fun getItemCount(): Int {
        return mPeriodDataList.size+1
    }

    class ViewHolder(itemView: View, val mContext: Context) : RecyclerView.ViewHolder(itemView){

        fun bindPeriodData(periodData: PeriodData) = with(periodData){
            itemView.year.text = periodData.years.toString()
            itemView.ag.text = periodData.ag.toString()
            itemView.f0.text = periodData.f0.toString()
            itemView.tcstar.text = periodData.tcstar.toString()
        }

        fun bindHeader()
        {
            itemView.year.text =             mContext.resources.getText(R.string.year_header)
            itemView.ag.text =      mContext.resources.getText(R.string.ag_header)
            itemView.f0.text =       mContext.resources.getText(R.string.f0_header)
            itemView.tcstar.text =       mContext.resources.getText(R.string.tcstar_header)
            itemView.setBackgroundColor(ContextCompat.getColor(mContext, R.color.light_grey))
        }

    }

}