package com.polito.sismic.Presenters.Adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.polito.sismic.Domain.CloseNodeData
import com.polito.sismic.Extensions.inflate
import com.polito.sismic.R
import kotlinx.android.synthetic.main.close_points_layout.view.*

class NodeListAdapter(val mContext : Context, val mNodeData : List<CloseNodeData>) : RecyclerView.Adapter<NodeListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NodeListAdapter.ViewHolder? {
        val v = parent.inflate(R.layout.close_points_layout)
        return NodeListAdapter.ViewHolder(v)
    }

    override fun onBindViewHolder(holder: NodeListAdapter.ViewHolder, position: Int) {
        holder.bindNodeData(mNodeData[position])
    }

    override fun getItemCount(): Int {
        return mNodeData.size
    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        fun bindNodeData(closeNodeData: CloseNodeData) = with(closeNodeData){
            itemView.node_id.text = id
            itemView.node_longitude.text = longitude.toString()
            itemView.node_latitude.text = latitude.toString()
            itemView.node_distance.text = "%.2".format(distance)
        }

    }

}