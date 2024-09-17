package com.imax.edumeet.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import com.imax.edumeet.R
import com.imax.edumeet.databinding.FragmentPasswordConfirmationBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PasswordConfirmationFragment: Fragment(R.layout.fragment_password_confirmation) {

    private val binding by viewBinding(FragmentPasswordConfirmationBinding::bind)
    private val navArgs by navArgs<PasswordConfirmationFragmentArgs>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.etPhone.setText(navArgs.phone)
        binding.etPassword.setText(navArgs.password)

        binding.btnBack.setOnClickListener { findNavController().popBackStack() }

    }
}