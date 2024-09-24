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
import com.imax.edumeet.databinding.FragmentHomeBinding
import com.imax.edumeet.ui.viewmodel.MainViewModel
import com.imax.edumeet.utils.Constants.LISTENING
import com.imax.edumeet.utils.Constants.READING
import com.imax.edumeet.utils.Constants.SPEAKING
import com.imax.edumeet.utils.Constants.WRITING
import com.imax.edumeet.utils.LocalStorage
import com.imax.edumeet.utils.snackBar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home) {

    private val binding by viewBinding(FragmentHomeBinding::bind)
    private val viewModel by viewModels<MainViewModel>()

    @Inject
    lateinit var localStorage: LocalStorage

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        fetchData()
        setupListeners()
        setupObservers()


    }

    private fun fetchData() {
        binding.loadingProgressBar.isVisible = true
        viewModel.getCountOfNotification(localStorage.studentId)
        viewModel.getStudentScores(localStorage.studentId)
    }

    private fun setupListeners() {
        binding.cardListening.setOnClickListener {
            findNavController().navigate(
                HomeFragmentDirections.actionMainFragmentToTopicsFragment(LISTENING)
            )
        }
        binding.cardReading.setOnClickListener {
            findNavController().navigate(
                HomeFragmentDirections.actionMainFragmentToTopicsFragment(
                    READING
                )
            )
        }
        binding.cardWriting.setOnClickListener {
            findNavController().navigate(
                HomeFragmentDirections.actionMainFragmentToTopicsFragment(
                    WRITING
                )
            )
        }
        binding.cardSpeaking.setOnClickListener {
            findNavController().navigate(
                HomeFragmentDirections.actionMainFragmentToTopicsFragment(SPEAKING)
            )
        }

        binding.btnNotifications.setOnClickListener {
            findNavController().navigate(
                HomeFragmentDirections.actionHomeFragmentToNotificationFragment()
            )
        }

    }

    private fun setupObservers() {
        viewModel.getCountOfNotification.onEach { result ->
            result.onSuccess {
                binding.tvCountOfNotification.isVisible = it != 0
                binding.tvCountOfNotification.text = it.toString()
            }
            result.onFailure {
                it.printStackTrace()
                snackBar(it.localizedMessage)
            }
        }.launchIn(viewLifecycleOwner.lifecycleScope)

        viewModel.getStudentScores.onEach { result ->
            result.onSuccess { studentScores ->
                val lessons = studentScores.lessons

                val listening = lessons.singleOrNull { it.lesson == "Listening" }
                binding.tvListeningPercent.text = "${listening?.percentage}%"
                binding.progressListening.progress = listening?.percentage ?: 0

                val reading = lessons.singleOrNull { it.lesson == "Reading" }
                binding.tvReadingPercent.text = "${reading?.percentage}%"
                binding.progressReading.progress = reading?.percentage ?: 0

                val writing = lessons.singleOrNull { it.lesson == "Writing" }
                binding.tvWritingPercent.text = "${writing?.percentage}%"
                binding.progressWriting.progress = writing?.percentage ?: 0

                val speaking = lessons.singleOrNull { it.lesson == "Speaking" }
                binding.tvSpeakingPercent.text = "${speaking?.percentage}%"
                binding.progressSpeaking.progress = speaking?.percentage?: 0
            }
            result.onFailure {
                it.printStackTrace()
                snackBar(it.localizedMessage)
            }
            binding.loadingProgressBar.isVisible = false
        }.launchIn(viewLifecycleOwner.lifecycleScope)
    }
}