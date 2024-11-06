package com.imax.edumeet.ui

import android.Manifest
import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.media.MediaRecorder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.imax.edumeet.R
import com.imax.edumeet.data.ai.WhisperApi
import com.imax.edumeet.data.ai.WhisperApiImpl
import com.imax.edumeet.data.ai.createHttpClient
import com.imax.edumeet.databinding.FragmentSpeakingAiBinding
import com.imax.edumeet.ui.viewmodel.MainViewModel
import com.imax.edumeet.utils.snackBar
import com.tougee.recorderview.AudioRecordView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import java.io.File
import java.io.IOException

@AndroidEntryPoint
class SpeakingAIFragment : Fragment(R.layout.fragment_speaking_ai), AudioRecordView.Callback {

    private val binding by viewBinding(FragmentSpeakingAiBinding::bind)
    private val viewModel by viewModels<MainViewModel>()

    private val file: File by lazy {
        val f = File("${requireContext().externalCacheDir}${File.separator}audio.mp3")
        if (!f.exists()) {
            f.createNewFile()
        }
        f
    }

    private lateinit var whisperApi: WhisperApi
    private val json = Json { ignoreUnknownKeys = true }
    private var audioUri: Uri? = null

    private var mediaRecorder: MediaRecorder? = null

    companion object {
        const val REQUEST_RECORD_AUDIO_PERMISSION_RESULT = 123
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requestPermission()

        // Initialize the API with the HTTP client
        whisperApi = WhisperApiImpl(createHttpClient(json))

        binding.recordView.apply {

            activity = requireActivity()
            callback = this@SpeakingAIFragment

            /*micIcon = com.tougee.recorderview.R.drawable.ic_chevron_left_gray_24dp
            micActiveIcon = R.drawable.ic_play
            micHintEnable = false
            micHintText = "Custom hint text"
            micHintColor = ContextCompat.getColor(requireContext(), android.R.color.holo_red_light)
            micHintBg = R.drawable.ic_launcher_background
            blinkColor = ContextCompat.getColor(requireContext(), R.color.color_blue)
            circleColor = ContextCompat.getColor(requireContext(), R.color.color_blink)
            cancelIconColor = ContextCompat.getColor(requireContext(), R.color.color_blue)
            slideCancelText = "Custom Slide to cancel"
            cancelText = "Custom Cancel"
            vibrationEnable = false*/
        }

        binding.recordView.setTimeoutSeconds(200)

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

    override fun onRecordStart() {
        binding.btnSend.isVisible = false
        startRecording()
    }

    override fun isReady() = true

    override fun onRecordEnd() {
        binding.btnSend.isVisible = true
        stopRecording()

        val filename = "audio.mp3" // Ensure correct file extension
        audioUri = file.toUri()

        val fileBytes = uriToByteArray(audioUri!!)

        lifecycleScope.launch {
            val result = whisperApi.transcribe(fileBytes, filename)
            result.onSuccess {
                binding.etEssay.setText(it.summary)
            }.onFailure {
                it.printStackTrace()
                Toast.makeText(requireContext(), "Transcription failed", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    private fun startRecording() {
        mediaRecorder = MediaRecorder().apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
            setOutputFile(file)
            setAudioEncoder(MediaRecorder.AudioEncoder.AAC)

            try {
                prepare()
            } catch (e: IOException) {
                Log.e("TTTT", "prepare() failed")
            }

            start()
        }
    }

    private fun stopRecording() {
        mediaRecorder?.apply {
            stop()
            release()
        }
        mediaRecorder = null
    }

    override fun onStop() {
        super.onStop()
        mediaRecorder?.release()
        mediaRecorder = null
    }

    private fun uriToByteArray(uri: Uri): ByteArray {
        requireContext().contentResolver.openInputStream(uri)?.use { inputStream ->
            return inputStream.readBytes()
        }
        return ByteArray(0)
    }

    override fun onRecordCancel() {
        snackBar("onCancel")
        stopRecording()
    }

    private fun requestPermission() {
        @Suppress("ControlFlowWithEmptyBody")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.RECORD_AUDIO
                ) ==
                PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                    requireContext(),
                    WRITE_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                    requireContext(),
                    READ_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED
            ) {
            } else {
                if (shouldShowRequestPermissionRationale(Manifest.permission.RECORD_AUDIO)) {
                    snackBar("App required access to audio")
                }
                requestPermissions(
                    arrayOf(
                        Manifest.permission.RECORD_AUDIO,
                        WRITE_EXTERNAL_STORAGE,
                        READ_EXTERNAL_STORAGE
                    ),
                    REQUEST_RECORD_AUDIO_PERMISSION_RESULT
                )
            }
        } else {
            // put your code for Version < Marshmallow
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray,
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == REQUEST_RECORD_AUDIO_PERMISSION_RESULT) {
            if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                snackBar("Application will not have audio on record")
            }
        }
    }
}