package com.coriolang.lighteducation.ui.search

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.coriolang.lighteducation.databinding.ItemSearchResultBinding
import com.coriolang.lighteducation.model.data.Direction
import com.coriolang.lighteducation.model.data.toLevel
import com.coriolang.lighteducation.model.data.toString

class DirectionAdapter(
    private val onDirectionClicked: (String) -> Unit
) : ListAdapter<Direction, DirectionAdapter.DirectionViewHolder>(DiffCallback) {

    class DirectionViewHolder(
        private val context: Context,
        private val binding: ItemSearchResultBinding,
        private val onDirectionClicked: (String) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(direction: Direction) {
            binding.textTitle.text = direction.name
            binding.textSecondary.text = direction.code
            binding.textSupporting.text = direction.level
                ?.toLevel()
                ?.toString(context)

            itemView.setOnClickListener {
                onDirectionClicked(direction.id!!)
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): DirectionViewHolder {

        return DirectionViewHolder(
            parent.context,
            ItemSearchResultBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            onDirectionClicked
        )
    }

    override fun onBindViewHolder(
        holder: DirectionViewHolder,
        position: Int
    ) {
        val direction = getItem(position)
        holder.bind(direction)
    }

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<Direction>() {

            override fun areItemsTheSame(
                oldItem: Direction,
                newItem: Direction
            ): Boolean = oldItem.id == newItem.id

            override fun areContentsTheSame(
                oldItem: Direction,
                newItem: Direction
            ): Boolean = oldItem == newItem
        }
    }
}