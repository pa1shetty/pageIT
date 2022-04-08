package com.example.pageit.ui.homescreen.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.pageit.data.database.module.PageModule

import com.example.pageit.databinding.PageItemBinding

class PageListAdapter(
    private val context: Context,
    private val onItemClicked: (PageModule) -> Unit
) : ListAdapter<PageModule, PageListAdapter.PageViewHolder>(DiffCallback) {
    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<PageModule>() {
            override fun areItemsTheSame(oldItem: PageModule, newItem: PageModule): Boolean {
                return oldItem.page_id == newItem.page_id
            }

            override fun areContentsTheSame(oldItem: PageModule, newItem: PageModule): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PageViewHolder {
        return PageViewHolder(
            PageItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ), onItemClicked
        )
    }

    override fun onBindViewHolder(holder: PageViewHolder, position: Int) {
        holder.bind(getItem(position), context)
    }

    class PageViewHolder(
        private var binding: PageItemBinding,
        private val onItemClicked: (PageModule) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(pageModule: PageModule, context: Context) {
            binding.tvPageName.text = pageModule.name
            binding.tvPageCategory.text = pageModule.category
            Glide.with(context)
                .load(pageModule.picture)
                .circleCrop()
                .into(binding.ivPagePicture)
            binding.cvPage.setOnClickListener {
                onItemClicked(pageModule)
            }
        }


    }
}
