package com.polito.sismic.Presenters.Adapters

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import com.polito.sismic.Domain.ReportMedia
import com.polito.sismic.Extensions.toUri
import com.polito.sismic.GlideApp


/**
 * Created by Matteo on 20/08/2017.
 */
class ReportVideoAdapter (private val videoList: List<ReportMedia>,
                          private val mContext: Context) : BaseAdapter()
{

    override fun getCount(): Int {
        return videoList.size
    }

    override fun getItem(position: Int): Any? {
        return videoList[position]
    }

    override fun getItemId(position: Int): Long {
        return videoList[position].id.toLong()
    }

    // create a new ImageView for each item referenced by the Adapter
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        val imageView: ImageView = if (convertView == null) {

            // if it's not recycled, initialize some attributes
            ImageView(mContext)
        }
        else {
            convertView as ImageView
        }

        GlideApp.with(mContext)
                .asBitmap()
                .load(videoList[position].uri.toUri())
                .override(750, 750)
                .into(imageView)

        return imageView
    }
}