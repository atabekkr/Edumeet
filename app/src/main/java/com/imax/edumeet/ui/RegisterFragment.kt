package com.imax.edumeet.ui

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.imax.edumeet.R
import com.imax.edumeet.data.models.Register
import com.imax.edumeet.databinding.FragmentRegisterBinding
import com.imax.edumeet.models.Group
import com.imax.edumeet.ui.viewmodel.MainViewModel
import com.imax.edumeet.utils.LocalStorage
import com.imax.edumeet.utils.MaskWatcher
import com.imax.edumeet.utils.snackBar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@AndroidEntryPoint
class RegisterFragment : Fragment(R.layout.fragment_register) {

    private val binding by viewBinding(FragmentRegisterBinding::bind)
    private val viewModel by viewModels<MainViewModel>()

    @Inject
    lateinit var localStorage: LocalStorage

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupObservers()
        viewModel.getGroups()

        binding.etPhoneNumber.addTextChangedListener(MaskWatcher.phoneNumber())

        binding.btnBack.setOnClickListener { findNavController().popBackStack() }

        binding.goToLogin.setOnClickListener { findNavController().popBackStack() }

        binding.btnEnter.setOnClickListener {
            val name = binding.inputName.text.toString()
            val phoneNumber = "+998" + binding.etPhoneNumber.text.toString().filter { it.isDigit() }
            val password = binding.inputPassword.text.toString()
            val group = binding.inputGroup.text.toString()
            viewModel.register(Register(name, password, phoneNumber, group))
            binding.loadingProgressBar.isVisible = true
        }

    }

    private fun setupObservers() {
        viewModel.authResult.onEach { result ->
            result.onSuccess {
                localStorage.token = it.token
                localStorage.isLogin = true
                localStorage.group = it.student.group
                localStorage.studentId = it.student._id
                findNavController().navigate(
                    RegisterFragmentDirections.actionRegisterFragmentToContainerFragment()
                )
            }
            result.onFailure {
                it.printStackTrace()
                snackBar(it.localizedMessage)
            }
            binding.loadingProgressBar.isVisible = false
        }.launchIn(viewLifecycleOwner.lifecycleScope)

        viewModel.getGroups.onEach { result ->
            result.onSuccess {
                val listOfGroups = it.map { group: Group -> group.name }
                val arrayAdapter = ArrayAdapter(requireContext(), R.layout.item_dropdown, listOfGroups)
                binding.inputGroup.setAdapter(arrayAdapter)
            }
            result.onFailure {
                it.printStackTrace()
                snackBar(it.localizedMessage)
            }
        }.launchIn(viewLifecycleOwner.lifecycleScope)
    }
}