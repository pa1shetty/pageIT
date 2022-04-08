package com.example.pageit.ui.pageDetailsScreen.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.pageit.data.database.module.FeedModule
import com.example.pageit.databinding.FeedItemBinding
import com.example.pageit.utils.DateMethods.convertFbDate

class FeedListAdapter : ListAdapter<FeedModule, FeedListAdapter.FeedViewHolder>(DiffCallback) {
    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<FeedModule>() {
            override fun areItemsTheSame(oldItem: FeedModule, newItem: FeedModule): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: FeedModule, newItem: FeedModule): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeedViewHolder {
        return FeedViewHolder(
            FeedItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: FeedViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class FeedViewHolder(
        private var binding: FeedItemBinding,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(feedModule: FeedModule) {
            feedModule.message?.let { message ->
                binding.feedMessage.text = message
            }
            feedModule.story?.let { story ->
                binding.feedMessage.text = story
            }
            binding.feedTime.text = convertFbDate(feedModule.createdTime)
        }


    }
}
