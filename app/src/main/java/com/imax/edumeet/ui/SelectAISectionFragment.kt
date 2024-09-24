package com.imax.edumeet.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.imax.edumeet.R
import com.imax.edumeet.databinding.FragmentSelectAiSectionBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SelectAISectionFragment: Fragment(R.layout.fragment_select_ai_section) {

    private val binding by viewBinding(FragmentSelectAiSectionBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.cardWriting.setOnClickListener {
            findNavController().navigate(
                SelectAISectionFragmentDirections.actionSelectAISectionFragmentToWritingAIFragment()
            )
        }

        binding.cardSpeaking.setOnClickListener {
            findNavController().navigate(
                SelectAISectionFragmentDirections.actionSelectAISectionFragmentToSpeakingAIFragment()
            )
        }

        binding.btnBack.setOnClickListener { findNavController().popBackStack() }

    }
}