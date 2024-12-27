package com.example.tabapplication.ui.image

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.tabapplication.R

class ImageViewModel : ViewModel() {
    private val _imageResId = MutableLiveData<Int>()
    val imageResId: LiveData<Int> get() = _imageResId

    fun setImage(resId: Int) {
        _imageResId.value = resId
    }
}