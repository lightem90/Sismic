package com.polito.sismic.Presenters.Adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.polito.sismic.Domain.PillarDomainPoint
import com.polito.sismic.Extensions.inflate
import com.polito.sismic.Interactors.Helpers.StatiLimite
import com.polito.sismic.R
import kotlinx.android.synthetic.main.result_item.view.*

/**
 * Created by Matteo on 07/10/2017.
 */
class ResultsAdapter(val mItems: List<StatiLimite>,
                     val context: Context,
                     val pointsList: List<PillarDomainPoint>?) :
        RecyclerView.Adapter<ResultsAdapter.ViewHolder>() {

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        holder?.bind(mItems[position], pointsList, pointsList?.firstOrNull{it.label == "MRD"}, context)
    }

    override fun getItemCount(): Int {
        return mItems.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder{
        return ViewHolder(parent.inflate(R.layout.result_item))
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(statoLimite: StatiLimite, pointsList: List<PillarDomainPoint>?, mrd: PillarDomainPoint?, context: Context)
        {
            itemView.label.text = statoLimite.name
            if (mrd == null) return
            val pointForStato = pointsList?.firstOrNull{it.label == statoLimite.name}
            pointForStato?.let {

                val result = (it.m / mrd.m).toInt()
                itemView.value.text = String.format(context.getString(R.string.result),result.toString())
                itemView.progress.progress = result
            }
        }
    }
}