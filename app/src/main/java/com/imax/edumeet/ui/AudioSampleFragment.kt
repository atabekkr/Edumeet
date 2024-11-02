package com.imax.edumeet.ui

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.ParcelFileDescriptor
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.imax.edumeet.R
import com.imax.edumeet.databinding.FragmentAudioSampleBinding
import com.imax.edumeet.utils.AudioPlayer
import com.imax.edumeet.utils.AudioRecorder
import com.imax.edumeet.utils.snackBar
import com.tougee.recorderview.AudioRecordView
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import java.io.FileInputStream
import java.io.PrintWriter

@AndroidEntryPoint
class AudioSampleFragment : Fragment(R.layout.fragment_audio_sample), AudioRecordView.Callback {

    private val binding by viewBinding(FragmentAudioSampleBinding::bind)

    companion object {
        const val REQUEST_CAMERA_PERMISSION_RESULT = 123
    }

    private val file: File by lazy {
        val f = File("${requireContext().externalCacheDir}${File.separator}audio.pcm")
        if (!f.exists()) {
            f.createNewFile()
        }
        f
    }

    private val tmpFile: File by lazy {
        val f = File("${requireContext().externalCacheDir}${File.separator}tmp.pcm")
        if (!f.exists()) {
            f.createNewFile()
        }
        f
    }

    private var audioRecord: AudioRecorder? = null
    private var audioPlayer: AudioPlayer? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.fileTv.text = "path: ${file.absolutePath}\nlength: ${file.length()}"
        binding.recordView.apply {

            activity = requireActivity()
            callback = this@AudioSampleFragment

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

        binding.recordView.setTimeoutSeconds(20)
        binding.playIv.setOnClickListener {
            if (audioPlayer != null && audioPlayer!!.isPlaying) {
                binding.playIv.setImageResource(R.drawable.ic_play)
                audioPlayer!!.stop()
            } else {
                binding.playIv.setImageResource(R.drawable.ic_pause)
                audioPlayer = AudioPlayer(FileInputStream(file))
                audioPlayer!!.start()
            }
        }

    }

    override fun onResume() {
        super.onResume()
        requestPermission()
    }


    override fun onRecordStart() {
        snackBar("onRecordStart")

        clearFile(tmpFile)

        audioRecord =
            AudioRecorder(ParcelFileDescriptor.open(tmpFile, ParcelFileDescriptor.MODE_READ_WRITE))
        audioRecord?.start()
    }

    override fun isReady() = true

    override fun onRecordEnd() {
        snackBar("onEnd")
        audioRecord?.stop()

        tmpFile.copyTo(file, true)
    }

    override fun onRecordCancel() {
        snackBar("onCancel")
        audioRecord?.stop()
    }

    private fun clearFile(f: File) {
        PrintWriter(f).run {
            print("")
            close()
        }
    }

    private fun requestPermission() {
        @Suppress("ControlFlowWithEmptyBody")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.RECORD_AUDIO
                ) ==
                PackageManager.PERMISSION_GRANTED
            ) {
            } else {
                if (shouldShowRequestPermissionRationale(Manifest.permission.RECORD_AUDIO)) {
                    snackBar("App required access to audio")
                }
                requestPermissions(
                    arrayOf(Manifest.permission.RECORD_AUDIO),
                    REQUEST_CAMERA_PERMISSION_RESULT
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

        if (requestCode == REQUEST_CAMERA_PERMISSION_RESULT) {
            if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                snackBar("Application will not have audio on record")
            }
        }
    }
}