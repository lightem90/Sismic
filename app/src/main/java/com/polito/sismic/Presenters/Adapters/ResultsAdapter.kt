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

    val results: HashMap<StatiLimite, Double> = HashMap()
    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        holder?.bind(mItems[position], pointsList, pointsList.firstOrNull { it.label == "MRD" }, context, results)
    }

    override fun getItemCount(): Int {
        return mItems.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(parent.inflate(R.layout.result_item))
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private var hidden = false
        fun bind(statoLimite: StatiLimite,
                 pointsList: List<PillarDomainPoint>?,
                 mrd: PillarDomainPoint?,
                 context: Context,
                 results: HashMap<StatiLimite, Double>) {
            itemView.label.setTextColor(ContextCompat.getColor(context, statoLimite.color))
            if (mrd == null) return
            val pointForStato = pointsList?.firstOrNull { it.label == statoLimite.name }
            pointForStato?.let {

                val result = (mrd.m / it.m)
                val perc = result * 100
                val resPerc = if (perc >= 100.0) 100.0 else perc
                itemView.label.text = String.format(context.getString(R.string.result_label), statoLimite.name, result)
                itemView.value.text = String.format(context.getString(R.string.result),  resPerc, context.getString(R.string.percent))
                itemView.progress.progress = resPerc.toInt()
                results.put(statoLimite, resPerc)
                itemView.setOnClickListener { hideShowDetails() }
                itemView.n_value.text = String.format(context.getString(R.string.result_format), it.n)
                itemView.m_value.text = String.format(context.getString(R.string.result_format), it.m)
                itemView.mrd_value.text = String.format(context.getString(R.string.result_format), mrd.m)
                itemView.sd_value.text = String.format(context.getString(R.string.domain_point_value_format_high), it.sd)
                itemView.fh_value.text = String.format(context.getString(R.string.domain_point_value_format_high), it.fh)
                itemView.ty_value.text = String.format(context.getString(R.string.domain_point_value_format_high), it.ty)
            }
        }

        private fun hideShowDetails() {
            if (hidden) {
                //show
                itemView.canvas.visibility = View.VISIBLE
                (0 until itemView.canvas.childCount)
                        .map { itemView.canvas.getChildAt(it) }
                        .forEach {
                            it.visibility = View.VISIBLE // Or whatever you want to do with the view.
                        }
                hidden = false
            } else {
                //hide
                itemView.canvas.visibility = View.GONE
                (0 until itemView.canvas.childCount)
                        .map { itemView.canvas.getChildAt(it) }
                        .forEach {
                            it.visibility = View.GONE // Or whatever you want to do with the view.
                        }
                hidden = true
            }
        }
    }
}