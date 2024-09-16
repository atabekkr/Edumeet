package com.imax.edumeet.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.imax.edumeet.data.models.Notification
import com.imax.edumeet.databinding.ItemNotificationBinding

class NotificationAdapter :
    ListAdapter<Notification, NotificationAdapter.NotificationVh>(MyDiffUtil) {
    inner class NotificationVh(private val binding: ItemNotificationBinding) :
        ViewHolder(binding.root) {
        fun bind() {
            val item = getItem(adapterPosition)
            binding.tvName.text = item.from.name
            binding.tvComment.text = item.feedback
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

    private object MyDiffUtil : DiffUtil.ItemCallback<Notification>() {
        override fun areItemsTheSame(
            oldItem: Notification,
            newItem: Notification,
        ): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: Notification,
            newItem: Notification,
        ): Boolean {
            return oldItem == newItem
        }

    }
}