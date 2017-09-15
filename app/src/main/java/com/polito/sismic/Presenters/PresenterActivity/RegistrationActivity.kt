package com.polito.sismic.Presenters.PresenterActivity

import android.app.Activity
import android.content.Intent
import android.os.AsyncTask
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.polito.sismic.Domain.UserDetails
import com.polito.sismic.Extensions.toast
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

        //TODO check, registrazione server, conferma, chiude e torna al login
        btn_reg_confirm.setOnClickListener{ register() }
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
                                                       private val mName : String,
                                                       private val mAddress : String,
                                                       private val mPhone : String,
                                                       private val mQualification : String,
                                                       private val mRegister : String,
                                                       private val mPassword: String,
                                                       private val caller : Activity) : AsyncTask<Void, Void, JSONObject?>() {

        private val SERVER_ADDR_lOGIN = "https://polito/sismic/register?"
        override fun doInBackground(vararg params: Void): JSONObject? {
            LoginSharedPreferences.demoLogin(applicationContext)
            try {
                val sb = StringBuilder(SERVER_ADDR_lOGIN)
                sb.append("username=")
                sb.append(mEmail)
                sb.append("&secret=")
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
                val status = conn.responseCode

                when (status) {
                    200, 201 -> {
                        val br = BufferedReader(InputStreamReader(conn.inputStream))
                        val sb2 = StringBuilder()
                        var line: String? = null
                        while ({ line = br.readLine(); line }() != null) {
                            sb2.append(line + "\n")
                        }
                        br.close()
                        return JSONObject(sb2.toString())
                    }
                }
            } catch (e: InterruptedException) {
                return null
            }
            return null
        }

        override fun onPostExecute(success: JSONObject?) {

            success?.let {
                val results = it.getJSONObject("result").getJSONArray("result_data")
                if (results[0].toString().equals("success")) {
                    LoginSharedPreferences.login(UserDetails(
                            mName,
                            mAddress,
                            mEmail,
                            mPhone,
                            mQualification,
                            mRegister,
                            ""), caller)
                    caller.startActivity(Intent(caller, PresenterActivity::class.java))
                    finish()
                } else {
                    caller.toast(R.string.registration_error)
                }
            }
        }

        override fun onCancelled() {
        }
    }
}
