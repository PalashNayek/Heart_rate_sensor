package com.palash.heart_rate_sensor.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.palash.heart_rate_sensor.R
import com.palash.heart_rate_sensor.databinding.FragmentHomeBinding
import com.palash.heart_rate_sensor.view_models.HeartRateViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {
    private var _binding : FragmentHomeBinding?=null
    private val binding get() = _binding!!
    private val heartRateViewModel by viewModels<HeartRateViewModel>()

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
        heartRateViewModel.heartRateData.observe(viewLifecycleOwner, Observer {
            updateUI(it)
        })
        heartRateViewModel.startHeartRateUpdates()
    }

    private fun updateUI(it: Float?) {
        if (it != null) {
            binding.textViewHeartRate.text = "Heart Rate: $it bpm"
        } else {
            binding.textViewHeartRate.text = "Heart Rate not available"
        }
    }

    override fun onResume() {
        super.onResume()
        heartRateViewModel.startHeartRateUpdates()
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