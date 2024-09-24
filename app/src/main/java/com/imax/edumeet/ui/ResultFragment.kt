package com.imax.edumeet.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import com.imax.edumeet.R
import com.imax.edumeet.databinding.FragmentResultBinding
import com.imax.edumeet.models.SendScore
import com.imax.edumeet.ui.viewmodel.MainViewModel
import com.imax.edumeet.utils.LocalStorage
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ResultFragment : Fragment(R.layout.fragment_result) {

    private val binding by viewBinding(FragmentResultBinding::bind)
    private val navArgs by navArgs<ResultFragmentArgs>()
    private val viewModel by viewModels<MainViewModel>()

    @Inject
    lateinit var localStorage: LocalStorage

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tvResult.text = getString(R.string.correct_answers_s, navArgs.result.toString())

        val section = when (navArgs.sectionId) {
            1 -> "Listening"
            2 -> "Reading"
            3 -> "Writing"
            else -> "Speaking"
        }
        viewModel.sendPercent(
            SendScore(
                localStorage.studentId,
                section,
                navArgs.topicId.toString(),
                navArgs.result
            )
        )

        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }

    }
}