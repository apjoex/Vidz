package com.apjoex.vidz.adapters

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.support.v7.app.AlertDialog
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import com.apjoex.vidz.R
import com.apjoex.vidz.ui.PlaylistActivity
import com.apjoex.vidz.ui.PlaylistViewActivity
import com.apjoex.vidz.utils.PlaylistData
import com.apjoex.vidz.utils.Store

/**
 * Created by apjoe on 12/14/2017.
 */
class PlaylistAdapter(context: Context, playlists: MutableList<PlaylistData>) : RecyclerView.Adapter<PlaylistAdapter.ViewHolder>() {

    private val mContext = context
    private val mPlaylists = playlists
    private val layoutInflater = LayoutInflater.from(mContext)

    override fun getItemCount(): Int {
        return mPlaylists.size
    }

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {

        val playlist = mPlaylists.get(position)

        holder?.playlist_name?.text = playlist.name

        holder?.playlist_card?.setOnClickListener {
            Store.currentPlaylistPosition = position
            val intent = Intent(mContext, PlaylistViewActivity::class.java)
            mContext.startActivity(intent)
        }

        holder?.playlist_card?.setOnLongClickListener {
            val dialog = AlertDialog.Builder(mContext)
                    .setTitle("Delete playlist")
                    .setMessage("Are you sure want to delete this playlist?")
                    .setPositiveButton("YES", DialogInterface.OnClickListener { dialogInterface, i ->
                        dialogInterface.dismiss()
                        Store.playlists.remove(Store.playlists.get(position))
                        val activity = mContext as PlaylistActivity
                        activity.refresh()
                    })
                    .setNegativeButton("NO", DialogInterface.OnClickListener { dialogInterface, i ->
                        dialogInterface.dismiss()
                    })
                    .create()

            dialog.show()

            return@setOnLongClickListener true
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        val view = layoutInflater.inflate(R.layout.playlist_item, parent, false)
        return ViewHolder(view)
    }

    class ViewHolder (itemView : View) : RecyclerView.ViewHolder(itemView){
        val playlist_name = itemView.findViewById<TextView>(R.id.playlist_name)
        val playlist_card = itemView.findViewById<LinearLayout>(R.id.playlist_card)
    }

}