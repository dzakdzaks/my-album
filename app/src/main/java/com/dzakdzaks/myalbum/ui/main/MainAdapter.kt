package com.dzakdzaks.myalbum.ui.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dzakdzaks.myalbum.data.AlbumEntity
import com.dzakdzaks.myalbum.databinding.ItemMainBinding
import com.dzakdzaks.myalbum.util.Constant
import com.dzakdzaks.myalbum.util.ext.gone
import com.dzakdzaks.myalbum.util.ext.visible

/**
 * ==================================//==================================
 * ==================================//==================================
 * Created on Monday, 16 November 2020 at 6:18 PM.
 * Project Name => MyAlbum
 * Package Name => com.dzakdzaks.myalbum.ui
 * ==================================//==================================
 * ==================================//==================================
 */

class MainAdapter(
    private val onLongClickItem: (AlbumEntity) -> Unit = {},
    private val onClickItem: (AlbumEntity, View) -> Unit = { _, _ -> }
) : ListAdapter<AlbumEntity, MainAdapter.MainViewHolder>(DiffCallback()) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        val binding = ItemMainBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MainViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.bind(currentItem)
    }

    inner class MainViewHolder(private val binding: ItemMainBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.apply {
                img.setOnClickListener {
                    if (adapterPosition != RecyclerView.NO_POSITION)
                        onClickItem.invoke(getItem(adapterPosition), img)
                }

                img.setOnLongClickListener {
                    if (adapterPosition != RecyclerView.NO_POSITION)
                        onLongClickItem.invoke(getItem(adapterPosition))
                    false
                }
            }
        }

        fun bind(albumEntity: AlbumEntity) {
            binding.apply {
                if (albumEntity.type == Constant.TYPE_VIDEO)
                    iconPlay.visible()
                else
                    iconPlay.gone()

                Glide.with(img.context)
                    .load(albumEntity.url)
                    .fitCenter()
                    .centerCrop()
                    .into(img)

                ViewCompat.setTransitionName(img, albumEntity.id.toString())
            }

        }
    }

    class DiffCallback : DiffUtil.ItemCallback<AlbumEntity>() {
        override fun areItemsTheSame(oldItem: AlbumEntity, newItem: AlbumEntity) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: AlbumEntity, newItem: AlbumEntity) =
            oldItem == newItem
    }
}