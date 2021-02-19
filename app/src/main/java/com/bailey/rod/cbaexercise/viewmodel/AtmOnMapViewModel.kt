package com.bailey.rod.cbaexercise.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class AtmOnMapViewModel : ViewModel() {
    val state : MutableLiveData<AtmOnMapViewModelState> = MutableLiveData()
}