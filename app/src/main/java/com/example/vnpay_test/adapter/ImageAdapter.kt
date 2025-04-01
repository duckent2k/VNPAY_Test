package com.example.vnpay_test.adapter

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.vnpay_test.ImageCache
import com.example.vnpay_test.R
import com.example.vnpay_test.databinding.ItemImageBinding
import com.example.vnpay_test.utils.ImageUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ImageAdapter(private val onItemClick: (Uri, Int) -> Unit) :
    PagingDataAdapter<Uri, ImageAdapter.ImageViewHolder>(DIFF_CALLBACK) {

    private val imageCache = ImageCache()

    inner class ImageViewHolder(private val binding: ItemImageBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(uri: Uri, position: Int, onItemClick: (Uri, Int) -> Unit) {
            val uri = getItem(position) ?: return
            binding.imageView.setTag(R.id.imageView, uri.toString())

            val cacheBitmap = imageCache.getBitmapFromCache(uri.toString())
            if (cacheBitmap != null) {
                binding.imageView.setImageBitmap(cacheBitmap)
            } else {
                binding.imageView.setImageBitmap(null)
                CoroutineScope(Dispatchers.IO).launch {
                    val bitmap = ImageUtils.decodeSampleBitmapFromUri(
                        binding.imageView.context.contentResolver, uri, 200, 200
                    )
                    bitmap?.let {
                        imageCache.addBitmapToCache(uri.toString(), it)
                        withContext(Dispatchers.Main){
                            if (binding.imageView.getTag(R.id.imageView) == uri.toString()) {
                                binding.imageView.setImageBitmap(it)
                            }
                        }
                    }
                }
            }

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