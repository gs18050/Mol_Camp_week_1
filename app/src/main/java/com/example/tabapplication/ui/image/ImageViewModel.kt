package com.example.tabapplication.ui.image

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ImageViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "Image Information"
    }
    val text: LiveData<String> = _text
}