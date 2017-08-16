package com.polito.sismic.Interactors.Helpers

import android.content.Context
import com.polito.sismic.Extensions.edit

/**
 * Created by Matteo on 14/08/2017.
 */

//TODO can be done better, in a more "kotlin" way
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
            var sp = getSharedPreferences("login", Context.MODE_PRIVATE)
            UserDetails(sp.getString("name", ""),
                    sp.getString("address", ""),
                    sp.getString("email", ""),
                    sp.getString("phone", ""),
                    sp.getString("qualification", ""),
                    sp.getString("registration", ""))
        }

        fun login(userDetails: UserDetails, context: Context) = with(context)
        {
            var sp = getSharedPreferences("login", Context.MODE_PRIVATE).edit()
            {
                putString("name", userDetails.name)
                putString("name", userDetails.address)
                putString("name", userDetails.email)
                putString("name", userDetails.phone)
                putString("name", userDetails.qualification)
                putString("name", userDetails.registration)
            }
        }

        fun demoLogin (context: Context)
        {
            login(UserDetails("demo", "demo", "demo", "demo", "demo", "demo"), context)
        }
    }
}

class UserDetails (val name : String,
                   val address : String,
                   val email : String,
                   val phone : String,
                   val qualification : String,
                   val registration : String)


