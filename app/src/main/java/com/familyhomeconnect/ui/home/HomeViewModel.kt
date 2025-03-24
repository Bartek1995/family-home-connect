package com.familyhomeconnect.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.familyhomeconnect.model.House
import com.familyhomeconnect.repository.HomeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val homeRepository: HomeRepository
) : ViewModel() {

    // LiveData przechowujące informacje o domu
    val houseInfo: LiveData<House> = homeRepository.houseInfo
}
