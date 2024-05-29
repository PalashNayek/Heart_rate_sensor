package com.palash.heart_rate_sensor.repositories

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import javax.inject.Inject

class HeartRateRepository @Inject constructor(
    private val sensorManager: SensorManager
) : SensorEventListener {

    private val _heartRateData = MutableLiveData<Float?>()
    val heartRateData: LiveData<Float?> = _heartRateData

    private var heartRateSensor: Sensor? = sensorManager.getDefaultSensor(Sensor.TYPE_HEART_RATE)


    fun startHeartRateUpdates() {
        heartRateSensor?.also { sensor ->
            sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL)
        }
        if (heartRateSensor == null) {
            _heartRateData.postValue(null)
        }
    }

    fun stopHeartRateUpdates() {
        sensorManager.unregisterListener(this)
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event?.sensor?.type == Sensor.TYPE_HEART_RATE) {
            _heartRateData.postValue(event.values[0])
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        // Handle sensor accuracy changes if needed
    }
}