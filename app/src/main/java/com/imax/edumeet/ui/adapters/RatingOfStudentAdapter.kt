package com.imax.edumeet.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.imax.edumeet.data.models.RatingOfStudent
import com.imax.edumeet.databinding.ItemRatingBinding

class RatingOfStudentAdapter :
    ListAdapter<RatingOfStudent, RatingOfStudentAdapter.RatingVh>(MyDiffUtil) {
    inner class RatingVh(private val binding: ItemRatingBinding) :
        ViewHolder(binding.root) {
        fun bind() {
            val item = getItem(adapterPosition)
            binding.tvPlace.text = "${adapterPosition + 1}"
            binding.tvName.text = item.student.name
            binding.tvGroupName.text = item.student.group
        }
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
        holder.bind()
    }

    private object MyDiffUtil : DiffUtil.ItemCallback<RatingOfStudent>() {
        override fun areItemsTheSame(
            oldItem: RatingOfStudent,
            newItem: RatingOfStudent,
        ): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: RatingOfStudent,
            newItem: RatingOfStudent,
        ): Boolean {
            return oldItem == newItem
        }

    }
}