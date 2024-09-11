package com.imax.edumeet.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.imax.edumeet.R
import com.imax.edumeet.databinding.ItemAboutAppSliderBinding
import com.imax.edumeet.models.AboutAppData

class AboutAppSliderAdapter(private val texts: List<AboutAppData>) :
    RecyclerView.Adapter<AboutAppSliderAdapter.Vh>() {

    inner class Vh(private val binding: ItemAboutAppSliderBinding) : ViewHolder(binding.root) {
        fun bind(data: AboutAppData) {
            when (data.id) {
                1 -> {
                    binding.ivPic.setImageResource(R.drawable.pic_illustration_1)
                }

                2 -> {
                    binding.ivPic.setImageResource(R.drawable.pic_illustration_2)
                }

                3 -> {
                    binding.ivPic.setImageResource(R.drawable.pic_illustration_3)
                }
            }
            binding.tvTitle.text = data.title
            binding.tvAboutApp.text = data.description
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Vh {
        return Vh(
            ItemAboutAppSliderBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: Vh, position: Int) {
        holder.bind(texts[position])
    }

    override fun getItemCount(): Int {
        return texts.size
    }

}