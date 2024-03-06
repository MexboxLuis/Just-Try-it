package com.example.prac_3.ui.gallery

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.prac_3.databinding.FragmentGalleryBinding
import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager


class GalleryFragment : Fragment(), SensorEventListener {

    private var _binding: FragmentGalleryBinding? = null
    private val binding get() = _binding!!

    private lateinit var sensorManager: SensorManager
    private var accelerometer: Sensor? = null
    private var lightSensor: Sensor? = null
    private lateinit var accelerationTextView: TextView
    private lateinit var lightTextView: TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGalleryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        accelerationTextView = binding.aceleracionTextView
        lightTextView = binding.luzTextView

        sensorManager = requireActivity().getSystemService(Context.SENSOR_SERVICE) as SensorManager
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT)
    }

    override fun onResume() {
        super.onResume()
        accelerometer?.also { sensor ->
            sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL)
        }
        lightSensor?.also { sensor ->
            sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL)
        }
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        // No es necesario hacer nada aquí.
    }

    override fun onSensorChanged(event: SensorEvent?) {
        event?.let {
            when (it.sensor.type) {
                Sensor.TYPE_ACCELEROMETER -> {
                    val accelerationX = event.values[0]
                    val accelerationY = event.values[1]
                    val accelerationZ = event.values[2]
                    accelerationTextView.text = "Aceleración:\nX: $accelerationX\nY: $accelerationY\nZ: $accelerationZ"
                }
                Sensor.TYPE_LIGHT -> {
                    val lightValue = event.values[0]
                    lightTextView.text = "Luz: $lightValue"
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}







