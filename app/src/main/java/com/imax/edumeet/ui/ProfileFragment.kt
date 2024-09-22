package com.imax.edumeet.ui

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.imax.edumeet.R
import com.imax.edumeet.databinding.FragmentProfileBinding
import com.imax.edumeet.ui.viewmodel.MainViewModel
import com.imax.edumeet.utils.snackBar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class ProfileFragment : Fragment(R.layout.fragment_profile) {

    private val binding by viewBinding(FragmentProfileBinding::bind)
    private val viewModel by viewModels<MainViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.loadingProgressBar.isVisible = true
        viewModel.getStudent()
        setupObservers()

        binding.ivEditName.setOnClickListener {
            val name = binding.tvName.text.toString()
            findNavController().navigate(
                ProfileFragmentDirections.actionProfileFragmentToEditNameFragment(name)
            )
        }

        binding.ivEditNumber.setOnClickListener {
            val phone = binding.tvPhone.text.toString()
            val password = binding.tvPassword.text.toString()
            findNavController().navigate(
                ProfileFragmentDirections.actionProfileFragmentToPasswordConfirmationFragment(
                    phone,
                    password
                )
            )
        }

        binding.ivEditPassword.setOnClickListener {
            val phone = binding.tvPhone.text.toString()
            val password = binding.tvPassword.text.toString()
            findNavController().navigate(
                ProfileFragmentDirections.actionProfileFragmentToPasswordConfirmationFragment(
                    phone,
                    password
                )
            )
        }

    }

    private fun setupObservers() {
        viewModel.getStudent.onEach { result ->
            result.onSuccess {
                binding.tvName.text = it.name
                binding.etName.text = it.name
                binding.tvPhone.text = it.phone
                binding.tvGroup.text = it.group
                binding.tvPassword.text = it.originalPassword
            }
            result.onFailure {
                it.printStackTrace()
                snackBar(it.localizedMessage)
            }
            binding.loadingProgressBar.isVisible = false
        }.launchIn(viewLifecycleOwner.lifecycleScope)
    }

}