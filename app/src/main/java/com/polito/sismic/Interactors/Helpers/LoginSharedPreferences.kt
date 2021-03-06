package com.polito.sismic.Interactors.Helpers

import android.content.Context
import com.polito.sismic.Domain.UserDetails
import com.polito.sismic.Extensions.edit

/**
 * Created by Matteo on 14/08/2017.
 */

class LoginSharedPreferences {

    companion object {

        fun logout(context: Context) = with(context)
        {
            getSharedPreferences("login", Context.MODE_PRIVATE).edit {
                putBoolean("logged", false)
            }
        }

        fun isLoggedIn (context: Context) : Boolean = with(context)
        {
            getSharedPreferences("login", Context.MODE_PRIVATE).getBoolean("logged", false)
        }

        fun getLoggedUser(context: Context) : UserDetails = with(context)
        {
            val sp = getSharedPreferences("login", Context.MODE_PRIVATE)
            UserDetails(sp.getString("name", ""),
                    sp.getString("address", ""),
                    sp.getString("email", ""),
                    sp.getString("phone", ""),
                    sp.getString("qualification", ""),
                    sp.getString("registration", ""))
        }

        fun login(userDetails: UserDetails, context: Context) = with(context)
        {
            getSharedPreferences("login", Context.MODE_PRIVATE).edit()
            {
                putString("name", userDetails.name)
                putString("address", userDetails.address)
                putString("email", userDetails.email)
                putString("phone", userDetails.phone)
                putString("qualification", userDetails.qualification)
                putString("registration", userDetails.registration)
                putBoolean("logged", true)
            }
        }

        fun demoLogin (context: Context)
        {
            login(UserDetails("Matteo", "demo", "demo", "demo", "demo", "demo"), context)
        }
    }
}


