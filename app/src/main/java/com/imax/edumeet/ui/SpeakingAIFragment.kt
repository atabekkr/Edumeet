package com.imax.edumeet.ui

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.speech.RecognizerIntent
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.imax.edumeet.R
import com.imax.edumeet.databinding.FragmentSpeakingAiBinding
import com.imax.edumeet.ui.viewmodel.MainViewModel
import com.imax.edumeet.utils.snackBar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.Objects

// on below line we are creating a constant value
private val REQUEST_CODE_SPEECH_INPUT = 1

@AndroidEntryPoint
class SpeakingAIFragment : Fragment(R.layout.fragment_speaking_ai) {

    private val binding by viewBinding(FragmentSpeakingAiBinding::bind)
    private val viewModel by viewModels<MainViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnMic.setOnClickListener {
            val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)

            intent.putExtra(
                RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
            )

            intent.putExtra(
                RecognizerIntent.EXTRA_LANGUAGE,
                "en-US"
            )
            intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak in English")

            intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak to text")

            try {
                startActivityForResult(intent, REQUEST_CODE_SPEECH_INPUT)
            } catch (e: Exception) {
                snackBar(e.message.toString())
            }
        }

        val essay = binding.inputTopic.text.toString() + ": " + binding.etEssay.text.toString()
        viewModel.sendEssayToGPT(essay)

        binding.btnSend.setOnClickListener {
            lifecycleScope.launch {
                viewModel.gptResponse.collectLatest { response ->
                    response?.let {
                        binding.etResponse.setText(it)
                    }
                }
            }
        }
        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_CODE_SPEECH_INPUT) {
            if (resultCode == RESULT_OK && data != null) {

                val res: ArrayList<String> =
                    data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS) as ArrayList<String>

                binding.etEssay.setText(
                    Objects.requireNonNull(res)[0]
                )
            }
        }
    }
}