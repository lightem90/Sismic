package com.polito.sismic.Extensions

import android.content.Context
import android.widget.Toast

/**
 * Created by Matteo on 07/08/2017.
 */

//Context extensions
fun Context.toast(resourceId: Int) = toast(getString(resourceId))
fun Context.toast(message: CharSequence) =
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()