package com.polito.sismic.Interactors.Helpers

import android.content.Context
import com.polito.sismic.Extensions.toast
import com.polito.sismic.R

/**
 * Created by Matteo on 11/08/2017.
 */
class LocationSuggestionsHelper(val mContext : Context) {

    private var regions : Array<String> = arrayOf<String>()
    //List of province for each region
    private var provinceMapping : HashMap<String, Array<String>> = HashMap()
    //List of localities for each province
    private var localityMapping : HashMap<String, Array<String>> = HashMap()

    //TODO: Read from csv, on separate thread?
    fun initialize() {

    }

    fun getRegions() : Array<String> { return regions }
    fun getProvinceByRegion(region : String) : Array<String>
    {
        var provinces = provinceMapping.get(region)
        if (provinces == null)
        {
            mContext.toast(R.string.province_error)
            return arrayOf<String>()
        }
        return provinces
    }

    fun getLocalityByProvince(province : String) : Array<String>
    {
        var localities = localityMapping.get(province)
        if (localities == null)
        {
            mContext.toast(R.string.localities_error)
            return arrayOf<String>()
        }
        return localities
    }
}