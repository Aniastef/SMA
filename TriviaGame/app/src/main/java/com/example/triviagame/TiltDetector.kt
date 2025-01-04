package com.example.triviagame
import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf

class TiltDetector(context: Context) {
    private val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    private val accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

    private val _tiltDirection = mutableStateOf<String?>(null)
    val tiltDirection: State<String?> = _tiltDirection

    private val sensorEventListener = object : SensorEventListener {
        override fun onSensorChanged(event: SensorEvent?) {
            event?.let {
                val x = it.values[0]
                _tiltDirection.value = when {
                    x > 5 -> "Left"
                    x < -5 -> "Right"
                    else -> null
                }
            }
        }

        override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
    }

    fun startListening() {
        sensorManager.registerListener(sensorEventListener, accelerometer, SensorManager.SENSOR_DELAY_UI)
    }

    fun stopListening() {
        sensorManager.unregisterListener(sensorEventListener)
    }
}
