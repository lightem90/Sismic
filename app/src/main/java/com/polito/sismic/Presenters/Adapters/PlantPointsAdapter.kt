package com.polito.sismic.Presenters.Adapters

import android.app.Activity
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.polito.sismic.Domain.PlantPoint
import com.polito.sismic.Extensions.inflate
import com.polito.sismic.Interactors.SismicPlantBuildingInteractor
import com.polito.sismic.R
import kotlinx.android.synthetic.main.plant_point_item.view.*

/**
 * Created by it0003971 on 28/09/2017.
 */
class PlantPointsAdapter(val activity: Activity,
                         private val mSismicPlantBuildingInteractor: SismicPlantBuildingInteractor,
                         private val invalidateAndReload: () -> Unit) : RecyclerView.Adapter<PlantPointsAdapter.ViewHolder>() {


    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        holder?.bindReport(mSismicPlantBuildingInteractor.pointList[position], position, position == mSismicPlantBuildingInteractor.pointList.size-1)
    }

    override fun getItemCount(): Int {
        return mSismicPlantBuildingInteractor.pointList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = parent.inflate(R.layout.plant_point_item)
        return ViewHolder(v, mSismicPlantBuildingInteractor, activity, invalidateAndReload)
    }

    class ViewHolder(itemView: View,
                     val mSismicPlantBuildingInteractor: SismicPlantBuildingInteractor,
                     val activity : Activity,
                     val invalidateAndReload: () -> Unit) : RecyclerView.ViewHolder(itemView) {

        fun bindReport(plantPoint: PlantPoint, position: Int, isLast: Boolean) = with(itemView) {

            plant_x.text = String.format(context.getString(R.string.plant_point_x), "%.2f".format(plantPoint.x))
            plant_y.text = String.format(context.getString(R.string.plant_point_y), "%.2f".format(plantPoint.y))

            if (position == 0) delete.visibility = View.GONE
            if (isLast) close.visibility = View.VISIBLE
            else close.visibility = View.GONE

            add.setOnClickListener {
                mSismicPlantBuildingInteractor.addGenericPointAfter(activity, plantPoint, invalidateAndReload)
            }

            delete.setOnClickListener {
                mSismicPlantBuildingInteractor.deletePoint(plantPoint)
                invalidateAndReload()
            }

            close.setOnClickListener {
                mSismicPlantBuildingInteractor.closePlant(context)
                invalidateAndReload()
            }
        }
    }
}