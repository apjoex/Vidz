package com.apjoex.vidz.ui

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import com.apjoex.vidz.R
import com.apjoex.vidz.adapters.VideoListAdapter
import com.apjoex.vidz.models.VideoData
import com.apjoex.vidz.utils.Store
import kotlinx.android.synthetic.main.activity_playlist_view.*

class PlaylistViewActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_playlist_view)

        supportActionBar?.title = Store.playlists.get(Store.currentPlaylistPosition).name

        val playlistVideos = Store.playlists.get(Store.currentPlaylistPosition).videos

        val layoutManager = LinearLayoutManager(self@this)
        videosList.layoutManager = layoutManager
        val adapter = VideoListAdapter(self@this, playlistVideos)
        videosList.adapter = adapter
    }
}
