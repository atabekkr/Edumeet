package com.imax.edumeet.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.view.MotionEvent
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
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

private val REQUEST_CODE_SPEECH_INPUT = 1

@AndroidEntryPoint
class SpeakingAIFragment : Fragment(R.layout.fragment_speaking_ai) {

    private val binding by viewBinding(FragmentSpeakingAiBinding::bind)
    private val viewModel by viewModels<MainViewModel>()

    private var speechRecognizer: SpeechRecognizer? = null

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(requireContext())
        val speechRecognizerIntent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        speechRecognizerIntent.putExtra(
            RecognizerIntent.EXTRA_LANGUAGE_MODEL,
            RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
        )
        speechRecognizerIntent.putExtra(
            RecognizerIntent.EXTRA_LANGUAGE,
            "en-US"
        )

        speechRecognizer?.setRecognitionListener(object : RecognitionListener {
            override fun onReadyForSpeech(params: Bundle?) {}

            override fun onBeginningOfSpeech() {
                binding.etEssay.setText("")
            }

            override fun onRmsChanged(rmsdB: Float) {}

            override fun onBufferReceived(buffer: ByteArray?) {}

            override fun onEndOfSpeech() {}

            override fun onError(error: Int) {}

            override fun onResults(results: Bundle?) {
                binding.btnMic.imageTintList =
                    ContextCompat.getColorStateList(requireContext(), R.color.brand_color)
                val data = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                binding.etEssay.setText(data?.getOrNull(0))
            }

            override fun onPartialResults(partialResults: Bundle?) {}

            override fun onEvent(eventType: Int, params: Bundle?) {}
        })

        binding.btnMic.setOnTouchListener { v, event ->
            if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    android.Manifest.permission.RECORD_AUDIO
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                checkPermission()
            } else {
                if (event.action == MotionEvent.ACTION_UP) {
                    speechRecognizer?.stopListening()
                }
                if (event.action == MotionEvent.ACTION_DOWN) {
                    binding.btnMic.imageTintList =
                        ContextCompat.getColorStateList(requireContext(), R.color.green)
                    speechRecognizer?.startListening(speechRecognizerIntent)
                }
            }
            false
        }

        binding.btnSend.setOnClickListener {
            val essay = binding.inputTopic.text.toString() + ": " + binding.etEssay.text.toString()
            viewModel.sendEssayToGPT(essay)
        }

        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }

        lifecycleScope.launch {
            viewModel.gptResponse.collectLatest { response ->
                response?.let {
                    binding.etResponse.setText(it)
                }
            }
        }

    }

    private fun checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ActivityCompat.requestPermissions(
                requireActivity(), arrayOf(android.Manifest.permission.RECORD_AUDIO),
                REQUEST_CODE_SPEECH_INPUT
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray,
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_SPEECH_INPUT && grantResults.isNotEmpty()) {
            snackBar("Permission Granted")
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        speechRecognizer?.destroy()
    }
}