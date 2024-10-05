package com.imax.edumeet.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bumptech.glide.Glide
import com.imax.edumeet.R
import com.imax.edumeet.data.models.ProfileImage
import com.imax.edumeet.databinding.FragmentProfileBinding
import com.imax.edumeet.ui.viewmodel.MainViewModel
import com.imax.edumeet.utils.snackBar
import com.imax.edumeet.utils.uploadImage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.json.JSONObject
import java.io.File
import java.io.IOException

@AndroidEntryPoint
class ProfileFragment : Fragment(R.layout.fragment_profile) {

    private val binding by viewBinding(FragmentProfileBinding::bind)
    private val viewModel by viewModels<MainViewModel>()

    private lateinit var pLauncher: ActivityResultLauncher<String>
    private val client = OkHttpClient()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.loadingProgressBar.isVisible = true
        viewModel.getStudent()
        setupObservers()
        registerPermissionListener()

        binding.ivEditName.setOnClickListener {
            val name = binding.tvName.text.toString()
            findNavController().navigate(
                ProfileFragmentDirections.actionProfileFragmentToEditNameFragment(name)
            )
        }

        binding.ivEditNumber.setOnClickListener {
            val phone = binding.tvPhone.text.toString()
            val password = binding.tvPassword.text.toString()
            findNavController().navigate(
                ProfileFragmentDirections.actionProfileFragmentToPasswordConfirmationFragment(
                    phone,
                    password
                )
            )
        }

        binding.ivEditPassword.setOnClickListener {
            val phone = binding.tvPhone.text.toString()
            val password = binding.tvPassword.text.toString()
            findNavController().navigate(
                ProfileFragmentDirections.actionProfileFragmentToPasswordConfirmationFragment(
                    phone,
                    password
                )
            )
        }

        binding.cardUserImage.setOnClickListener {
            checkGalleryPermission()
        }

    }

    private fun fetchSelectedImagesFromGallery(data: Intent?) {
        try {
            if (data?.data != null) {
                val imageUri = data.data
                Glide
                    .with(requireContext())
                    .load(imageUri)
                    .centerCrop()
                    .into(binding.userImage)

                binding.loadingProgressBar.isVisible = true
                lifecycleScope.launch {
                    imageUri?.let {
                        uploadImageToCloudinary(it) { imageUrl ->
                            if (imageUrl != null) {
                                postProfileImage(imageUrl)
                            } else {
                                snackBar("Error in uploading image!")
                            }
                        }
                    }
                }
            }
        } catch (e: Exception) {
            snackBar(e.localizedMessage)
        }
    }

    private fun uploadImageToCloudinary(fileUri: Uri, callback: (String?) -> Unit) {
        val requestBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addPart(
                uploadImage(fileUri, requireContext().contentResolver)!!
            )
            .addFormDataPart("upload_preset", "restoran-order")
            .addFormDataPart("cloud_name", "djsdapm3z")
            .build()

        val request = Request.Builder()
            .url("https://api.cloudinary.com/v1_1/djsdapm3z/image/upload")
            .post(requestBody)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
                callback(null)
            }

            override fun onResponse(call: Call, response: Response) {
                response.body?.string()?.let { responseBody ->
                    val json = JSONObject(responseBody)
                    val imageUrl = json.getString("secure_url")
                    callback(imageUrl)
                }
            }
        })
    }

    // Yuklangan rasm URL'ni API'ga yuborish
    private fun postProfileImage(profileImageUrl: String) {
        viewModel.editProfileImage(ProfileImage(profileImageUrl))
    }

    private fun setupObservers() {
        viewModel.getStudent.onEach { result ->
            result.onSuccess {
                binding.tvName.text = it.name
                binding.etName.text = it.name
                binding.tvPhone.text = it.phone
                binding.tvGroup.text = it.group
                binding.tvPassword.text = it.originalPassword
                Glide
                    .with(requireContext())
                    .load(it.profileImage)
                    .centerCrop()
                    .error(R.drawable.pic_temporary_image)
                    .into(binding.userImage)
            }
            result.onFailure {
                it.printStackTrace()
                snackBar(it.localizedMessage)
            }
            binding.loadingProgressBar.isVisible = false
        }.launchIn(viewLifecycleOwner.lifecycleScope)

        viewModel.authResult.onEach { result ->
            result.onSuccess {
                snackBar("The profile image successfully changed")
            }
            result.onFailure {
                snackBar("Error in changing image")
            }
            binding.loadingProgressBar.isVisible = false
        }.launchIn(viewLifecycleOwner.lifecycleScope)
    }

    @SuppressLint("IntentReset")
    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, "Choose the image")
        startActivityForResult(chooser, 101)
    }

    private fun checkGalleryPermission() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    android.Manifest.permission.READ_MEDIA_IMAGES
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                openGallery()
            } else {
                pLauncher.launch(android.Manifest.permission.READ_MEDIA_IMAGES)
            }
        } else {
            if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                openGallery()
            } else {
                pLauncher.launch(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
            }
        }

    }

    private fun registerPermissionListener() {
        pLauncher = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) {
            if (it) {
                openGallery()
            } else {
                snackBar("Разрешение не доставлено")
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 101) {
            fetchSelectedImagesFromGallery(data)
        }
    }
}