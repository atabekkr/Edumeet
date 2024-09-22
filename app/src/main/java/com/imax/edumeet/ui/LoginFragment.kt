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
import com.imax.edumeet.databinding.FragmentLoginBinding
import com.imax.edumeet.models.Login
import com.imax.edumeet.ui.viewmodel.MainViewModel
import com.imax.edumeet.utils.LocalStorage
import com.imax.edumeet.utils.MaskWatcher
import com.imax.edumeet.utils.snackBar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@AndroidEntryPoint
class LoginFragment : Fragment(R.layout.fragment_login) {

    private val binding by viewBinding(FragmentLoginBinding::bind)
    private val viewModel by viewModels<MainViewModel>()

    @Inject
    lateinit var localStorage: LocalStorage

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupUI()
        setupListeners()
        setupObservers()

    }

    private fun setupUI() {
        binding.etPhoneNumber.addTextChangedListener(MaskWatcher.phoneNumber())
    }

    private fun setupListeners() {
        binding.btnBack.setOnClickListener { findNavController().popBackStack() }

        binding.goToRegister.setOnClickListener {
            findNavController().navigate(
                LoginFragmentDirections.actionLoginFragmentToRegisterFragment()
            )
        }

        binding.btnEnter.setOnClickListener {

            val phoneNumber = "+998" + binding.etPhoneNumber.text.toString().filter { it.isDigit() }
            val password = binding.inputPassword.text.toString()

            if (phoneNumber.length >= 13 && password.length >= 4) {
                viewModel.login(Login(phoneNumber, password))
                binding.loadingProgressBar.isVisible = true
            } else {
                snackBar("Phone number or password incorrect")
            }

        }
    }

    private fun setupObservers() {
        viewModel.authResult.onEach { result ->
            result.onSuccess {
                localStorage.token = it.token
                localStorage.isLogin = true
                localStorage.group = it.student.group
                localStorage.studentId = it.student._id
                findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToContainerFragment())
            }
            result.onFailure {
                it.printStackTrace()
                snackBar(it.localizedMessage)
            }
            binding.loadingProgressBar.isVisible = false
        }.launchIn(viewLifecycleOwner.lifecycleScope)
    }
}