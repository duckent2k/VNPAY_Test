package com.example.vnpay_test.adapter

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.vnpay_test.R
import com.example.vnpay_test.databinding.ItemImageBinding

class ImageAdapter(private val onItemClick: (Uri, Int) -> Unit) :
    PagingDataAdapter<Uri, ImageAdapter.ImageViewHolder>(DIFF_CALLBACK) {
    inner class ImageViewHolder(private val binding: ItemImageBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(uri: Uri, position: Int, onItemClick: (Uri, Int) -> Unit) {
            Glide.with(binding.root)
                .load(uri.toString())
                .override(200, 200)
                .into(binding.imageView)

            binding.root.setOnClickListener { onItemClick(uri, position) }
        }
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it, position, onItemClick) }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ImageViewHolder {
        val binding = ItemImageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ImageViewHolder(binding)
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Uri>() {
            override fun areItemsTheSame(oldItem: Uri, newItem: Uri) = oldItem == newItem
            override fun areContentsTheSame(oldItem: Uri, newItem: Uri) = oldItem == newItem
        }
    }
}