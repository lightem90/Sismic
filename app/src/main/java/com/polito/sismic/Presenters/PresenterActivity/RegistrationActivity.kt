package com.polito.sismic.Presenters.PresenterActivity

import android.app.Activity
import android.content.Intent
import android.os.AsyncTask
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.polito.sismic.Domain.UserDetails
import com.polito.sismic.Extensions.toast
import com.polito.sismic.Interactors.Helpers.AESCryptHelper
import com.polito.sismic.Interactors.Helpers.LoginSharedPreferences
import com.polito.sismic.R
import kotlinx.android.synthetic.main.activity_registration.*
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class RegistrationActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)

        btn_reg_confirm.setOnClickListener { register() }
    }

    private fun register() {
        UserRegisterClass(et_email.text.toString(),
                et_name.text.toString(),
                et_address.text.toString(),
                et_phone.text.toString(),
                et_qualification.text.toString(),
                et_register.text.toString(),
                et_password.text.toString(),
                this).execute()
    }

    inner class UserRegisterClass internal constructor(private val mEmail: String,
                                                       private val mName: String,
                                                       private val mAddress: String,
                                                       private val mPhone: String,
                                                       private val mQualification: String,
                                                       private val mRegister: String,
                                                       private val mPassword: String,
                                                       private val caller: Activity) : AsyncTask<Void, Void, Int>() {

        private val SERVER_ADDR_lOGIN = "http://192.168.0.11:5000/sismic/registration_form?"
        override fun doInBackground(vararg params: Void): Int {
            LoginSharedPreferences.demoLogin(applicationContext)
            try {
                val sb = StringBuilder(SERVER_ADDR_lOGIN)
                sb.append("email=")
                sb.append(mEmail)
                sb.append("&secret=")
                sb.append(AESCryptHelper.encrypt(mPassword))
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
                    LoginSharedPreferences.login(UserDetails(
                            mName,
                            mAddress,
                            mEmail,
                            mPhone,
                            mQualification,
                            mRegister), caller)
                    finish()
                }
                else -> caller.toast(String.format(caller.getString(R.string.registration_error), returnCode))
            }
        }
    }
}
