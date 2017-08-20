package com.polito.sismic.Extensions

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.widget.Toast
import com.polito.sismic.Domain.Report
import com.polito.sismic.Interactors.ReportExtraInfo
import com.polito.sismic.Presenters.ReportActivity.Fragments.FragmentState
import org.jetbrains.anko.db.MapRowParser
import org.jetbrains.anko.db.SelectQueryBuilder
import java.text.SimpleDateFormat
import java.util.*
import kotlin.reflect.KProperty

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

fun String.toFormattedDate() : Date
{
    return SimpleDateFormat("yyyy-MM-dd HH:mm:ss ").parse(this)
}

fun Date.toFormattedString() : String
{
    return SimpleDateFormat("yyyy-MM-dd HH:mm:ss ").format(this)
}

fun <K, V : Any> Map<K, V?>.toVarargArray(): Array<out Pair<K, V>> =
        map({ Pair(it.key, it.value!!) }).toTypedArray()

fun Bundle.putFragmentState(state : FragmentState)
{
    putParcelable("fragment_state", state)
}

fun Bundle.getFragmentState() : FragmentState?
{
    return getParcelable<FragmentState>("fragment_state")
}

fun Bundle.putReportExtraInfo(extraInfo: ReportExtraInfo)
{
    extraInfo.extraValues.forEach { putCharArray(it.first, it.second.toString().toCharArray()) }
}

fun Bundle.getReportExtraInfo(keyList : List<String>) : ReportExtraInfo
{
    val listOfValues = mutableListOf<Pair<String, Any?>>()
    keyList.forEach{
        listOfValues.add(it to String(getCharArray(it)))
    }
    return ReportExtraInfo(listOfValues)
}