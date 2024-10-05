package com.imax.edumeet.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.imax.edumeet.R
import com.imax.edumeet.data.models.RatingOfStudent
import com.imax.edumeet.databinding.ItemRatingBinding

class RatingOfStudentAdapter(private val list: List<RatingOfStudent>) :
    Adapter<RatingOfStudentAdapter.RatingVh>() {
    inner class RatingVh(private val binding: ItemRatingBinding) :
        ViewHolder(binding.root) {
        fun bind(item: RatingOfStudent) {
            binding.tvPlace.text = "${adapterPosition + 1}"
            binding.tvName.text = item.student.name
            binding.tvGroupName.text = item.student.group
            Glide
                .with(binding.root)
                .load(item.student.profileImage)
                .centerCrop()
                .error(R.drawable.pic_temporary_image)
                .into(binding.userImage)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RatingVh {
        return RatingVh(
            ItemRatingBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RatingVh, position: Int) {
        holder.bind(list[position])
    }

}