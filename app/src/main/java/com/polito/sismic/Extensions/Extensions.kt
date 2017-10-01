package com.polito.sismic.Extensions

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.database.sqlite.SQLiteDatabase
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import com.polito.sismic.Presenters.Adapters.ReportFragmentsAdapter
import com.stepstone.stepper.adapter.StepAdapter
import org.jetbrains.anko.db.MapRowParser
import org.jetbrains.anko.db.SelectQueryBuilder
import java.text.SimpleDateFormat
import java.util.*
import kotlin.reflect.KProperty
import android.provider.OpenableColumns
import android.view.*
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import com.github.mikephil.charting.data.Entry
import com.polito.sismic.Domain.*
import android.view.inputmethod.InputMethodManager.HIDE_NOT_ALWAYS
import android.widget.EditText


/**
 * Created by Matteo on 07/08/2017.
 */

//Context extensions
fun Context.toast(resourceId: Int) = toast(getString(resourceId))

fun Context.toast(message: CharSequence) =
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

inline fun SharedPreferences.edit(func: SharedPreferences.Editor.() -> Unit) {
    val editor = edit()
    editor.func()
    editor.apply()
}

object DelegatesExt {
    fun <T> notNullSingleValue() = NotNullSingleValueVar<T>()
    fun <T> preference(context: Context, name: String,
                       default: T) = Preference(context, name, default)
}

fun <T : Any> SelectQueryBuilder.parseList(parser: (Map<String, Any?>) -> T): List<T> =
        parseList(object : MapRowParser<T> {
            override fun parseRow(columns: Map<String, Any?>): T = parser(columns)
        })

fun <T : Any> SelectQueryBuilder.parseOpt(parser: (Map<String, Any?>) -> T): T? =
        parseOpt(object : MapRowParser<T> {
            override fun parseRow(columns: Map<String, Any?>): T = parser(columns)
        })

fun SQLiteDatabase.clear(tableName: String) {
    execSQL("delete from $tableName")
}

fun SelectQueryBuilder.byId(id: String) = whereSimple("_id = ?", id)
fun SelectQueryBuilder.byReportId(id: String) = whereSimple("report_id = ?", id)

class NotNullSingleValueVar<T> {

    private var value: T? = null

    operator fun getValue(thisRef: Any?, property: KProperty<*>): T {
        return value ?: throw IllegalStateException("${property.name} not initialized")
    }

    operator fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
        this.value = if (this.value == null) value
        else throw IllegalStateException("${property.name} already initialized")
    }
}

class Preference<T>(val context: Context, val name: String, val default: T) {

    val prefs: SharedPreferences by lazy { context.getSharedPreferences("default", Context.MODE_PRIVATE) }

    operator fun getValue(thisRef: Any?, property: KProperty<*>): T {
        return findPreference(name, default)
    }

    operator fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
        putPreference(name, value)
    }

    @Suppress("UNCHECKED_CAST")
    private fun findPreference(name: String, default: T): T = with(prefs) {
        val res: Any = when (default) {
            is Long -> getLong(name, default)
            is String -> getString(name, default)
            is Int -> getInt(name, default)
            is Boolean -> getBoolean(name, default)
            is Float -> getFloat(name, default)
            else -> throw IllegalArgumentException("This type can be saved into Preferences")
        }

        res as T
    }

    @SuppressLint("CommitPrefEdits")
    private fun putPreference(name: String, value: T) = with(prefs.edit()) {
        when (value) {
            is Long -> putLong(name, value)
            is String -> putString(name, value)
            is Int -> putInt(name, value)
            is Boolean -> putBoolean(name, value)
            is Float -> putFloat(name, value)
            else -> throw IllegalArgumentException("This type can't be saved into Preferences")
        }.apply()
    }
}

fun String.toFormattedDate(): Date {
    return SimpleDateFormat("yyyy-MM-dd HH:mm:ss ").parse(this)
}

fun Date.toFormattedString(): String {
    return SimpleDateFormat("yyyy-MM-dd HH:mm:ss ").format(this)
}

fun <K, V : Any> Map<K, V?>.toVarargArray(): Array<out Pair<K, V>> =
        map({ Pair(it.key, it.value!!) }).toTypedArray()

fun Bundle.putReport(state: Report) {
    putParcelable("report_state", state)
}

fun Bundle.getReport(): Report? {
    return getParcelable<Report>("report_state")
}

fun ViewGroup.inflate(layoutRes: Int): View {
    return LayoutInflater.from(context).inflate(layoutRes, this, false)
}

fun Double.getPowerOfTwoForSampleRatio(): Int {
    val k = Integer.highestOneBit(Math.floor(this).toInt())
    if (k == 0)
        return 1
    else
        return k
}

fun StepAdapter.getCustomAdapter(): ReportFragmentsAdapter {
    return this as ReportFragmentsAdapter
}

fun NeighboursNodeSquare.toList(): List<NeighboursNodeData> {
    return listOf(NE, NO, SO, SE)
}

fun Uri.getFileName(mContext: Context): String? {
    val returnCursor = mContext.contentResolver.query(this, null, null, null, null)
    val nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
    returnCursor.moveToFirst()
    val filename = returnCursor.getString(nameIndex)
    returnCursor.close()
    return filename
}

fun Uri.getSizeInMb(mContext: Context): Double {
    val returnCursor = mContext.contentResolver.query(this, null, null, null, null)
    returnCursor.moveToFirst()
    val sizeIndex = returnCursor.getLong(returnCursor.getColumnIndex(OpenableColumns.SIZE))
    returnCursor.close()
    val doubleSizeIndex = sizeIndex.toDouble()
    return doubleSizeIndex / 1024 / 1024
}

fun Uri.getMediaPath(mContext: Context): String {
    val projection = arrayOf(MediaStore.Images.Media.DATA)
    val cursor = mContext.contentResolver.query(this, projection, null, null, null)
    val column_index = cursor
            .getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
    cursor.moveToFirst()
    val toReturn = cursor.getString(column_index)
    cursor.close()
    return toReturn
}

fun String.toUri(): Uri {
    return Uri.parse(this)
}

fun List<Entry>.toSpectrumPointList(): List<SpectrumPoint> {
    return map { SpectrumPoint(it.x.toDouble(), it.y.toDouble()) }
}


fun List<SpectrumPoint>.toEntryList(): List<Entry> {
    return map { Entry(it.x.toFloat(), it.y.toFloat()) }
}

fun PeriodData.interpolateWith(next: PeriodData, newYear: Int): PeriodData {
    val newAg = MathUti.periodDataInterpolation(ag, next.ag, years, next.years, newYear)
    val newF0 = MathUti.periodDataInterpolation(f0, next.f0, years, next.years, newYear)
    val newTcStar = MathUti.periodDataInterpolation(tcstar, next.tcstar, years, next.years, newYear)
    return PeriodData(newYear, newAg, newF0, newTcStar)
}

fun String.toDoubleOrZero(): Double {

    return if (isEmpty()) 0.0 else
    {
        var tmp = this
        tmp = tmp.replace(",", ".")
        return tmp.toDouble()
    }
}

fun String.toIntOrZero(): Int {
    return if (isEmpty()) 0 else toInt()
}

fun Double.toStringOrEmpty(): String {
    return if (this == 0.0) "" else toString()
}

fun Int.toStringOrEmpty(): String {
    return if (this == 0) "" else toString()
}

//better safe than sorry
fun Activity.hideSoftKeyboard() {
    val inputManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    inputManager?.let {
        if (currentFocus != null && currentFocus.windowToken != null && window != null) it.hideSoftInputFromWindow(currentFocus.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
    }
}

fun Activity.showSoftKeyboard() {
    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm?.let {
        if (currentFocus != null && window != null) it.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, 0)
    }
}

fun EditText.onConfirm(callback: () -> Unit) {
    setOnEditorActionListener { _, actionId, event ->
        if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                actionId == EditorInfo.IME_ACTION_DONE ||
                event?.action == KeyEvent.ACTION_DOWN &&
                        event?.keyCode == KeyEvent.KEYCODE_ENTER) {
            callback.invoke()
            true
        } else {
            false
        }
    }
}

fun String.toPlantPointList(): List<PlantPoint> {
    val pointList = split(" ")
    return pointList.map { it ->
        val pair = it.split(" ")

        PlantPoint(pair[0].toDouble(), pair[1].toDouble())
    }
}

fun List<PlantPoint>.indexOfNext(point: PlantPoint) : Int
{
    return indexOf(point) +1
}

fun PlantPoint.distanceFrom(other : PlantPoint) : Double
{
    return Math.sqrt((Math.pow((x - other.x), 2.0)) + (Math.pow((y - other.y), 2.0)))
}



fun List<PlantPoint>.toParsableString(): String {
    val sb = StringBuilder()
    forEach {
        sb.append(it.x.toString())
        sb.append(",")
        sb.append(it.y.toString())
        sb.append(" ")
    }
    return sb.toString()
}

class MathUti {
    companion object {

        fun periodDataInterpolation(p1: Double, p2: Double, tr1: Int, tr2: Int, tr: Int): Double {
            val first = Math.log(p1)
            val second = Math.log((p2 / p1))
            val third = Math.log(tr.toDouble() / tr1.toDouble())
            val fourth = Math.log(tr2.toDouble() / tr1.toDouble())
            val lnp = first + (second * third * (1 / fourth))
            return Math.pow(Math.E, lnp)
        }
    }
}
