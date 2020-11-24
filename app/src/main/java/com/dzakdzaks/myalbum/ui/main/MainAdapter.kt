package com.dzakdzaks.myalbum.ui.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
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
) : RecyclerView.Adapter<MainViewHolder>() {

    private val listOfData: MutableList<AlbumEntity> = mutableListOf()

    fun addAllData(list: List<AlbumEntity>) {
        listOfData.clear()
        listOfData.addAll(list)
        notifyDataSetChanged()
    }

    fun addData(data: AlbumEntity) {
        val lastPos = listOfData.size
        listOfData.add(lastPos, data)
        notifyItemInserted(lastPos)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        val binding = ItemMainBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MainViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        val data = listOfData[position]

        holder.binding.apply {
            if (data.type == Constant.TYPE_VIDEO)
                iconPlay.visible()
            else
                iconPlay.gone()

            Glide.with(img.context)
                .load(data.url)
                .fitCenter()
                .centerCrop()
                .into(img)

            ViewCompat.setTransitionName(img, data.id.toString())

            img.setOnClickListener {
                onClickItem.invoke(data, img)
            }

            img.setOnLongClickListener {
                onLongClickItem.invoke(data)
                false
            }

        }


    }

    override fun getItemCount(): Int = listOfData.size

}


class MainViewHolder(val binding: ItemMainBinding) : RecyclerView.ViewHolder(binding.root)
