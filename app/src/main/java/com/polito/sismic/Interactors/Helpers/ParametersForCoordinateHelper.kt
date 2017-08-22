package com.polito.sismic.Interactors.Helpers

import android.content.Context
import com.polito.sismic.R

/**
 * Created by Matteo on 22/08/2017.
 */

//Could work with classes but with indexes is faster
class ParametersForCoordinateHelper(val mContext : Context) {
    // 0,  1, 2,  3,  4,   5 ,   6,   7,   8,   9,  10,  11,   12,   13,   14,   15,   16,  17,   18,    19,   20,   21,   22,   23,  24,   25,    26,   27,     28,    29
    //ID,LON,LAT,ag30,F030,Tc30,ag50,F050,Tc50,ag72,F072,Tc72,ag101,F0101,Tc101,ag140,F0140,Tc140,ag201,F0201,Tc201,ag475,F0475,Tc475,ag975,F0975,Tc975,ag2475,F02475,Tc2475
    private val mCsvHelper : CsvHelper = CsvHelper(mContext)
    //just lat/lon

    //TODO: quadtree
    private val mLatPair : MutableList<Pair<String, String>> = mutableListOf()
    private val mLonPair : MutableList<Pair<String, String>> = mutableListOf()
    private val mMap : HashMap<String, Array<String>> = hashMapOf()
    private var initialized = false

    fun initialize()
    {
        if (initialized) return
        mCsvHelper.readFromRaw(R.raw.database)
        {
            mMap.put(it[0], arrayOf(it[3], it[4], it[5], it[6], it[7], it[8], it[9], it[10], it[11], it[12], it[13], it[14], it[15], it[16], it[17],
                    it[18], it[19], it[20], it[21], it[22], it[23], it[24], it[25], it[26], it[27], it[28], it[29]))

            mLatPair.add(it[0] to it[2])
            mLonPair.add(it[0] to it[1])
        }
        initialized = true
    }

    fun getClosestTo(coordinate : Pair<String, String>) : List<Pair<String, String>>
    {
        if (!initialized) return listOf()

        return listOf()
    }
}