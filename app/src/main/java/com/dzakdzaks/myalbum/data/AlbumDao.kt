package com.dzakdzaks.myalbum.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

/**
 * ==================================//==================================
 * ==================================//==================================
 * Created on Monday, 16 November 2020 at 5:18 PM.
 * Project Name => MyAlbum
 * Package Name => com.dzakdzaks.myalbum.data
 * ==================================//==================================
 * ==================================//==================================
 */
@Dao
interface AlbumDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(albumEntity: AlbumEntity)

    @Delete
    suspend fun delete(albumEntity: AlbumEntity)

    @Query("SELECT * FROM album")
    fun getAllData(): Flow<List<AlbumEntity>>

}