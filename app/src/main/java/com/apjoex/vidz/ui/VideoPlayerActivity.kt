package com.apjoex.vidz.ui

import android.content.Context
import android.media.AudioManager
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.WindowManager
import android.widget.SeekBar
import android.widget.Toast
import com.apjoex.vidz.R
import com.apjoex.vidz.models.VideoData
import com.apjoex.vidz.utils.Store
import kotlinx.android.synthetic.main.activity_video_player.*

class VideoPlayerActivity : AppCompatActivity() {

    val store = Store
    lateinit var currentVideo : VideoData
    var isPlaying : Boolean = false
    lateinit var audioManager : AudioManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_video_player)

        updatePlayBtn()

        showVideo()

        clickEvents()

        setVolume()
    }

    private fun setVolume() {
        audioManager = getSystemService(Context.AUDIO_SERVICE) as AudioManager;
        val maxVolume : Int = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        val currentVolume : Int = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);

        volume.setMax(maxVolume);
        volume.setProgress(currentVolume);

        volume.setOnSeekBarChangeListener(listener)

    }

    val listener = object : SeekBar.OnSeekBarChangeListener {
        override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, p1, 0)
        }

        override fun onStartTrackingTouch(p0: SeekBar?) {
        }

        override fun onStopTrackingTouch(p0: SeekBar?) {
        }

    }

    private fun clickEvents() {
        playBtn.setOnClickListener {
            isPlaying = !isPlaying
            updatePlayBtn()

            if(isPlaying){
                videoView.start()
            }else{
                videoView.pause()
            }
        }

        nextBtn.setOnClickListener {
            if (store.currentPosition != store.videos.size - 1){
                store.currentPosition ++
                showVideo()
            }else{
                Toast.makeText(self@this, "This is the last video", Toast.LENGTH_SHORT).show()
            }
        }

        previousBtn.setOnClickListener {
            if(store.currentPosition > 0){
                store.currentPosition --
                showVideo()
            }else{
                Toast.makeText(self@this, "This is the first video", Toast.LENGTH_SHORT).show()
            }
        }


    }

    private fun showVideo() {
        currentVideo = store.videos.get(store.currentPosition)
        playVideo(currentVideo)
    }

    private fun playVideo(currentVideo: VideoData) {
        val uri = Uri.parse(MediaStore.Video.Media.EXTERNAL_CONTENT_URI.toString() + "/" + currentVideo.id)

        if(videoView.isPlaying){
            videoView.stopPlayback()
        }

        videoView.setVideoURI(uri)
        videoView.start()
        isPlaying = true
        updatePlayBtn()


        videoView.setOnCompletionListener {
            if(store.currentPosition != store.videos.size - 1){
                store.currentPosition++
            }else{
                store.currentPosition = 0
            }
            showVideo()
        }

    }

    private fun updatePlayBtn() {
     if(isPlaying){
         playBtn.setImageResource(R.drawable.ic_pause)
     }else{
         playBtn.setImageResource(R.drawable.ic_play)
     }
    }
}
