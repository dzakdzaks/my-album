package com.dzakdzaks.myalbum.ui.camerax

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import id.dipay.camerax.Selector

class CameraXViewModel : ViewModel() {

    private val _cameraSelector = MutableLiveData(Selector.BACK)
    val cameraSelector: LiveData<Selector>
        get() = _cameraSelector

    fun setCameraSelector(value: Selector) {
        _cameraSelector.value = value
    }

}