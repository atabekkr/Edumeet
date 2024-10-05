package com.imax.edumeet.ui

import android.os.Bundle
import android.view.View
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.imax.edumeet.R
import com.imax.edumeet.databinding.FragmentRatingBinding
import com.imax.edumeet.ui.adapters.RatingOfStudentAdapter
import com.imax.edumeet.ui.viewmodel.MainViewModel
import com.imax.edumeet.utils.snackBar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class RatingFragment : Fragment(R.layout.fragment_rating) {

    private val binding by viewBinding(
        FragmentRatingBinding::bind
    )
    private val viewModel by viewModels<MainViewModel>()

    private var isSectionsOpen = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupListeners()
        setupData()
        fetchData()
        setupObservers()

    }

    private fun fetchData() {
        viewModel.getRatingBySection("Listening")
    }

    private fun setupData() {

        binding.selectSection.addTextChangedListener { section ->
            viewModel.getRatingBySection(section.toString())
        }

    }

    private fun setupListeners() {
        binding.btnBack.setOnClickListener { findNavController().popBackStack() }
        binding.tilSection.setOnClickListener {
            if (isSectionsOpen) {
                binding.sections.visibility = View.GONE
                binding.ivDropDown.rotation = 0f
            } else {
                binding.ivDropDown.rotation = 180f
                binding.sections.visibility = View.VISIBLE
            }
            isSectionsOpen = !isSectionsOpen
        }
        binding.tvSectionListening.setOnClickListener {
            binding.selectSection.setText(R.string.listening)
            binding.sections.visibility = View.GONE
            binding.ivDropDown.rotation = 0f
            isSectionsOpen = false
        }
        binding.tvSectionReading.setOnClickListener {
            binding.selectSection.setText(R.string.reading)
            binding.sections.visibility = View.GONE
            binding.ivDropDown.rotation = 0f
            isSectionsOpen = false
        }
        binding.tvSectionWriting.setOnClickListener {
            binding.selectSection.setText(R.string.writing)
            binding.sections.visibility = View.GONE
            binding.ivDropDown.rotation = 0f
            isSectionsOpen = false
        }
        binding.tvSectionSpeaking.setOnClickListener {
            binding.selectSection.setText(R.string.speaking)
            binding.sections.visibility = View.GONE
            binding.ivDropDown.rotation = 0f
            isSectionsOpen = false
        }
    }

    private fun setupObservers() {
        viewModel.getRatingBySection.onEach { result ->
            result.onSuccess {
                if (it.data.isNotEmpty()) {
                    binding.tvNotFound.visibility = View.GONE
                    binding.recyclerView.visibility = View.VISIBLE
                    val adapter = RatingOfStudentAdapter(it.data)
                    binding.recyclerView.adapter = adapter
                } else {
                    binding.tvNotFound.visibility = View.VISIBLE
                    binding.recyclerView.visibility = View.GONE
                }
            }
            result.onFailure {
                it.printStackTrace()
                snackBar(it.localizedMessage)
            }
        }.launchIn(viewLifecycleOwner.lifecycleScope)
    }
}