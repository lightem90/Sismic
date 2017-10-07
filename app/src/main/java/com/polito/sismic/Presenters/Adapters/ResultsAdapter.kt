package com.polito.sismic.Presenters.Adapters

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.polito.sismic.Domain.PillarDomainPoint
import com.polito.sismic.Extensions.inflate
import com.polito.sismic.Interactors.Helpers.StatiLimite
import com.polito.sismic.R

/**
 * Created by Matteo on 07/10/2017.
 */
class ResultsAdapter(val mItems : List<StatiLimite>,
                     val mrd: PillarDomainPoint?) :
        RecyclerView.Adapter<ResultsAdapter.ViewHolder>() {

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        holder?.bind(mItems[position], mrd)
    }

    override fun getItemCount(): Int {
        return mItems.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder{
        return ViewHolder(parent.inflate(R.layout.result_item))
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        //TODO
        fun bind(statiLimite: StatiLimite, mrd: PillarDomainPoint?)
        {

        }
    }
}