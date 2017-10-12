package com.polito.sismic.Interactors.Helpers

import android.os.Parcel
import android.os.Parcelable
import com.polito.sismic.Domain.PlantPoint
import java.util.*

/**
 * Created by it0003971 on 10/10/2017.
 */
data class PlantFigure(val name: String, val edges: Array<PlantEdge>) : Parcelable {
    operator fun contains(p: PlantPoint) = edges.count({ it(p) }) % 2 != 0

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as PlantFigure

        if (name != other.name) return false
        if (!Arrays.equals(edges, other.edges)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + Arrays.hashCode(edges)
        return result
    }

    constructor(source: Parcel) : this(
            source.readString(),
            source.readParcelableArray(PlantEdge::class.java.classLoader) as Array<PlantEdge>
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeString(name)
        writeParcelableArray(edges, 0)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<PlantFigure> = object : Parcelable.Creator<PlantFigure> {
            override fun createFromParcel(source: Parcel): PlantFigure = PlantFigure(source)
            override fun newArray(size: Int): Array<PlantFigure?> = arrayOfNulls(size)
        }
    }
}

data class PlantEdge(val s: PlantPoint, val e: PlantPoint) : Parcelable {
    operator fun invoke(p: PlantPoint): Boolean = when {
        //fuck performances...
        s.y > e.y -> PlantEdge(e, s).invoke(p)
        (p.x == s.x && p.y == s.y) || (p.x == e.x && p.y == e.y) ->
            invoke(PlantPoint(p.x + epsilon, p.y - epsilon)) ||
            invoke(PlantPoint(p.x + epsilon, p.y + epsilon)) ||
            invoke(PlantPoint(p.x - epsilon, p.y + epsilon)) ||
            invoke(PlantPoint(p.x - epsilon, p.y - epsilon))

        p.y == s.y || p.y == e.y -> if (!invoke(PlantPoint(p.x, p.y + epsilon))) invoke(PlantPoint(p.x, p.y - epsilon)) else true
        p.x == s.x || p.x == e.x -> if (!invoke(PlantPoint(p.x + epsilon, p.y))) invoke(PlantPoint(p.x - epsilon, p.y)) else true
        p.y > e.y || p.y < s.y || p.x > Math.max(s.x, e.x) -> false
        p.x < Math.min(s.x, e.x) -> true
        else -> {
            val blue = if (Math.abs(s.x - p.x) > java.lang.Double.MIN_VALUE) (p.y - s.y) / (p.x - s.x) else java.lang.Double.MAX_VALUE
            val red = if (Math.abs(s.x - e.x) > java.lang.Double.MIN_VALUE) (e.y - s.y) / (e.x - s.x) else java.lang.Double.MAX_VALUE
            blue >= red
        }
    }

    private val epsilon = 0.0001

    constructor(source: Parcel) : this(
            source.readParcelable<PlantPoint>(PlantPoint::class.java.classLoader),
            source.readParcelable<PlantPoint>(PlantPoint::class.java.classLoader)
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeParcelable(s, 0)
        writeParcelable(e, 0)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<PlantEdge> = object : Parcelable.Creator<PlantEdge> {
            override fun createFromParcel(source: Parcel): PlantEdge = PlantEdge(source)
            override fun newArray(size: Int): Array<PlantEdge?> = arrayOfNulls(size)
        }
    }
}