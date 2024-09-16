package com.imax.edumeet.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.gson.Gson
import com.imax.edumeet.R
import com.imax.edumeet.databinding.FragmentTopicOverviewBinding
import com.imax.edumeet.models.Theory
import com.imax.edumeet.ui.adapters.TopicOverviewAdapter
import com.imax.edumeet.utils.getJsonDataFromAsset
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TopicOverviewFragment : Fragment(R.layout.fragment_topic_overview) {

    private val binding by viewBinding(FragmentTopicOverviewBinding::bind)
    private val navArgs by navArgs<TopicOverviewFragmentArgs>()
    private val adapter = TopicOverviewAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val jsonString = getJsonDataFromAsset(requireContext(), "teorya.json")
        val data = Gson().fromJson(jsonString, Theory::class.java)

        val reading = data.theory.single { it.id == navArgs.sectionId }
        val content = reading.topics.single { it.topic_id == navArgs.topicId }

        binding.recyclerView.adapter = adapter

        val title = content.content.single { it.type == "Title" }.body
        binding.tvTitle.text = title.toString()

        adapter.models = content.content

        binding.btnBack.setOnClickListener {
            adapter.releaseMediaPlayer()
            findNavController().popBackStack()
        }

        binding.btnGoToTest.setOnClickListener {
            findNavController().navigate(
                TopicOverviewFragmentDirections.actionTopicOverviewFragmentToTaskFragment(
                    navArgs.topicId
                )
            )
        }

    }
}