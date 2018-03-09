package com.apjoex.vidz.utils

import com.apjoex.vidz.models.VideoData

/**
 * Created by apjoe on 12/14/2017.
 */
object Store {

    var videos : MutableList<VideoData> = mutableListOf()
    var currentPosition: Int = 0

    var playlists : MutableList<PlaylistData> = mutableListOf()
    var currentPlaylistPosition: Int = 0

}