package com.imax.edumeet.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.imax.edumeet.databinding.ItemNotificationBinding
import com.imax.edumeet.models.NotificationData

class NotificationAdapter :
    ListAdapter<NotificationData, NotificationAdapter.NotificationVh>(MyDiffUtil) {
    inner class NotificationVh(private val binding: ItemNotificationBinding) :
        ViewHolder(binding.root) {
        fun bind() {
            val item = getItem(adapterPosition)
            binding.tvName.text = item.name
            binding.tvComment.text = item.comment
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationVh {
        return NotificationVh(
            ItemNotificationBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: NotificationVh, position: Int) {
        holder.bind()
    }

    private object MyDiffUtil : DiffUtil.ItemCallback<NotificationData>() {
        override fun areItemsTheSame(
            oldItem: NotificationData,
            newItem: NotificationData,
        ): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: NotificationData,
            newItem: NotificationData,
        ): Boolean {
            return oldItem == newItem
        }

    }
}