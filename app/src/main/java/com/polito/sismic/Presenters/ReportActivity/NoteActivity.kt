package com.polito.sismic.Presenters.ReportActivity

import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.polito.sismic.R
import kotlinx.android.synthetic.main.activity_note.*

//Custom activity to take a note
class NoteActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note)

        note_save.setOnClickListener { exitWithSuccess() }
    }

    private fun exitWithSuccess() {

        val intent = Intent()
        intent.putExtra("note", note.text.toString())
        setResult(Activity.RESULT_OK, intent)
        finish()
    }
}
