package com.dzakdzaks.myalbum.ui.main

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.dzakdzaks.myalbum.data.AlbumEntity
import com.dzakdzaks.myalbum.repository.AlbumRepository
import com.dzakdzaks.myalbum.util.ext.toStringFormat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import java.util.*

/**
 * ==================================//==================================
 * ==================================//==================================
 * Created on Monday, 16 November 2020 at 4:55 PM.
 * Project Name => MyAlbum
 * Package Name => com.dzakdzaks.myalbum.ui
 * ==================================//==================================
 * ==================================//==================================
 */
class MainViewModel @ViewModelInject constructor(
        private val repository: AlbumRepository
): ViewModel() {

    fun insert(imagePath: String, type: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val albumEntity = AlbumEntity(imagePath, type, Calendar.getInstance().time.toStringFormat())
            repository.insert(albumEntity)
        }
    }

    fun delete(albumEntity: AlbumEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.delete(albumEntity)
        }
    }

    fun getAllData() = repository.getAllData().flowOn(Dispatchers.IO) .asLiveData()

}