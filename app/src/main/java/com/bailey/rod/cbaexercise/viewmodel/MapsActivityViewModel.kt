package com.bailey.rod.cbaexercise.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MapsActivityViewModel : ViewModel() {
    val state : MutableLiveData<MapsActivityViewModelState> = MutableLiveData()
}