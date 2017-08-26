package com.polito.sismic.Interactors.Helpers

import android.content.Context
import com.polito.sismic.Presenters.CustomLayout.ParameterReportLayout
import com.polito.sismic.R

/**
 * Created by Matteo on 22/08/2017.
 */
class LocationInfoHelper(mContext : Context){

    private val mCsvHelper : CsvHelper = CsvHelper(mContext)
    private val mProvinceForRegionMap : HashMap<String, MutableList<String>> = hashMapOf()
    private val mComuneForProvinceMap : HashMap<String, MutableList<String>> = hashMapOf()
    private val mZoneCodeForComune : HashMap<String, Triple<String, String, String>> = hashMapOf()

    fun initialize()
    {
        //Don't need return value, the callback is needed to read the file just once
        mCsvHelper.readFromRaw(R.raw.istat)
        {
            val regione : String = it[0]
            val provincia = it[1]
            val codiceistat = it[2]
            val comune = it[3]
            val classificazione = it[4]

            //List of province foreach region
            if (mProvinceForRegionMap.containsKey(regione))
            {
                mProvinceForRegionMap[regione]!!.add(provincia)
            }
            else
            {
                mProvinceForRegionMap.put(regione, mutableListOf(provincia))
            }

            //List or comune foreach province
            if (mComuneForProvinceMap.containsKey(provincia))
            {
                mComuneForProvinceMap[provincia]!!.add(comune)
            }
            else
            {
                mComuneForProvinceMap.put(provincia, mutableListOf(comune))
            }

            //mapping between comune and classification / zone
            mZoneCodeForComune.put(comune, Triple(comune, codiceistat, classificazione))
        }
    }

    fun getZoneCodeForComune(comune : String) : Pair<String, String>
    {
        if (mZoneCodeForComune.containsKey(comune))
        {
            return mZoneCodeForComune[comune]!!.second to mZoneCodeForComune[comune]!!.third
        }
        return "" to ""
    }

    fun getProvincesForRegion(region : String): Array<String>
    {
        if (mProvinceForRegionMap.containsKey(region))
        {
            return mProvinceForRegionMap[region]!!.distinct().toTypedArray()
        }
        return arrayOf()
    }

    fun getComuniForProvinces(province : String): Array<String>
    {
        if (mComuneForProvinceMap.containsKey(province))
        {
            return mComuneForProvinceMap[province]!!.distinct().toTypedArray()
        }
        return arrayOf()
    }

    fun setProvinceSuggestionForRegion(provinceParameter: ParameterReportLayout?, newRegion: String) {

        val sugg = getProvincesForRegion(newRegion)
        if (!sugg.isEmpty()) provinceParameter?.setSuggestions(sugg)
    }

    fun setComuniSuggestionForProvince(comuneParameter: ParameterReportLayout?, newProvince: String) {

        val sugg = getComuniForProvinces(newProvince)
        if (!sugg.isEmpty()) comuneParameter?.setSuggestions(sugg)
    }

    fun setZoneCodeForComune(zona_sismica_parameter: ParameterReportLayout, codice_istat_parameter: ParameterReportLayout, newComune: String) {

        val pair = getZoneCodeForComune(newComune)
        if (!pair.first.isEmpty()) codice_istat_parameter.setParameterValue(pair.first)
        if (!pair.second.isEmpty()) zona_sismica_parameter.setParameterValue(pair.second)
    }

}

