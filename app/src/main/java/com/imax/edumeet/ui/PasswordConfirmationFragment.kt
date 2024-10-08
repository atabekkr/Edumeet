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
import com.imax.edumeet.databinding.FragmentPasswordConfirmationBinding
import com.imax.edumeet.models.Login
import com.imax.edumeet.ui.viewmodel.MainViewModel
import com.imax.edumeet.utils.LocalStorage
import com.imax.edumeet.utils.snackBar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@AndroidEntryPoint
class PasswordConfirmationFragment: Fragment(R.layout.fragment_password_confirmation) {

    private val binding by viewBinding(FragmentPasswordConfirmationBinding::bind)
    private val navArgs by navArgs<PasswordConfirmationFragmentArgs>()
    private val viewModel by viewModels<MainViewModel>()

    @Inject
    lateinit var localStorage: LocalStorage

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupObservers()
        setupListeners()

    }

    private fun setupListeners() {

        binding.etPhone.setText(navArgs.phone)
        binding.etPassword.setText(navArgs.password)

        binding.btnBack.setOnClickListener { findNavController().popBackStack() }

        binding.btnSave.setOnClickListener {
            val phone = binding.etPhone.text.toString()
            val password = binding.etPassword.text.toString()
            binding.loadingProgressBar.isVisible = true
            viewModel.editPhoneAndPassword(Login(phone, password))
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