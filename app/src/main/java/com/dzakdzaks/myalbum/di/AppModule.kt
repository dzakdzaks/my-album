package com.dzakdzaks.myalbum.di

import android.content.Context
import androidx.room.Room
import com.dzakdzaks.myalbum.data.AlbumDao
import com.dzakdzaks.myalbum.data.AlbumDatabase
import com.dzakdzaks.myalbum.repository.AlbumRepository
import com.dzakdzaks.myalbum.util.Constant
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

/**
 * ==================================//==================================
 * ==================================//==================================
 * Created on Monday, 16 November 2020 at 4:26 PM.
 * Project Name => MyAlbum
 * Package Name => com.dzakdzaks.myalbum.di
 * ==================================//==================================
 * ==================================//==================================
 */

@Module
@InstallIn(ApplicationComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideAlbumDatabase(
        @ApplicationContext context: Context
    ) = Room.databaseBuilder(
        context,
        AlbumDatabase::class.java,
        Constant.DB_NAME
    )
        .build()

    @Singleton
    @Provides
    fun provideShoppingDao(
        albumDatabase: AlbumDatabase
    ) = albumDatabase.albumDao()

    @Singleton
    @Provides
    fun provideAlbumRepository(
        dao: AlbumDao
    ) = AlbumRepository(dao)

}