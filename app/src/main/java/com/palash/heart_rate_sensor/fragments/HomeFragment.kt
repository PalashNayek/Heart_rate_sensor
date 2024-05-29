package com.palash.heart_rate_sensor.fragments

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.palash.heart_rate_sensor.R
import com.palash.heart_rate_sensor.databinding.FragmentHomeBinding
import com.palash.heart_rate_sensor.view_models.HeartRateViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val heartRateViewModel by viewModels<HeartRateViewModel>()

    companion object {
        private const val PERMISSION_REQUEST_BODY_SENSORS = 1
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        heartRateViewModel.heartRateData.observe(viewLifecycleOwner, Observer { heartRate ->
            updateUI(heartRate)
        })
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.BODY_SENSORS)
            == PackageManager.PERMISSION_GRANTED
        ) {
            heartRateViewModel.startHeartRateUpdates()
        } else {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.BODY_SENSORS),
                PERMISSION_REQUEST_BODY_SENSORS
            )
        }
    }

    private fun updateUI(heartRate: Float?) {
        if (heartRate != null) {
            binding.textViewHeartRate.text = "Heart Rate: $heartRate bpm"
        } else {
            binding.textViewHeartRate.text = "Heart Rate Sensors are not available in your smart phone"
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            PERMISSION_REQUEST_BODY_SENSORS -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    heartRateViewModel.startHeartRateUpdates()
                } else {
                    Toast.makeText(
                        requireContext(),
                        "Permission denied to access body sensors",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                return
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.BODY_SENSORS)
            == PackageManager.PERMISSION_GRANTED
        ) {
            heartRateViewModel.startHeartRateUpdates()
        }
    }

    override fun onPause() {
        super.onPause()
        heartRateViewModel.stopHeartRateUpdates()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}