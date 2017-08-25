package com.polito.sismic.Presenters.ReportActivity

import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import com.polito.sismic.R
import kotlinx.android.synthetic.main.activity_note.*

//Custom activity to take a note
class NoteActivity : AppCompatActivity() {

    private lateinit var mUserName : String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note)

        mUserName = intent.getStringExtra("username")
        note_save.setOnClickListener { exitWithSuccess() }
    }

    private fun exitWithSuccess() {

        val intent = Intent()
        intent.putExtra("note", note.text.toString())
        setResult(Activity.RESULT_OK, intent)
        finish()
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == android.R.id.home) {

            val intent = Intent()
            intent.putExtra("username", mUserName)
            setResult(Activity.RESULT_CANCELED, intent)
            finish()
            return true
        }
        return false
    }
}
