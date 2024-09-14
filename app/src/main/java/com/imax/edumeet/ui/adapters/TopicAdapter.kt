package com.imax.edumeet.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.imax.edumeet.databinding.ItemTopicBinding
import com.imax.edumeet.models.Topic
import com.imax.edumeet.utils.getResourceId

class TopicAdapter : ListAdapter<Topic, TopicAdapter.TopicVh>(MyDiffUtil) {
    inner class TopicVh(private val binding: ItemTopicBinding) : ViewHolder(binding.root) {
        fun bind() {
            val topic = getItem(adapterPosition)
            binding.tvTitle.text = topic.name
            val context = binding.root.context
            val resourceId = context.getResourceId(topic.image)
            binding.ivPic.setImageResource(resourceId)

            binding.root.setOnClickListener {
                onItemClick.invoke(topic.id)
            }
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

    private var onItemClick: (topicId: Int) -> Unit = {}
    fun setOnItemClickListener(aa: (topicId: Int) -> Unit) {
        onItemClick = aa
    }

    private object MyDiffUtil : DiffUtil.ItemCallback<Topic>() {
        override fun areItemsTheSame(oldItem: Topic, newItem: Topic): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Topic, newItem: Topic): Boolean {
            return oldItem == newItem
        }

    }
}