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
class ReportVideoAdapter (private val imageList : List<ReportMedia>,
                          private val mContext: Context) : BaseAdapter()
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
        val path = getPath(Uri.parse(imageList[position].url))
        val thumb = ThumbnailUtils.createVideoThumbnail(path,
                MediaStore.Images.Thumbnails.MINI_KIND)
        imageView.setImageBitmap(thumb)

        return imageView
    }

    private fun getPath(uri: Uri): String {
        val projection = arrayOf(MediaStore.Images.Media.DATA)
        val cursor = mContext.contentResolver.query(uri, projection, null, null, null)
        val column_index = cursor
                .getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        cursor.moveToFirst()
        val toReturn = cursor.getString(column_index)
        cursor.close()
        return toReturn
    }
}