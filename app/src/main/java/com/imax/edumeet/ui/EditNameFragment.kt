package com.imax.edumeet.ui

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import com.imax.edumeet.R
import com.imax.edumeet.databinding.FragmentEditNameBinding
import com.imax.edumeet.models.Group
import com.imax.edumeet.ui.viewmodel.MainViewModel
import com.imax.edumeet.utils.snackBar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class EditNameFragment: Fragment(R.layout.fragment_edit_name) {

    private val binding by viewBinding(FragmentEditNameBinding::bind)
    private val navArgs by navArgs<EditNameFragmentArgs>()
    private val viewModel by viewModels<MainViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupObservers()
        setupListeners()

    }

    private fun setupListeners() {
        binding.etName.setText(navArgs.name)

        binding.btnBack.setOnClickListener { findNavController().popBackStack() }

        binding.btnSave.setOnClickListener {
            val name = binding.etName.text.toString()
            viewModel.editName(Group(name))
            binding.loadingProgressBar.isVisible = true
        }
    }

    private fun setupObservers() {
        viewModel.authResult.onEach { result ->
            result.onSuccess {
                findNavController().popBackStack()
            }
            result.onFailure {
                it.printStackTrace()
                snackBar(it.localizedMessage)
            }
            binding.loadingProgressBar.isVisible = false
        }.launchIn(viewLifecycleOwner.lifecycleScope)
    }
}