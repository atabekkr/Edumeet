package com.imax.edumeet.ui

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
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

    private val adapter = RatingOfStudentAdapter()

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

        val listOfGroups = listOf("Listening", "Reading", "Writing", "Speaking")
        val arrayAdapter = ArrayAdapter(requireContext(), R.layout.item_section, listOfGroups)
        binding.selectSection.setAdapter(arrayAdapter)

        binding.recyclerView.adapter = adapter
    }

    private fun setupListeners() {
        binding.btnBack.setOnClickListener { findNavController().popBackStack() }
    }

    private fun setupObservers() {
        viewModel.getRatingBySection.onEach { result ->
            result.onSuccess {
                if (it.data.isNotEmpty()) {
                    binding.tvNotFound.visibility = View.GONE
                    binding.recyclerView.visibility = View.VISIBLE
                    adapter.submitList(it.data)
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