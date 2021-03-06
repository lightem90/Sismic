package com.polito.sismic.Presenters.Adapters

import android.content.Context
import android.net.Uri
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.polito.sismic.Domain.ReportMedia
import com.polito.sismic.Extensions.getFileName
import com.polito.sismic.Interactors.Helpers.MediaType


/**
 * Created by Matteo on 20/08/2017.
 */
class ReportStringAdapter (private val fileList: List<ReportMedia>,
                           private val mContext: Context) : BaseAdapter()
{

    override fun getCount(): Int {
        return fileList.size
    }

    override fun getItem(position: Int): Any? {
        return fileList[position]
    }

    override fun getItemId(position: Int): Long {
        return fileList[position].id.toLong()
    }

    // create a new ImageView for each item referenced by the Adapter
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val textView: TextView
        if (convertView == null) {

            // if it's not recycled, initialize some attributes
            textView = TextView(mContext)
        }
        else {
            textView = convertView as TextView
        }

        textView.text =
        if (fileList[position].type == MediaType.Audio.toString())
            Uri.parse(fileList[position].uri).getFileName(mContext)
        else
            fileList[position].note

        return textView
    }
}
