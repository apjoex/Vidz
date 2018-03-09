package com.apjoex.vidz.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.apjoex.vidz.R
import com.apjoex.vidz.models.VideoData
import android.provider.MediaStore
import android.R.attr.path
import android.content.Intent
import android.media.ThumbnailUtils
import android.graphics.Bitmap
import android.net.Uri
import android.support.v7.widget.CardView
import android.util.Log
import android.widget.ImageView
import android.widget.LinearLayout
import com.apjoex.vidz.ui.VideoPlayerActivity
import com.apjoex.vidz.utils.Store


/**
 * Created by apjoe on 12/14/2017.
 */
class VideoListAdapter(context: Context, videos: MutableList<VideoData>) : RecyclerView.Adapter<VideoListAdapter.ViewHolder>() {

    private var mContext : Context = context
    private val mVideos = videos
    private val layoutInflater : LayoutInflater = LayoutInflater.from(mContext)


    override fun getItemCount(): Int {
        return mVideos.size
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        val view = layoutInflater.inflate(R.layout.video_item, parent, false)
        return ViewHolder(view)

    }

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {

        val video = mVideos.get(position)
        holder?.title?.text = video.title
        holder?.duration?.text = showDuration(video.duration)
        val thumb = ThumbnailUtils.createVideoThumbnail(video.path, MediaStore.Images.Thumbnails.FULL_SCREEN_KIND)
        holder?.thumbnail?.setImageBitmap(thumb)
        holder?.card?.setOnClickListener {
            Store.videos.clear()
            Store.videos.addAll(mVideos)
            Store.currentPosition = position
            showVideo()
        }
    }

    private fun showVideo() {
        val intent = Intent(mContext, VideoPlayerActivity::class.java)
        mContext.startActivity(intent)
    }

    private fun showDuration(duration: Int): String {

        val min: Int
        val sec: Int

        val time = duration / 1000
        min = time / 60
        sec = time % 60

        var minPart = Integer.toString(min)
        if (min < 10)
            minPart = "0" + Integer.toString(min)
        var secPart = Integer.toString(sec)
        if (sec < 10)
            secPart = "0" + Integer.toString(sec)
        return minPart + ":" + secPart
    }


    class ViewHolder(item : View) : RecyclerView.ViewHolder(item){
        val thumbnail = item.findViewById<ImageView>(R.id.video_thumbnail)
        val title = item.findViewById<TextView>(R.id.video_title)
        val duration = item.findViewById<TextView>(R.id.video_duration)
        val card = item.findViewById<LinearLayout>(R.id.card)
    }
}