package com.polito.sismic.Presenters.Adapters

import android.app.Activity
import android.support.v7.widget.RecyclerView
import android.text.InputType
import android.view.View
import android.view.ViewGroup
import com.polito.sismic.Domain.PlantPoint
import com.polito.sismic.Extensions.inflate
import com.polito.sismic.Extensions.onConfirm
import com.polito.sismic.Extensions.toDoubleOrZero
import com.polito.sismic.Extensions.toStringOrEmpty
import com.polito.sismic.Interactors.SismicPlantBuildingInteractor
import com.polito.sismic.R
import kotlinx.android.synthetic.main.plant_point_item.view.*

/**
 * Created by it0003971 on 28/09/2017.
 */
class PlantPointsAdapter(val activity: Activity,
                         val mSismicPlantBuildingInteractor: SismicPlantBuildingInteractor,
                         val invalidateAndReload: () -> Unit) : RecyclerView.Adapter<PlantPointsAdapter.ViewHolder>() {


    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        holder?.bindReport(mSismicPlantBuildingInteractor.pointList[position], position == 0)
    }

    override fun getItemCount(): Int {
        return mSismicPlantBuildingInteractor.pointList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = parent.inflate(R.layout.plant_point_item)
        return ViewHolder(v, mSismicPlantBuildingInteractor, invalidateAndReload)
    }

    class ViewHolder(itemView: View, val mSismicPlantBuildingInteractor: SismicPlantBuildingInteractor, val invalidateAndReload: () -> Unit) : RecyclerView.ViewHolder(itemView) {

        fun bindReport(plantPoint: PlantPoint, locked: Boolean) = with(itemView) {

            plant_x.setText(plantPoint.x.toStringOrEmpty())
            plant_x.inputType = InputType.TYPE_NUMBER_FLAG_DECIMAL or InputType.TYPE_CLASS_NUMBER

            //Special behaviour for first item
            if (!locked)
            {
                plant_x.onConfirm {
                    plantPoint.x = plant_x.text.toString().toDoubleOrZero()
                    plant_y.requestFocus()
                    invalidateAndReload.invoke()
                }
            }
            else
            {
                plant_x.setText(0.0.toString())
                plant_x.isFocusable = false
            }

            plant_y.setText(plantPoint.y.toStringOrEmpty())
            plant_y.inputType = InputType.TYPE_NUMBER_FLAG_DECIMAL or InputType.TYPE_CLASS_NUMBER
            if (!locked)
            {
                plant_y.onConfirm {
                    plantPoint.y = plant_y.text.toString().toDoubleOrZero()
                    invalidateAndReload.invoke()
                }
            }
            else
            {
                plant_y.setText(0.0.toString())
                plant_y.isFocusable = false
            }

            add.setOnClickListener {
                mSismicPlantBuildingInteractor.addGenericPointAfter(plantPoint)
                invalidateAndReload.invoke()
            }

            delete.setOnClickListener {
                mSismicPlantBuildingInteractor.deletePoint(plantPoint)
                invalidateAndReload.invoke()
            }

            up_point.setOnClickListener {
                mSismicPlantBuildingInteractor.addPointOnXAfter(plantPoint)
                invalidateAndReload.invoke()
            }

            right_point.setOnClickListener {
                mSismicPlantBuildingInteractor.addPointOnYAfter(plantPoint)
                invalidateAndReload.invoke()
            }

            close.setOnClickListener{
                mSismicPlantBuildingInteractor.closePlant()
                invalidateAndReload.invoke()
            }
        }
    }
}