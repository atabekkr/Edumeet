package com.imax.edumeet.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.gson.Gson
import com.imax.edumeet.R
import com.imax.edumeet.databinding.FragmentTopicsBinding
import com.imax.edumeet.models.Sections
import com.imax.edumeet.ui.adapters.TopicAdapter
import com.imax.edumeet.utils.getJsonDataFromAsset
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TopicsFragment : Fragment(R.layout.fragment_topics) {

    private val binding by viewBinding(FragmentTopicsBinding::bind)
    private val navArgs by navArgs<TopicsFragmentArgs>()
    private val adapter = TopicAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val jsonString = getJsonDataFromAsset(requireContext(), "tema_topic.json")
        val data = Gson().fromJson(jsonString, Sections::class.java)

        val section = data.sections.single { it.id == navArgs.sectionId }

        binding.recyclerView.adapter = adapter

        binding.tvTitle.text = section.name

        val topics = section.topics
        adapter.submitList(topics)

        binding.btnBack.setOnClickListener { findNavController().popBackStack() }

        adapter.setOnItemClickListener { topicId ->
            findNavController().navigate(
                TopicsFragmentDirections.actionTopicsFragmentToTopicOverviewFragment(
                    topicId,
                    section.id
                )
            )
        }

    }

}