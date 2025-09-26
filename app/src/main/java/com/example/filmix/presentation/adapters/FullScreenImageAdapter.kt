package com.example.filmix.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DecodeFormat
import com.bumptech.glide.request.RequestOptions
import com.example.filmix.R
import com.example.filmix.databinding.ItemFullscreenImageBinding

class FullScreenImageAdapter(
    private val imageUrls: List<String>
) : RecyclerView.Adapter<FullScreenImageAdapter.FullScreenViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FullScreenViewHolder {
        val binding = ItemFullscreenImageBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return FullScreenViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FullScreenViewHolder, position: Int) {
        holder.bind(imageUrls[position])
    }

    override fun getItemCount(): Int = imageUrls.size

    inner class FullScreenViewHolder(
        private val binding: ItemFullscreenImageBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(imageUrl: String) {
            Glide.with(binding.root.context)
                .load(imageUrl)
                .apply(
                    RequestOptions()
                        .format(DecodeFormat.PREFER_ARGB_8888)
                        .placeholder(R.color.input_gray)
                        .error(R.color.input_gray)
                        .fitCenter()
                )
                .into(binding.ivFullscreenImage)
        }
    }
}
