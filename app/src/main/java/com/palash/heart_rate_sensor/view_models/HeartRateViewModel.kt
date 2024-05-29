package com.palash.heart_rate_sensor.view_models

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.palash.heart_rate_sensor.repositories.HeartRateRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HeartRateViewModel @Inject constructor(private val heartRateRepository: HeartRateRepository) : ViewModel() {

    val heartRateData: LiveData<Float?> = heartRateRepository.heartRateData

    fun startHeartRateUpdates() {
        viewModelScope.launch {
            heartRateRepository.startHeartRateUpdates()
        }
    }

    fun stopHeartRateUpdates() {
        viewModelScope.launch {
            heartRateRepository.stopHeartRateUpdates()
        }
    }
}