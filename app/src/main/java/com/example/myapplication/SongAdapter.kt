package com.example.myapplication

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.databinding.FragmentDiscoverBinding
import com.example.myapplication.databinding.ItemListBinding

// SongAdapter.kt
class SongAdapter(
    private val songs: List<Song>,
    private val onSongClick: (Song) -> Unit
) : RecyclerView.Adapter<SongAdapter.SongViewHolder>() {

    class SongViewHolder(val binding: ItemListBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongViewHolder {
        val binding = ItemListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SongViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SongViewHolder, position: Int) {
        val song = songs[position]
        holder.binding.apply {
            tvTitle.text = song.title
            tvSubtitle.text = song.artist
            // 如果你有封面 ImageView，可以这样设置：
            // imgCover.setImageBitmap(song.cover ?: defaultLogo)

            root.setOnClickListener { onSongClick(song) }
        }
    }

    override fun getItemCount() = songs.size
}

