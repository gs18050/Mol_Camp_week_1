package com.example.tabapplication.ui.contact

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ContactViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "Contact Information"
    }
    val text: LiveData<String> = _text
}