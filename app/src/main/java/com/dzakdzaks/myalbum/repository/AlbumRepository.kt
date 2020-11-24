package com.dzakdzaks.myalbum.repository

import com.dzakdzaks.myalbum.data.AlbumDao
import com.dzakdzaks.myalbum.data.AlbumEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * ==================================//==================================
 * ==================================//==================================
 * Created on Monday, 16 November 2020 at 4:38 PM.
 * Project Name => MyAlbum
 * Package Name => com.dzakdzaks.myalbum.repository
 * ==================================//==================================
 * ==================================//==================================
 */
class AlbumRepository @Inject constructor(
    private val albumDao: AlbumDao
): AlbumRepositoryInterface {

    override suspend fun insert(albumEntity: AlbumEntity) {
        albumDao.insert(albumEntity)
    }

    override suspend fun delete(albumEntity: AlbumEntity) {
        albumDao.delete(albumEntity)
    }

    override fun getAllData(): Flow<List<AlbumEntity>> = albumDao.getAllData()

}