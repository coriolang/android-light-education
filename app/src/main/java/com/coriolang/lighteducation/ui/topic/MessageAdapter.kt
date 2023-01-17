package com.coriolang.lighteducation.ui.topic

import android.content.Context
import android.graphics.Color
import android.text.format.DateUtils
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.coriolang.lighteducation.databinding.ItemMessageBinding

class MessageAdapter(
    private val messageOwnerId: String
) : ListAdapter<MessageWithUser, MessageAdapter.MessageViewHolder>(DiffCallback) {

    class MessageViewHolder(
        private val context: Context,
        private val binding: ItemMessageBinding,
        private val messageOwnerId: String
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(message: MessageWithUser) {
            binding.textUsername.text = message.username
            if (message.expert) {
                binding.imageVerified
                    .visibility = View.VISIBLE
            }

            binding.textMessage.text = message.text
            binding.textTimestamp.text = DateUtils.formatDateTime(
                context,
                message.timestamp,
                DateUtils.FORMAT_SHOW_DATE
            )

            if (message.userId == messageOwnerId) {
                binding.root.gravity = Gravity.END
                binding.cardMessage.setCardBackgroundColor(
                    Color.parseColor("#7cb342")
                )
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MessageViewHolder {

        return MessageViewHolder(
            parent.context,
            ItemMessageBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            messageOwnerId
        )
    }

    override fun onBindViewHolder(
        holder: MessageViewHolder,
        position: Int
    ) {
        val message = getItem(position)
        holder.bind(message)
    }

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<MessageWithUser>() {

            override fun areItemsTheSame(
                oldItem: MessageWithUser,
                newItem: MessageWithUser
            ): Boolean = oldItem.id == newItem.id

            override fun areContentsTheSame(
                oldItem: MessageWithUser,
                newItem: MessageWithUser
            ): Boolean = oldItem == newItem
        }
    }
}