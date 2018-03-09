package com.apjoex.vidz.utils

import com.apjoex.vidz.models.VideoData

/**
 * Created by apjoe on 12/14/2017.
 */
data class PlaylistData (
        val name : String,
        val videos :  MutableList<VideoData>
)
