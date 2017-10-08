package com.polito.sismic.Presenters.Adapters

import android.content.Context
import android.support.v4.content.ContextCompat
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
class ResultsAdapter(private val mItems: List<StatiLimite>,
                     val context: Context,
                     private val pointsList: List<PillarDomainPoint>) :
        RecyclerView.Adapter<ResultsAdapter.ViewHolder>() {

    val results : HashMap<StatiLimite, Int> = HashMap()
    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        holder?.bind(mItems[position], pointsList, pointsList.firstOrNull{it.label == "MRD"}, context, results)
    }

    override fun getItemCount(): Int {
        return mItems.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder{
        return ViewHolder(parent.inflate(R.layout.result_item))
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(statoLimite: StatiLimite, pointsList: List<PillarDomainPoint>?, mrd: PillarDomainPoint?, context: Context, results: HashMap<StatiLimite, Int>)
        {
            itemView.label.text = statoLimite.name
            itemView.label.setTextColor(ContextCompat.getColor(context, statoLimite.color))
            if (mrd == null) return
            val pointForStato = pointsList?.firstOrNull{it.label == statoLimite.name}
            pointForStato?.let {

                val result = ((it.m / mrd.m)*100).toInt()
                itemView.value.text = String.format(context.getString(R.string.result), result, context.getString(R.string.percent))
                itemView.progress.progress = result


                when(result)
                {
                    in Int.MIN_VALUE..10 -> itemView.progress.progressDrawable.setColorFilter(ContextCompat.getColor(context, R.color.report_danger_low), android.graphics.PorterDuff.Mode.SRC_IN)
                    in 11..35 -> itemView.progress.progressDrawable.setColorFilter(ContextCompat.getColor(context, R.color.report_danger_normal), android.graphics.PorterDuff.Mode.SRC_IN)
                    in 36..50 -> itemView.progress.progressDrawable.setColorFilter(ContextCompat.getColor(context, R.color.report_danger_medium), android.graphics.PorterDuff.Mode.SRC_IN)
                    in 51..Int.MAX_VALUE -> itemView.progress.progressDrawable.setColorFilter(ContextCompat.getColor(context, R.color.report_danger_high), android.graphics.PorterDuff.Mode.SRC_IN)
                }
                results.put(statoLimite, result)
            }
        }
    }
}