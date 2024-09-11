package com.imax.edumeet.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.imax.edumeet.R
import com.imax.edumeet.databinding.FragmentAboutAppBinding
import com.imax.edumeet.models.AboutAppData
import com.imax.edumeet.ui.adapters.AboutAppSliderAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AboutAppFragment : Fragment(R.layout.fragment_about_app) {

    private val binding by viewBinding(FragmentAboutAppBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupData()
        setupListeners()

    }

    private fun setupData() {
        val texts = listOf(
            AboutAppData(
                1,
                getString(R.string.text_title_page_1),
                getString(R.string.text_description_page_1)
            ),
            AboutAppData(
                2,
                getString(R.string.text_title_page_2),
                getString(R.string.text_description_page_2)
            ),
            AboutAppData(
                3,
                getString(R.string.text_title_page_3),
                getString(R.string.text_description_page_3)
            )
        )
        val adapter = AboutAppSliderAdapter(texts)
        binding.viewPager.adapter = adapter
        binding.dotsIndicator.attachTo(binding.viewPager)
    }

    private fun setupListeners() {
        binding.tvEnter.setOnClickListener {
            findNavController().navigate(R.id.loginFragment)
        }

        binding.btnNext.setOnClickListener {

            if (binding.viewPager.currentItem == 2) {
                findNavController().navigate(R.id.loginFragment)
            } else
                binding.viewPager.currentItem++

        }
    }

}