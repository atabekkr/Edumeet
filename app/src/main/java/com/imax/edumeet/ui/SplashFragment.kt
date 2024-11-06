package com.imax.edumeet.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.imax.edumeet.R
import com.imax.edumeet.utils.LocalStorage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class SplashFragment : Fragment(R.layout.fragment_splash) {

    @Inject
    lateinit var localStorage: LocalStorage

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycleScope.launch {
            delay(1000)
            if (localStorage.isLogin) {
                findNavController().navigate(SplashFragmentDirections.actionSplashFragmentToContainerFragment())
            } else {
                findNavController().navigate(SplashFragmentDirections.actionSplashFragmentToAboutAppFragment())
            }
        }
    }
}