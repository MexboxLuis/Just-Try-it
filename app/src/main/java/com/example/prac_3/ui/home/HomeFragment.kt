package com.example.prac_3.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.prac_3.databinding.FragmentHomeBinding
import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.graphics.Color
import android.widget.Toast

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var sensorManager: SensorManager
    private lateinit var accelerometerSensor: Sensor
    private lateinit var sensorEventListener: SensorEventListener
    private lateinit var mensajeTextView: TextView
    private var pass: Boolean = false
    private var count: Int = 0


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mensajeTextView = binding.mensajeTextView

        sensorManager = requireActivity().getSystemService(Context.SENSOR_SERVICE) as SensorManager
        accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)!!

        sensorEventListener = object : SensorEventListener {
            override fun onAccuracyChanged(sensor: Sensor?, accuray: Int) {
            }

            override fun onSensorChanged(event: SensorEvent?) {
                if (event?.sensor?.type == Sensor.TYPE_ACCELEROMETER) {
                    val x = event.values[0]
                    val y = event.values[1]
                    val z = event.values[2]

                    if (y < -9.0) {
                        mensajeTextView.text = "Movimiento detectado"
                        binding.root.setBackgroundColor(Color.GREEN)
                        pass = true
                    } else if (pass && y > 0) {
                        mensajeTextView.text = "Regreso a la normalidad"


                        binding.root.setBackgroundColor(Color.YELLOW)
                        count++
                        if (count % 5 == 0 && count != 0) {
                            val countString = count.toString()

                            Toast.makeText(requireContext(), "Ya has realizado esto por $countString veces", Toast.LENGTH_SHORT).show()

                        }
                        pass = false
                    }
                    else{

                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        sensorManager.registerListener(
            sensorEventListener,
            accelerometerSensor,
            SensorManager.SENSOR_DELAY_NORMAL
        )
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(sensorEventListener)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
