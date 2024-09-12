package com.imax.edumeet.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.imax.edumeet.R
import com.imax.edumeet.databinding.FragmentLoginBinding
import com.imax.edumeet.utils.MaskWatcher
import com.imax.edumeet.utils.snackBar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment: Fragment(R.layout.fragment_login) {

    private val binding by viewBinding(FragmentLoginBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupUI()
        setupListeners()

    }

    private fun setupUI() {
        binding.etPhoneNumber.addTextChangedListener(MaskWatcher.phoneNumber())
    }

    private fun setupListeners() {
        binding.btnBack.setOnClickListener { findNavController().popBackStack() }

        binding.btnEnter.setOnClickListener {
            findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToContainerFragment())
//
//            val phoneNumber = "+998" + binding.etPhoneNumber.text.toString().filter { it.isDigit() }
//            val password = binding.inputPassword.text.toString()
//
//            if (phoneNumber.length >= 13 && password.length >= 8) {
//                findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToContainerFragment())
//            } else {
//                snackBar("Phone number or password incorrect")
//            }

        }
    }
}