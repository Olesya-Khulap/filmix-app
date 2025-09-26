package com.example.filmix.presentation.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.filmix.R
import com.example.filmix.databinding.ItemImageSlideBinding

class ImageSliderAdapter(
    private val context: Context,
    private var images: List<String>,
    private val onImageClick: (String, Int) -> Unit
) : RecyclerView.Adapter<ImageSliderAdapter.ImageViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val binding = ItemImageSlideBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ImageViewHolder(binding)
    }

    override fun getItemCount(): Int = images.size

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        holder.bind(images[position], position)
    }

    fun updateImages(newImages: List<String>) {
        images = newImages
        notifyDataSetChanged()
    }

    inner class ImageViewHolder(
        private val binding: ItemImageSlideBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(imageUrl: String, position: Int) {
            val fullImageUrl = if (imageUrl.startsWith("http")) {
                imageUrl
            } else {
                "https://image.tmdb.org/t/p/w780$imageUrl"
            }

            Glide.with(context)
                .load(fullImageUrl)
                .placeholder(R.color.input_gray)
                .error(R.color.input_gray)
                .into(binding.ivSlideImage)

            binding.root.setOnClickListener {
                onImageClick(fullImageUrl, position)
            }
        }
    }
}
