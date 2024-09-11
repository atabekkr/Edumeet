package com.imax.edumeet.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.imax.edumeet.R
import com.imax.edumeet.databinding.FragmentTopicsBinding
import com.imax.edumeet.models.TopicData
import com.imax.edumeet.ui.adapters.TopicAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TopicsFragment : Fragment(R.layout.fragment_topics) {

    private val binding by viewBinding(FragmentTopicsBinding::bind)
    private val adapter = TopicAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerView.adapter = adapter

        val listOfTopic = listOf(
            TopicData("Advanced passives review"),
            TopicData("Advanced present simple and continuous"),
            TopicData("A threat to bananas")
        )
        adapter.submitList(listOfTopic)

        binding.btnBack.setOnClickListener { findNavController().popBackStack() }

    }

}