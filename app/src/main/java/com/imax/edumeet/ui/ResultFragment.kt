package com.imax.edumeet.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import com.imax.edumeet.R
import com.imax.edumeet.databinding.FragmentResultBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ResultFragment : Fragment(R.layout.fragment_result) {

    private val binding by viewBinding(FragmentResultBinding::bind)
    private val navArgs by navArgs<ResultFragmentArgs>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tvResult.text = getString(R.string.correct_answers_s, navArgs.result.toString())

        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }

    }
}