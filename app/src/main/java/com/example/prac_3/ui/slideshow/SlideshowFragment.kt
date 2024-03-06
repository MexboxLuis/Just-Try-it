package com.example.prac_3.ui.slideshow

import android.app.Activity
import android.app.KeyguardManager
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.prac_3.databinding.FragmentSlideshowBinding
import kotlinx.coroutines.*

class SlideshowFragment : Fragment() {

    companion object {
        private const val REQUEST_CODE_CONFIRM_DEVICE_CREDENTIALS = 1
    }

    private var _binding: FragmentSlideshowBinding? = null
    private val binding get() = _binding!!
    private var success = false
    private lateinit var sensorManager: SensorManager
    private var temperatureSensor: Sensor? = null

    private val temperatureEventListener = object : SensorEventListener {
        override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {
            // No es necesario hacer nada aquí.
        }

        override fun onSensorChanged(event: SensorEvent) {
            val temperature = event.values[0]
            if (success == false){
                binding.temperatureTextView.visibility = View.GONE

            }

                binding.temperatureTextView.text = "Temperatura: $temperature °C"


        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSlideshowBinding.inflate(inflater, container, false)
        val root: View = binding.root

        sensorManager = requireActivity().getSystemService(Context.SENSOR_SERVICE) as SensorManager
        temperatureSensor = sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE)

        binding.btnAuthenticate.setOnClickListener {
            val keyguardManager = requireActivity().getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager
            if (keyguardManager.isDeviceSecure) {
                val intent = keyguardManager.createConfirmDeviceCredentialIntent(null, null)
                startActivityForResult(intent, REQUEST_CODE_CONFIRM_DEVICE_CREDENTIALS)
            }
        }

        return root
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_CONFIRM_DEVICE_CREDENTIALS) {
            if (resultCode == Activity.RESULT_OK) {
                binding.btnAuthenticate.setBackgroundColor(Color.GREEN)
                binding.btnAuthenticate.text = ("Autenticado")
                binding.viewOverlay.visibility = View.VISIBLE
                binding.progressBar.visibility = View.VISIBLE
                CoroutineScope(Dispatchers.Main).launch {
                    delay(3000L)
                    success = true
                    binding.lastJiji.text = "Sensor de Temperatura"
                    binding.lastJiji.setTextColor(Color.BLACK)
                    binding.btnAuthenticate.visibility = View.GONE
                    binding.viewOverlay.visibility = View.GONE
                    binding.progressBar.visibility = View.GONE
                    binding.temperatureTextView.visibility = View.VISIBLE
                }
            } else {
                binding.btnAuthenticate.setBackgroundColor(Color.RED)
                binding.btnAuthenticate.text = ("ERROR AL AUTENTICAR :(")
            }
        }
    }

    override fun onResume() {
        super.onResume()
        temperatureSensor?.also { sensor ->
            sensorManager.registerListener(temperatureEventListener, sensor, SensorManager.SENSOR_DELAY_NORMAL)
        }
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(temperatureEventListener)
    }
}
