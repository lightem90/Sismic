package com.polito.sismic.Presenters.PresenterActivity

import android.app.Fragment
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.*
import com.polito.sismic.Interactors.Helpers.LoginSharedPreferences
import com.polito.sismic.R
import kotlinx.android.synthetic.main.profile_fragment.*
import kotlinx.android.synthetic.main.profile_fragment.view.*
import org.jetbrains.anko.startActivity


/**
 * Created by Matteo on 27/07/2017.
 */
class ProfileFragment : Fragment() {


    fun getFragmentTag() : String
    {
        return "profile"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v =  inflater.inflate(R.layout.profile_fragment, container, false)
        with (LoginSharedPreferences.getLoggedUser(activity))
        {
            v.et_name.text = name
            v.et_email.text = email
            v.et_address.text = address
            v.et_phone.text = phone
            v.et_qualification.text = qualification
            v.et_register.text = registration
            v.profile_img.setImageURI(Uri.parse(imageUri))
        }
        return v
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item != null) {
            when (item.itemId)
            {
                R.id.logout ->
                {
                    logout()
                    return true
                }
            }
        }
        return false
    }

    private fun logout() = with (activity){
        LoginSharedPreferences.logout(this)
        startActivity(Intent(this, LoginActivity::class.java))
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.profile_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

}