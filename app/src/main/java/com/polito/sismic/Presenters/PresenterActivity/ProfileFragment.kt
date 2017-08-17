package com.polito.sismic.Presenters.PresenterActivity

import android.app.Fragment
import android.net.Uri
import android.os.Bundle
import android.view.*
import com.polito.sismic.Interactors.Helpers.LoginSharedPreferences
import com.polito.sismic.R
import kotlinx.android.synthetic.main.profile_fragment.*


/**
 * Created by Matteo on 27/07/2017.
 */
class ProfileFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        with (LoginSharedPreferences.getLoggedUser(activity))
        {
            et_name.text = name
            et_email.text = email
            et_address.text = address
            et_phone.text = phone
            et_qualification.text = qualification
            et_register.text = registration
            profile_img.setImageURI(Uri.parse(imageUri))
        }
        return inflater.inflate(R.layout.profile_fragment, container, false)
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
        return false;
    }

    private fun  logout() {
        LoginSharedPreferences.logout(activity)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.profile_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

}