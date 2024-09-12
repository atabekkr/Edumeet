package com.imax.edumeet.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.imax.edumeet.R
import com.imax.edumeet.databinding.FragmentNotificationBinding
import com.imax.edumeet.models.NotificationData
import com.imax.edumeet.ui.adapters.NotificationAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NotificationFragment:Fragment(R.layout.fragment_notification) {

    private val binding by viewBinding(FragmentNotificationBinding::bind)
    private val adapter = NotificationAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerView.adapter = adapter

        val listOfNotify = listOf(NotificationData("Such a shame", "Khin Min"),NotificationData("Lorem ipsum dolor sit amet consectetur...", "Margarita Sergeevna"))
        adapter.submitList(listOfNotify)

        binding.btnBack.setOnClickListener { findNavController().popBackStack() }

    }
}