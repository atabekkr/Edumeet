package com.imax.edumeet.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.imax.edumeet.databinding.ItemTopicBinding
import com.imax.edumeet.models.TopicData

class TopicAdapter : ListAdapter<TopicData, TopicAdapter.TopicVh>(MyDiffUtil) {
    inner class TopicVh(private val binding: ItemTopicBinding) : ViewHolder(binding.root) {
        fun bind() {
            val item = getItem(adapterPosition)
            binding.tvTitle.text = item.title
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TopicVh {
        return TopicVh(
            ItemTopicBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: TopicVh, position: Int) {
        holder.bind()
    }

    private object MyDiffUtil : DiffUtil.ItemCallback<TopicData>() {
        override fun areItemsTheSame(oldItem: TopicData, newItem: TopicData): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: TopicData, newItem: TopicData): Boolean {
            return oldItem == newItem
        }

    }
}