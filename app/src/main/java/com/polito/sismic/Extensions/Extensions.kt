package com.polito.sismic.Extensions

import android.content.Context
import android.content.SharedPreferences
import android.widget.Toast
import com.polito.sismic.Domain.ReportDatabaseHelper

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

// Access property for Context
val Context.database: ReportDatabaseHelper
    get() = ReportDatabaseHelper.getInstance(applicationContext)