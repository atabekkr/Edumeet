package com.imax.edumeet.ui

import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.imax.edumeet.R
import com.imax.edumeet.databinding.FragmentMainBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainFragment : Fragment(R.layout.fragment_main) {

    private val binding by viewBinding(FragmentMainBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.cardReading.setOnClickListener { findNavController().navigate(MainFragmentDirections.actionMainFragmentToTopicsFragment()) }


        //get the drawable
        val myFabSrc = resources.getDrawable(R.drawable.ic_home)

        //copy it in a new one
        val willBeWhite = myFabSrc.constantState!!.newDrawable()

        //set the color filter, you can use also Mode.SRC_ATOP
        willBeWhite.mutate().setColorFilter(Color.WHITE, PorterDuff.Mode.MULTIPLY)

        //set it to your fab button initialized before
        binding.fab.setImageDrawable(willBeWhite)

        binding.bottomNavigationView.menu.findItem(R.id.home).isChecked = true

    }
}