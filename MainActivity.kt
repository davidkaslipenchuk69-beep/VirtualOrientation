package com.example.virtualorientation

import android.app.Activity
import android.os.Bundle
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.widget.*
import kotlin.math.atan2
import kotlin.math.sqrt

class MainActivity : Activity(), SensorEventListener {
    private lateinit var sm: SensorManager
    private var accel: Sensor? = null
    private lateinit var pitchText: TextView
    private lateinit var rollText: TextView
    private lateinit var rawText: TextView
    private var pitchZero = 0.0
    private var rollZero = 0.0
    private var pitch = 0.0
    private var roll = 0.0

    override fun onCreate(state: Bundle?) {
        super.onCreate(state)
        sm = getSystemService(SENSOR_SERVICE) as SensorManager
        accel = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

        val box = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(32, 48, 32, 32)
        }

        val title = TextView(this).apply {
            text = "Virtual Orientation"
            textSize = 28f
        }
        pitchText = TextView(this).apply { textSize = 24f }
        rollText = TextView(this).apply { textSize = 24f }
        rawText = TextView(this).apply { textSize = 16f }

        val calibrate = Button(this).apply {
            text = "Калибровать (держи телефон ровно)"
            setOnClickListener {
                pitchZero = pitch
                rollZero = roll
            }
        }

        val note = TextView(this).apply {
            text = "\nИсточник: акселерометр\nPitch/Roll доступны. Yaw без гироскопа/магнитометра надёжно получить нельзя."
            textSize = 16f
        }

        box.addView(title)
        box.addView(pitchText)
        box.addView(rollText)
        box.addView(rawText)
        box.addView(calibrate)
        box.addView(note)
        setContentView(box)
    }

    override fun onResume() {
        super.onResume()
        accel?.let { sm.registerListener(this, it, SensorManager.SENSOR_DELAY_GAME) }
    }

    override fun onPause() {
        sm.unregisterListener(this)
        super.onPause()
    }

    override fun onSensorChanged(e: SensorEvent) {
        if (e.sensor.type != Sensor.TYPE_ACCELEROMETER) return
        val x = e.values[0].toDouble()
        val y = e.values[1].toDouble()
        val z = e.values[2].toDouble()

        val newPitch = Math.toDegrees(atan2(-x, sqrt(y*y + z*z)))
        val newRoll = Math.toDegrees(atan2(y, z))

        val a = 0.15
        pitch += a * (newPitch - pitch)
        roll += a * (newRoll - roll)

        pitchText.text = "Pitch: %.1f°".format(pitch - pitchZero)
        rollText.text = "Roll:  %.1f°".format(roll - rollZero)
        rawText.text = "Accelerometer:\nX %.2f   Y %.2f   Z %.2f".format(x, y, z)
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
}
