package com.imax.edumeet.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.imax.edumeet.R
import com.imax.edumeet.databinding.FragmentHomeBinding
import com.imax.edumeet.utils.Constants
import com.imax.edumeet.utils.Constants.LISTENING
import com.imax.edumeet.utils.Constants.READING
import com.imax.edumeet.utils.Constants.SPEAKING
import com.imax.edumeet.utils.Constants.WRITING
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home) {

    private val binding by viewBinding(FragmentHomeBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.cardListening.setOnClickListener { findNavController().navigate(HomeFragmentDirections.actionMainFragmentToTopicsFragment(LISTENING)) }
        binding.cardReading.setOnClickListener { findNavController().navigate(HomeFragmentDirections.actionMainFragmentToTopicsFragment(READING)) }
        binding.cardSpeaking.setOnClickListener { findNavController().navigate(HomeFragmentDirections.actionMainFragmentToTopicsFragment(SPEAKING)) }
        binding.cardWriting.setOnClickListener { findNavController().navigate(HomeFragmentDirections.actionMainFragmentToTopicsFragment(WRITING)) }

        binding.btnNotifications.setOnClickListener { findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToNotificationFragment()) }

    }
}