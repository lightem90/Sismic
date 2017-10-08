package com.polito.sismic.Interactors.Helpers

import android.content.Context
import android.util.Log
import com.polito.sismic.Domain.NeighboursNodeData
import com.polito.sismic.Domain.NeighboursNodeSquare
import com.polito.sismic.R

//Could work with classes but with indexes is faster
class ParametersForCoordinateHelper(val mContext : Context) {

    //DB precision for lat and long is 3 decimal
    companion object {

        //elements are very far in the database
        val SENSIBILITY = 0.10
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

        var QUAD_LUT = intArrayOf(1, 2, 4, 3)
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
        val result = mutableListOf<Triple<String, Double, Int>>()
        var initIndex = 0
        //get to the closest point in longitude (not safe)
        while (mCoordinateArray[initIndex].second < x) {
            initIndex++
        }

        //search left and right from initial index. it stops when the distance from a point is greater than the sum of the closest one
        //the return lists have an index for the map to the point and the distance from the input point
        val leftValues =  innerGetClosestPoint(x, y, true, initIndex, Double.MAX_VALUE, mutableListOf(), 0)
        val rightValues =  innerGetClosestPoint(x, y, false, initIndex, Double.MAX_VALUE, mutableListOf(), 0)

        Log.d("End of recursion", "Left list:" + leftValues.toString() + " Right list: " + rightValues.toString())
        result.addAll(leftValues)
        result.addAll(rightValues)
        //order by distance
        result.sortBy { it.second }
        Log.d("End of recursion", "Sorted list:" + result.toString())
        //retuns an object square of nodes with id, long, latitude and distance from input coordinate (in km)
        return createSquareFromCandidatesNodes(x, y, result)
    }

    fun getRowDataForNode(requestedNodeId : String) : Array<String>
    {
        if (!initialized)
            initialize()

        if (!mMap.containsKey(requestedNodeId)) return emptyArray()
        return mMap[requestedNodeId]!!
    }

    private fun createSquareFromCandidatesNodes(inputLon : Double, inputLat: Double, candidatesList: List<Triple<String, Double, Int>>): NeighboursNodeSquare {
        var candidateNE : NeighboursNodeData? = null
        var candidateNO : NeighboursNodeData? = null
        var candidateSE : NeighboursNodeData? = null
        var candidateSO : NeighboursNodeData? = null

        //candidatesList.forEachIndexed { index, _ ->
        //    val candidateLon = getDataForNode(candidatesList[index].first, CoordinateDatabaseParameters.LON)
        //    val candidateLat = getDataForNode(candidatesList[index].first, CoordinateDatabaseParameters.LAT)
        //    if (candidateLon <= inputLon && candidateLat <= inputLat && candidateSE == null)
        //    {
        //        candidateSE = NeighboursNodeData(candidatesList[index].first, candidateLon, candidateLat, distFrom(inputLat, inputLon, candidateLat, candidateLon))
        //    }
        //    else if (candidateLon <= inputLon && candidateLat > inputLat && candidateNE == null)
        //    {
        //        candidateNE = NeighboursNodeData(candidatesList[index].first, candidateLon, candidateLat, distFrom(inputLat, inputLon, candidateLat, candidateLon))
        //    }
        //    else if (candidateLon > inputLon && candidateLat > inputLat && candidateNO == null)
        //    {
        //        candidateNO = NeighboursNodeData(candidatesList[index].first, candidateLon, candidateLat, distFrom(inputLat, inputLon, candidateLat, candidateLon))
        //    }
        //    else if (candidateLon > inputLon && candidateLat <= inputLat && candidateSO == null)
        //    {
        //        candidateSO = NeighboursNodeData(candidatesList[index].first, candidateLon, candidateLat, distFrom(inputLat, inputLon, candidateLat, candidateLon))
        //    }
        //}

        //build a square if possible
        Log.d("Input points", "Input longitude is: "+ inputLon + " Input latitude is:" + inputLat)
        candidatesList.forEach {
            val candidateLon = getDataForNode(it.first, CoordinateDatabaseParameters.LON)
            val candidateLat = getDataForNode(it.first, CoordinateDatabaseParameters.LAT)
            when(it.third)
            {
                1 ->
                {
                    if (candidateNE == null)
                    {
                        candidateNE = NeighboursNodeData(it.first,
                                candidateLon,
                                candidateLat,
                                distFrom(inputLat, inputLon, candidateLat, candidateLon))
                        Log.d("NENode", candidateNE.toString())
                    }
                }
                2 ->
                {
                    if (candidateSE == null)
                    {
                        candidateSE = NeighboursNodeData(it.first,
                                candidateLon,
                                candidateLat,
                                distFrom(inputLat, inputLon, candidateLat, candidateLon))
                        Log.d("SENode", candidateSE.toString())
                    }
                }
                3 ->
                {
                    if (candidateSO == null)
                    {
                        candidateSO = NeighboursNodeData(it.first,
                                candidateLon,
                                candidateLat,
                                distFrom(inputLat, inputLon, candidateLat, candidateLon))
                        Log.d("SONode", candidateSO.toString())
                    }
                }
                4 ->
                {
                    if (candidateNO == null)
                    {
                        candidateNO = NeighboursNodeData(it.first,
                                candidateLon,
                                candidateLat,
                                distFrom(inputLat, inputLon, candidateLat, candidateLon))
                        Log.d("NNode", candidateNO.toString())
                    }
                }
                else -> throw Exception()
            }
        }

        //can interpolate on 3 points
        var nullCounter = 0
        if (candidateNE == null)
        {
            candidateNE = NeighboursNodeData.Invalid
            nullCounter++
        }
        if (candidateSE == null)
        {
            candidateSE = NeighboursNodeData.Invalid
            nullCounter++
        }
        if (candidateSO == null)
        {
            candidateSO = NeighboursNodeData.Invalid
            nullCounter++
        }
        if (candidateNO == null)
        {
            candidateNO = NeighboursNodeData.Invalid
            nullCounter++
        }

        return NeighboursNodeSquare(candidateNE!!, candidateNO!!, candidateSO!!, candidateSE!!, nullCounter == 0 )
    }

    private fun getDataForNode(nodeId : String, param : CoordinateDatabaseParameters) : Double
    {
        return mMap[nodeId]!![param.ordinal].toDouble()
    }

    private fun innerGetClosestPoint(x: Double, y: Double,
                                     left: Boolean,    //direction
                                     index: Int,
                                     limitDistance: Double,
                                     tmpList: MutableList<Triple<String, Double, Int>>,
                                     level: Int) : List<Triple<String, Double, Int>>
    {
        var innerIndex = index
        if (innerIndex < 0 || innerIndex >= mCoordinateArray.size) return tmpList

        if (level > 10) {
            Log.d("Level", "Max depth reached " + level)
            return tmpList
        }

        Log.d("Distance", "Limit distance is: " + limitDistance)

        //Stopping condition, the distance of the next point from input point is greater than sum of distances of 4 closest points
        val dist_quadrant_pair = calulateDistance(x to y, mCoordinateArray[innerIndex].second to mCoordinateArray[innerIndex].third)
        Log.d("Distance", "Distance is " + dist_quadrant_pair.first + " and quadrant is " + dist_quadrant_pair.second)
        if (dist_quadrant_pair.first >= limitDistance)
        {
            return tmpList
        }

        //Consider all very close (on x) points
        while (Math.abs(x - mCoordinateArray[innerIndex].second) <= SENSIBILITY)
        {
            //add point id with close distance from input point
            val tripleForPoint = calulateDistance(x to y, mCoordinateArray[innerIndex].second to mCoordinateArray[innerIndex].third)
            val point = Triple(mCoordinateArray[innerIndex].first, tripleForPoint.first, tripleForPoint.second)
            tmpList.add(point)
            Log.d("InnerList", "Adding " + point.toString() + " left is: " + left)

            if (left) innerIndex-- else innerIndex++
            if (innerIndex < 0 || innerIndex >= mCoordinateArray.size) return tmpList
        }

        //I just want 4 points, the one with the minimum distance
        tmpList.sortBy { it.second }
        val smallerList = if (tmpList.size > 3) tmpList.subList(0, 4) else tmpList //inclusive from exclusive to
        Log.d("SmallerList", "Reduced list is: " + smallerList.toString())

        //pass to recursion: input point, new index, new sum, new points
        return innerGetClosestPoint(x, y, left, innerIndex, smallerList.sumByDouble { it.second }, smallerList, level+1)
    }

    private fun calulateDistance(pair1 : Pair<Double, Double>, pair2 : Pair<Double, Double>) : Pair<Double, Int>
    {
        //db precision is 3 decimal, we check 4
        Log.d("Distance", "Distance of " + pair1.first + " from " + pair2.first + " and " + pair1.second + " from " + pair2.second)
        //val xDiff = BigDecimal(pair1.first - pair2.first).setScale(4, BigDecimal.ROUND_DOWN);
        //val yDiff = BigDecimal(pair1.second - pair2.second).setScale(4, BigDecimal.ROUND_DOWN);
        //val zero = BigDecimal(0).setScale(4, BigDecimal.ROUND_DOWN);
        //val distX = Math.abs(xDiff.toDouble())
        //val distY = Math.abs(yDiff.toDouble())
        val xDiff = pair1.first - pair2.first
        val yDiff = pair1.second - pair2.second
        val zero = 0.0
        val distX = Math.abs(xDiff)
        val distY = Math.abs(yDiff)
        //distance and quadrant
        var quad = -1
        if (xDiff >= zero && yDiff >= zero) quad = 1
        else if (xDiff <= zero && yDiff >= zero) quad = 2
        else if (xDiff <= zero && yDiff <= zero) quad = 3
        else if (xDiff >= zero && yDiff <= zero) quad = 4
        //(xDiff) shr 31 or (((yDiff) shr 30) and 0x2)
        //works for int..
        //y = y>>31;
        //return ((x>>31) ^ y) + y + y + 1;
        return Math.sqrt(distX*distX + distY*distY) to quad
    }
}