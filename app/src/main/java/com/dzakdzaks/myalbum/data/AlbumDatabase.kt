package com.dzakdzaks.myalbum.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.dzakdzaks.myalbum.util.Constant

/**
 * ==================================//==================================
 * ==================================//==================================
 * Created on Monday, 16 November 2020 at 4:14 PM.
 * Project Name => MyAlbum
 * Package Name => com.dzakdzaks.myalbum.data
 * ==================================//==================================
 * ==================================//==================================
 */
@Database(
    entities = [AlbumEntity::class],
    version = Constant.DB_VERSION,
    exportSchema = false
)

abstract class AlbumDatabase : RoomDatabase() {

    abstract fun albumDao(): AlbumDao

}