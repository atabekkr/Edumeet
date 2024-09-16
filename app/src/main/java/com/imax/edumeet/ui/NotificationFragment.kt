package com.imax.edumeet.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.imax.edumeet.R
import com.imax.edumeet.databinding.FragmentNotificationBinding
import com.imax.edumeet.ui.adapters.NotificationAdapter
import com.imax.edumeet.ui.viewmodel.MainViewModel
import com.imax.edumeet.utils.LocalStorage
import com.imax.edumeet.utils.snackBar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@AndroidEntryPoint
class NotificationFragment : Fragment(R.layout.fragment_notification) {

    private val binding by viewBinding(FragmentNotificationBinding::bind)
    private val viewModel by viewModels<MainViewModel>()
    private val adapter = NotificationAdapter()

    @Inject
    lateinit var localStorage: LocalStorage

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerView.adapter = adapter

        viewModel.getNotifications(localStorage.studentId)

        binding.btnBack.setOnClickListener { findNavController().popBackStack() }

        setupObservers()

    }

    private fun setupObservers() {
        viewModel.getNotificationResult.onEach { result ->
            result.onSuccess {
                adapter.submitList(it.notifications)
            }
            result.onFailure {
                it.printStackTrace()
                snackBar(it.localizedMessage)
            }
        }.launchIn(viewLifecycleOwner.lifecycleScope)
    }
}