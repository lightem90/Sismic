package com.polito.sismic.Presenters.Adapters

import android.app.Activity
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.polito.sismic.Domain.PlantPoint
import com.polito.sismic.Extensions.inflate
import com.polito.sismic.Extensions.onConfirm
import com.polito.sismic.Extensions.toStringOrEmpty
import com.polito.sismic.Interactors.SismicPlantBuildingInteractor
import com.polito.sismic.R
import kotlinx.android.synthetic.main.plant_point_item.view.*

/**
 * Created by it0003971 on 28/09/2017.
 */
class PlantPointsAdapter(val activity: Activity,
                         val mSismicPlantBuildingInteractor: SismicPlantBuildingInteractor) : RecyclerView.Adapter<PlantPointsAdapter.ViewHolder>() {

    val items = mSismicPlantBuildingInteractor.pointList
    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        holder?.bindReport(items[position])
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = parent.inflate(R.layout.plant_point_item)
        return ViewHolder(v, mSismicPlantBuildingInteractor)
    }

    fun somethingChanged(invalidateAndReload: () -> Unit) {
        invalidateAndReload.invoke()
    }

    class ViewHolder(itemView: View, val mSismicPlantBuildingInteractor: SismicPlantBuildingInteractor) : RecyclerView.ViewHolder(itemView) {
        fun bindReport(plantPoint: PlantPoint) = with(itemView) {
            plant_x.setText(plantPoint.x.toStringOrEmpty())
            plant_x.onConfirm { plant_y.requestFocus() }

            plant_y.setText(plantPoint.y.toStringOrEmpty())
            add.setOnClickListener { mSismicPlantBuildingInteractor.addGenericPointAfter(plantPoint) }
            delete.setOnClickListener { mSismicPlantBuildingInteractor.deletePoint(plantPoint) }
            up_point.setOnClickListener { mSismicPlantBuildingInteractor.addPointOnXAfter(plantPoint) }
            down_point.setOnClickListener { mSismicPlantBuildingInteractor.addPointOnXAfter(plantPoint) }
            left_point.setOnClickListener { mSismicPlantBuildingInteractor.addPointOnYAfter(plantPoint) }
            right_point.setOnClickListener { mSismicPlantBuildingInteractor.addPointOnYAfter(plantPoint) }
        }
    }
}