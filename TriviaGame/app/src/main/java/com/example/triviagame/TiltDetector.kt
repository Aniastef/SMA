package com.example.triviagame
import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.compose.runtime.mutableStateOf

class TiltDetector(context: Context) : SensorEventListener
{
    private val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    private val accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
    val tiltDirection = mutableStateOf("")

    fun startListening() {
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_UI)
    }

    fun stopListening() {
        sensorManager.unregisterListener(this)
    }

    override fun onSensorChanged(event: SensorEvent?) {
        event?.let {
            val x = event.values[0]
            val y = event.values[1]

            tiltDirection.value = when {
                x > 5 -> "Left"
                x < -5 -> "Right"
                y > 5 -> "Up"
                y < -5 -> "Down"
                else -> "Center"
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
}