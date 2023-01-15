package com.coriolang.lighteducation.ui.home

import android.content.Context
import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.coriolang.lighteducation.databinding.ItemTopicBinding
import com.coriolang.lighteducation.model.data.Topic

class TopicAdapter(
    private val onTopicClicked: (String) -> Unit
) : ListAdapter<Topic, TopicAdapter.TopicViewHolder>(DiffCallback) {

    class TopicViewHolder(
        private val context: Context,
        private val binding: ItemTopicBinding,
        private val onTopicClicked: (String) -> Unit
        ) : RecyclerView.ViewHolder(binding.root) {

            fun bind(topic: Topic) {
                binding.textTitle.text = topic.title
                binding.textDate.text = DateUtils.formatDateTime(
                    context,
                    topic.timestamp ?: 0L,
                    DateUtils.FORMAT_SHOW_DATE
                )
                binding.textMessage.text = topic.text

                itemView.setOnClickListener {
                    onTopicClicked(topic.id!!)
                }
            }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TopicViewHolder {

        return TopicViewHolder(
            parent.context,
            ItemTopicBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            onTopicClicked
        )
    }

    override fun onBindViewHolder(
        holder: TopicViewHolder,
        position: Int
    ) {
        val topic = getItem(position)
        holder.bind(topic)
    }

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<Topic>() {

            override fun areItemsTheSame(
                oldItem: Topic,
                newItem: Topic
            ): Boolean = oldItem.id == newItem.id

            override fun areContentsTheSame(
                oldItem: Topic,
                newItem: Topic
            ): Boolean = oldItem == newItem
        }
    }
}