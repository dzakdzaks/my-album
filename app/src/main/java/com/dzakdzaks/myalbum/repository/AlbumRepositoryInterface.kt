package com.dzakdzaks.myalbum.repository

import com.dzakdzaks.myalbum.data.AlbumEntity
import kotlinx.coroutines.flow.Flow

/**
 * ==================================//==================================
 * ==================================//==================================
 * Created on Monday, 16 November 2020 at 4:38 PM.
 * Project Name => MyAlbum
 * Package Name => com.dzakdzaks.myalbum.repository
 * ==================================//==================================
 * ==================================//==================================
 */

interface AlbumRepositoryInterface {

    suspend fun insert(albumEntity: AlbumEntity)

    suspend fun delete(albumEntity: AlbumEntity)

    fun getAllData(): Flow<List<AlbumEntity>>

}