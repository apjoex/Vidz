package com.apjoex.vidz.ui

import android.content.DialogInterface
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.apjoex.vidz.R
import com.apjoex.vidz.adapters.PlaylistAdapter
import com.apjoex.vidz.models.VideoData
import com.apjoex.vidz.utils.PlaylistData
import com.apjoex.vidz.utils.Store
import kotlinx.android.synthetic.main.activity_playlist.*
import java.util.*

class PlaylistActivity : AppCompatActivity() {

    private lateinit var dialog : AlertDialog
    private lateinit var nameView : View
    private lateinit var playlistName : EditText

    private val videos = mutableListOf<VideoData>()
    private val playlistVideos = mutableListOf<VideoData>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_playlist)

        nameView = LayoutInflater.from(self@this).inflate(R.layout.create_playlist, null, false)
        playlistName = nameView.findViewById(R.id.name)

        dialog = AlertDialog.Builder(self@this)
                .setTitle("Create new playlist")
                .setView(nameView)
                .setPositiveButton("PROCEED", { dialogInterface, _ ->
                    dialogInterface.dismiss()
                    proceedToChooseVideos()
                })
                .setNegativeButton("CANCEL", { dialogInterface, _ ->
                    dialogInterface.dismiss()
                    playlistName.text.clear()
                })
                .create()

        stepper.maxNumber = 10

        showPlaylists()
    }

    private fun proceedToChooseVideos() {
        if(!playlistName.text.toString().isEmpty()){

            val name = playlistName.text.toString()

            val resolver = contentResolver
            val projection = arrayOf(MediaStore.Video.Media._ID, MediaStore.Video.Media.DISPLAY_NAME, MediaStore.Video.Media.DURATION, MediaStore.Video.Media.DATA)
            val cursor = resolver.query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, projection, null, null, null)

            val items : Array<CharSequence> = Array(cursor.count,{""})
            val checkedItems = null

            while(cursor.moveToNext())
            {
                val id = cursor.getInt(0)
                val title = cursor.getString(1)
                val dur = cursor.getInt(2)
                val path = cursor.getString(3)
                videos.add(VideoData(id, title, dur, path))

                items[cursor.position] = title+"\n\n"
            }
            cursor.close()

            val videosDialog = AlertDialog.Builder(self@this)
                    .setTitle("Choose videos")
                    .setMultiChoiceItems(items,checkedItems,listener)
                    .setPositiveButton("CREATE", { dialogInterface, _ ->
                        dialogInterface.dismiss()
                        if(playlistVideos.size > 0){
                            Store.playlists.add(PlaylistData(name, playlistVideos))
                            playlistName.text.clear()
                            showPlaylists()
                        }else{
                            Toast.makeText(self@this, "No video was selected", Toast.LENGTH_SHORT).show()
                        }
                    })
                    .setNegativeButton("CANCEL", { dialogInterface, _ ->
                        dialogInterface.dismiss()
                    })
                    .create()

            playlistVideos.clear()
            videosDialog.show()

        }
    }

    private fun showPlaylists() {
        if(Store.playlists.size > 0){
            no_playlist.visibility = View.GONE

            val layoutManager = LinearLayoutManager(self@this)
            playlists.layoutManager = layoutManager

            Collections.reverse(Store.playlists)
            val adapter = PlaylistAdapter(self@this, Store.playlists)
            playlists.adapter = adapter

        }else{
            no_playlist.visibility = View.VISIBLE
        }

    }

    private var listener = DialogInterface.OnMultiChoiceClickListener { _, position, checked ->
        if(checked){
            playlistVideos.add(videos[position])
        }else{
            playlistVideos.remove(videos[position])
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.playlist, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId){
            R.id.action_add -> {
                createPlaylist()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun createPlaylist() {
        dialog.show()
    }

    fun refresh() {
        showPlaylists()
    }

}


