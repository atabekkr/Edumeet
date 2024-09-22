package com.imax.edumeet.ui

import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.imax.edumeet.R
import com.imax.edumeet.databinding.FragmentContainterBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ContainerFragment : Fragment(R.layout.fragment_containter) {

    private val binding by viewBinding(FragmentContainterBinding::bind)
    private var navController: NavController? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupData()

        binding.fabAdd.setOnClickListener {
            binding.bottomNav.selectedItemId = R.id.homeFragment
        }

    }

    private fun setupData() {

        val navHostFragment =
            childFragmentManager.findFragmentById(R.id.fragment_container_view) as NavHostFragment

        navController = navHostFragment.navController

        binding.bottomNav.setupWithNavController(navController!!)

        //get the drawable
        val myFabSrc = resources.getDrawable(R.drawable.ic_home)

        //copy it in a new one
        val willBeWhite = myFabSrc.constantState!!.newDrawable()

        //set the color filter, you can use also Mode.SRC_ATOP
        willBeWhite.mutate().setColorFilter(Color.WHITE, PorterDuff.Mode.MULTIPLY)

        //set it to your fab button initialized before
        binding.fabAdd.setImageDrawable(willBeWhite)

    }

}