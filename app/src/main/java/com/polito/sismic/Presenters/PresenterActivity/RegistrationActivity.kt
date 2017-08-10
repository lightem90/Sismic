package com.polito.sismic.Presenters.PresenterActivity

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.polito.sismic.R
import kotlinx.android.synthetic.main.activity_registration.*

class RegistrationActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)

        //TODO check, registrazione server, conferma, chiude e torna al login
        btn_reg_confirm.setOnClickListener{ finish()}
    }
}
