package com.dzakdzaks.myalbum.data

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

/**
 * ==================================//==================================
 * ==================================//==================================
 * Created on Monday, 16 November 2020 at 4:14 PM.
 * Project Name => MyAlbum
 * Package Name => com.dzakdzaks.myalbum.data
 * ==================================//==================================
 * ==================================//==================================
 */

@Parcelize
@Entity(tableName = "album")
data class AlbumEntity(
    var url: String,
    var type: String,
    var createdDate: String,
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null
): Parcelable