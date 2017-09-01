package com.polito.sismic.Interactors.Helpers

import android.content.Context
import com.polito.sismic.Domain.NeighboursNodeData
import com.polito.sismic.Domain.NeighboursNodeSquare
import com.polito.sismic.R

//Could work with classes but with indexes is faster
class ParametersForCoordinateHelper(val mContext : Context) {

    companion object {

        val SENSIBILITY = 0.025
        fun distFrom(lat1: Double, lng1: Double, lat2: Double, lng2: Double): Double {
            val earthRadius = 6371000.0 //meters
            val dLat = Math.toRadians((lat2 - lat1))
            val dLng = Math.toRadians((lng2 - lng1))

            val a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                    Math.cos(Math.toRadians(lat1)) *
                    Math.cos(Math.toRadians(lat2)) *
                    Math.sin(dLng / 2) * Math.sin(dLng / 2)
            val c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a))
            val dist = (earthRadius * c)

            return dist / 1000 //(KM)
        }
    }
    private val mCsvHelper : CsvHelper = CsvHelper(mContext)
    //just lat/lon

    private var mCoordinateArray : Array<Triple<String, Double, Double>> = arrayOf()
    private val mMap : HashMap<String, Array<String>> = hashMapOf()
    private var initialized = false

    fun initialize()
    {
        if (initialized) return
        val tmpList = mutableListOf<Triple<String, Double, Double>>()
        mCsvHelper.readFromRaw(R.raw.database)
        {
            mMap.put(it[CoordinateDatabaseParameters.ID.ordinal], it)
            tmpList.add(Triple(it[CoordinateDatabaseParameters.ID.ordinal],
                    it[CoordinateDatabaseParameters.LON.ordinal].toDouble(),
                    it[CoordinateDatabaseParameters.LAT.ordinal].toDouble()))
        }
        mCoordinateArray = tmpList.toTypedArray()
        initialized = true
    }

    fun getClosestPointsTo(x : Double, y : Double) : NeighboursNodeSquare
    {
        if (!initialized) return NeighboursNodeSquare.Invalid
        if (x == -1.0 || y == -1.0) return NeighboursNodeSquare.Invalid
        val result = mutableListOf<Pair<String, Double>>()
        var initIndex = 0
        //get to the closest point in longitude (not safe)
        while (mCoordinateArray[initIndex].second < x) {
            initIndex++
        }

        //search left and right from initial index. it stops when the distance from a point is greater than the sum of the closest one
        //the return lists have an index for the map to the point and the distance from the input point
        val leftValues =  innerGetClosestPoint(x, y, true, initIndex, Double.MAX_VALUE, mutableListOf())
        val rightValues =  innerGetClosestPoint(x, y, false, initIndex, Double.MAX_VALUE, mutableListOf())


        result.addAll(leftValues)
        result.addAll(rightValues)
        result.sortBy { it.second }
        //retuns an object square of nodes with id, long, latitude and distance from input coordinate (in km)
        return createSquareFromCandidatesNodes(x, y, result)
    }

    private fun createSquareFromCandidatesNodes(inputLon : Double, inputLat: Double, candidatesList: List<Pair<String, Double>>): NeighboursNodeSquare {
        var candidateNE : NeighboursNodeData? = null
        var candidateNO : NeighboursNodeData? = null
        var candidateSE : NeighboursNodeData? = null
        var candidateSO : NeighboursNodeData? = null

        //build a square if possible
        candidatesList.forEach {
            val candidateLon = getDataForNode(it.first, CoordinateDatabaseParameters.LON)
            val candidateLat = getDataForNode(it.first, CoordinateDatabaseParameters.LAT)
            if (candidateLon <= inputLon && candidateLat <= inputLat && candidateSE == null)
            {
                candidateSE = NeighboursNodeData(it.first, candidateLon, candidateLat, distFrom(inputLat, inputLon, candidateLat, candidateLon))
            }
            else if (candidateLon <= inputLon && candidateLat > inputLat && candidateNE == null)
            {
                candidateNE = NeighboursNodeData(it.first, candidateLon, candidateLat, distFrom(inputLat, inputLon, candidateLat, candidateLon))
            }
            else if (candidateLon > inputLon && candidateLat > inputLat && candidateNO == null)
            {
                candidateNO = NeighboursNodeData(it.first, candidateLon, candidateLat, distFrom(inputLat, inputLon, candidateLat, candidateLon))
            }
            else if (candidateLon > inputLon && candidateLat <= inputLat && candidateSO == null)
            {
                candidateSO = NeighboursNodeData(it.first, candidateLon, candidateLat, distFrom(inputLat, inputLon, candidateLat, candidateLon))
            }
        }

        //can interpolate on 3 points
        var count = 0
        if (candidateNE == null)
        {
            candidateNE = NeighboursNodeData.Invalid
            count++
        }
        if (candidateSE == null)
        {
            candidateSE = NeighboursNodeData.Invalid
            count++
        }
        if (candidateSO == null)
        {
            candidateSO = NeighboursNodeData.Invalid
            count++
        }
        if (candidateNO == null)
        {
            candidateNO = NeighboursNodeData.Invalid
            count++
        }
        return NeighboursNodeSquare(candidateNE!!, candidateNO!!, candidateSO!!, candidateSE!!, count <=1 )
    }

    private fun getDataForNode(nodeId : String, param : CoordinateDatabaseParameters) : Double
    {
        return mMap[nodeId]!![param.ordinal].toDouble()
    }

    private fun innerGetClosestPoint(x: Double, y: Double,
                                     left: Boolean,    //direction
                                     index: Int,
                                     limitDistance: Double,
                                     tmpList: MutableList<Pair<String, Double>>) : List<Pair<String, Double>>
    {
        var innerIndex = if (left) index -1 else index +1
        if (innerIndex < 0 || innerIndex >= mCoordinateArray.size) return tmpList

        //Stopping condition, the distance of the next point from input point is greater than sum of distances of 4 closest points
        if (calulateDistance(x to y, mCoordinateArray[innerIndex].second to mCoordinateArray[innerIndex].third) >= limitDistance)
        {
            return tmpList
        }

        //Consider all very close (on x) points
        while (Math.abs(x - mCoordinateArray[innerIndex].second) <= SENSIBILITY)
        {
            //add point id with distance from input point
            tmpList.add(mCoordinateArray[innerIndex].first to
                calulateDistance(x to y,
                        mCoordinateArray[innerIndex].second to mCoordinateArray[innerIndex].third))

            if (left) innerIndex-- else innerIndex++
        }

        //I just want 4 points, the one with the minimum distance
        tmpList.sortBy { it.second }
        val smallerList = tmpList.subList(0, 4) //inclusive from exclusive to

        //pass to recursion: input point, new index, new sum, new points
        return innerGetClosestPoint(x, y, left, innerIndex, smallerList.sumByDouble { it.second }, smallerList)
    }

    fun calulateDistance(pair1 : Pair<Double, Double>, pair2 : Pair<Double, Double>) : Double
    {
        val distX = Math.abs(pair1.first - pair2.first)
        val distY = Math.abs(pair1.second - pair2.second)
        return Math.sqrt(distX*distX + distY*distY)
    }
}