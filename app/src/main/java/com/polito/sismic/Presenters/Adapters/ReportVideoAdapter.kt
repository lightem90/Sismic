package com.polito.sismic.Presenters.Adapters

import android.content.Context
import android.media.ThumbnailUtils
import android.net.Uri
import android.provider.MediaStore
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import com.polito.sismic.Domain.ReportMedia
import java.io.File


/**
 * Created by Matteo on 20/08/2017.
 */
class ReportVideoAdapter (val imageList : List<ReportMedia>,
                          val mContext: Context) : BaseAdapter()
{

    override fun getCount(): Int {
        return imageList.size
    }

    override fun getItem(position: Int): Any? {
        return imageList[position]
    }

    override fun getItemId(position: Int): Long {
        return imageList[position].id.toLong()
    }

    // create a new ImageView for each item referenced by the Adapter
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val imageView: ImageView
        if (convertView == null) {

            // if it's not recycled, initialize some attributes
            imageView = ImageView(mContext)
        }
        else {
            imageView = convertView as ImageView
        }

        //TODO, non credo funzioni
        val file = File(Uri.parse(imageList[position].url).path)
        val thumb = ThumbnailUtils.createVideoThumbnail(file.absolutePath,
                MediaStore.Images.Thumbnails.MINI_KIND)
        imageView.setImageBitmap(thumb)

        return imageView
    }
}