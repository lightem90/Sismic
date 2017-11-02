package com.polito.sismic.Presenters.PresenterActivity

import android.app.Activity
import android.os.AsyncTask
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.view.View
import com.polito.sismic.Extensions.toast
import com.polito.sismic.Interactors.Helpers.LoginSharedPreferences
import com.polito.sismic.R
import kotlinx.android.synthetic.main.activity_registration.*
import java.net.HttpURLConnection
import java.net.URL

class RegistrationActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)

        btn_reg_confirm.setOnClickListener { register() }
    }

    private fun register() {

        // Reset errors.
        et_email.error = null
        et_password.error = null

        // Store values at the time of the login attempt.
        val emailStr = et_email.text.toString()
        val passwordStr = et_password.text.toString()

        var cancel = false
        var focusView: View? = null

        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(passwordStr) || !isPasswordValid(passwordStr)) {
            et_password.error = getString(R.string.error_invalid_password)
            focusView = et_password
            cancel = true
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(emailStr)) {
            et_email.error = getString(R.string.error_field_required)
            focusView = et_email
            cancel = true
        } else if (!isEmailValid(emailStr)) {
            et_email.error = getString(R.string.error_invalid_email)
            focusView = et_email
            cancel = true
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView?.requestFocus()
        } else {
            UserRegisterClass(et_email.text.toString(),
                    et_name.text.toString(),
                    et_address.text.toString(),
                    et_phone.text.toString(),
                    et_qualification.text.toString(),
                    et_register.text.toString(),
                    et_password.text.toString(),
                    this).execute()
        }
    }

    private fun isEmailValid(email: String): Boolean {
        return email.contains("@")
    }

    private fun isPasswordValid(password: String): Boolean {
        return password.length > 4
    }

    inner class UserRegisterClass internal constructor(private val mEmail: String,
                                                       private val mName: String,
                                                       private val mAddress: String,
                                                       private val mPhone: String,
                                                       private val mQualification: String,
                                                       private val mRegister: String,
                                                       private val mPassword: String,
                                                       private val caller: Activity) : AsyncTask<Void, Void, Int>() {

        private val SERVER_ADDR_lOGIN = "http://natasha.polito.it/seismic/registration_form?"
        override fun doInBackground(vararg params: Void): Int {
            LoginSharedPreferences.demoLogin(applicationContext)
            try {
                val sb = StringBuilder(SERVER_ADDR_lOGIN)
                sb.append("email=")
                sb.append(mEmail)
                sb.append("&secret=")
                //AESCryptHelper.encrypt(mPassword) in plain (for now)
                sb.append(mPassword)
                sb.append("&name=")
                sb.append(mName)
                sb.append("&address=")
                sb.append(mAddress)
                sb.append("&phone=")
                sb.append(mPhone)
                sb.append("&qualification=")
                sb.append(mQualification)
                sb.append("&register=")
                sb.append(mRegister)

                val urlUse = URL(sb.toString())
                val conn: HttpURLConnection?
                conn = urlUse.openConnection() as HttpURLConnection
                conn.requestMethod = "POST"
                conn.connectTimeout = 5000
                return conn.responseCode

            } catch (e: InterruptedException) {
                return 400
            }
        }

        override fun onPostExecute(returnCode: Int) {

            when (returnCode) {
                200, 201 -> {
                    //LoginSharedPreferences.login(UserDetails(
                    //        mName,
                    //        mAddress,
                    //        mEmail,
                    //        mPhone,
                    //        mQualification,
                    //        mRegister), caller)
                    caller.toast(R.string.success_registration)
                    finish()
                }
                else -> caller.toast(String.format(caller.getString(R.string.registration_error), returnCode))
            }
        }
    }
}
