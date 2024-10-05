package com.imax.edumeet.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.imax.edumeet.R
import com.imax.edumeet.databinding.FragmentWritingAiBinding
import com.imax.edumeet.ui.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class WritingAIFragment : Fragment(R.layout.fragment_writing_ai) {

    private val binding by viewBinding(FragmentWritingAiBinding::bind)
    private val viewModel by viewModels<MainViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.btnSend.setOnClickListener {
            val essay = binding.inputTopic.text.toString() + ": " + binding.etEssay.text.toString()
            viewModel.sendEssayToGPT(essay)
        }
        lifecycleScope.launch {
            viewModel.gptResponse.collectLatest { response ->
                response?.let {
                    binding.etEssay.text.clear()
                    binding.etResponse.setText(it)
                }
            }
        }
    }
}