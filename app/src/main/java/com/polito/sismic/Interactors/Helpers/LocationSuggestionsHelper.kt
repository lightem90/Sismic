//package com.polito.sismic.Interactors.Helpers
//
//import android.content.Context
//import com.opencsv.bean.CsvBindByPosition
//import com.opencsv.bean.CsvToBeanBuilder
//import com.polito.sismic.Extensions.toast
//import com.polito.sismic.R
//import java.io.InputStreamReader
//
//
///**
// * Created by Matteo on 11/08/2017.
// */
//class LocationSuggestionsHelper(val mContext : Context) {
//
//    private var regions : Array<String> = arrayOf<String>()
//    //List of province for each region
//    private var provinceMapping : HashMap<String, Array<String>> = HashMap()
//    //List of localities for each province
//    private var localityMapping : HashMap<String, Array<String>> = HashMap()
//
//    //TODO: Read from csv, on separate thread?
//    fun initialize() {
//
//        var streamReader = InputStreamReader(mContext.resources.openRawResource(R.raw.istat))
//        val beans = CsvToBeanBuilder<Visitors>(streamReader)
//                .withType(Visitors::class.java).withSkipLines(1).build().parse()
//
//        //initializes regions
//        var tmpRegions = mutableListOf<String>()
//        beans.map { { visitors: Visitors -> if (visitors.regione != null) tmpRegions.add(visitors.regione) } }
//        regions = tmpRegions.distinct().toTypedArray()
//
//    }
//
//    fun getRegions() : Array<String> { return regions }
//    fun getProvinceByRegion(region : String) : Array<String>
//    {
//        var provinces = provinceMapping.get(region)
//        if (provinces == null)
//        {
//            mContext.toast(R.string.province_error)
//            return arrayOf<String>()
//        }
//        return provinces
//    }
//
//    fun getLocalityByProvince(province : String) : Array<String>
//    {
//        var localities = localityMapping.get(province)
//        if (localities == null)
//        {
//            mContext.toast(R.string.localities_error)
//            return arrayOf<String>()
//        }
//        return localities
//    }
//
//    class Visitors
//    {
//        @CsvBindByPosition(position = 0) val regione: String? = null
//
//        @CsvBindByPosition(position = 1) val province: String? = null
//
//        @CsvBindByPosition(position = 2) val codiceIstat: Int = 0
//
//        @CsvBindByPosition(position = 3) val comune: String? = null
//
//        @CsvBindByPosition(position = 4) val classificazione: String? = null
//    }
//}