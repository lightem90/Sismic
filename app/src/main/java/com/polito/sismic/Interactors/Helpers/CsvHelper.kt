package com.polito.sismic.Interactors.Helpers

import android.content.Context
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader

/**
 * Created by Matteo on 22/08/2017.
 */

class CsvHelper(val mContext : Context)
{
    fun readFromRaw(rawId : Int, function: (m: Array<String>) -> Unit) : List<Array<String>>
    {
        val toReturn = mutableListOf<Array<String>>()
        val inputStreamReader: InputStreamReader
        try {
            inputStreamReader = InputStreamReader(mContext.resources.openRawResource(rawId))
            val inputStream = BufferedReader(inputStreamReader)
            inputStream.readLine() // Ignores the first line
            inputStream.forEachLine {
                val arrayToAdd = it.split(",".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray() // Splits the line up into a string array
                function.invoke(arrayToAdd)
                toReturn.add(arrayToAdd)
            }

            inputStream.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        finally {
            return toReturn
        }

    }
}