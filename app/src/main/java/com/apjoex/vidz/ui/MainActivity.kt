package com.apjoex.vidz.ui

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.MediaStore
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.apjoex.vidz.R
import com.apjoex.vidz.adapters.VideoListAdapter
import com.apjoex.vidz.models.VideoData
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity() {

    private val REQUEST_PERMISSION = 10
    private var localVideos = mutableListOf<VideoData>()
    private lateinit var adapter : VideoListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val permissions = arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE)

        //Get videos
        if(ActivityCompat.checkSelfPermission(self@this, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(self@this,permissions,REQUEST_PERMISSION)
        }else{
            getVideos()
        }
    }

    private fun getVideos() {

        val resolver = contentResolver
        val projection = arrayOf(MediaStore.Video.Media._ID, MediaStore.Video.Media.DISPLAY_NAME, MediaStore.Video.Media.DURATION, MediaStore.Video.Media.DATA)
        val cursor = resolver.query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, projection, null, null, null)

        while(cursor.moveToNext())
        {
            val id = cursor.getInt(0)
            val title = cursor.getString(1)
            val dur = cursor.getInt(2)
            val path = cursor.getString(3)
            localVideos.add(VideoData(id, title, dur, path))
        }

        cursor.close()

        displayVideos()
    }

    private fun displayVideos() {
        val layoutManager = LinearLayoutManager(self@this)
        videos_list.layoutManager = layoutManager
        Collections.reverse(localVideos)
        adapter = VideoListAdapter(self@this, localVideos)
        videos_list.adapter = adapter
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if(requestCode == REQUEST_PERMISSION){
            if(grantResults.isNotEmpty() && grantResults[0] == (PackageManager.PERMISSION_GRANTED)){
                getVideos()
            }else{
                Toast.makeText(self@this, "Please grant permission to view videos", Toast.LENGTH_SHORT).show()
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.home, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId){
            R.id.action_playlists -> {
                val intent = Intent(self@this, PlaylistActivity::class.java)
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
