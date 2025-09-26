package com.example.filmix.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.filmix.R
import com.example.filmix.databinding.ItemBannerSlideBinding
import com.example.filmix.domain.models.MovieItem

class BannerAdapter(
    private var movies: List<MovieItem>,
    private val onMovieClick: (MovieItem) -> Unit
) : RecyclerView.Adapter<BannerAdapter.BannerViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BannerViewHolder {
        val binding = ItemBannerSlideBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return BannerViewHolder(binding)
    }

    override fun getItemCount(): Int = if (movies.isEmpty()) 0 else Integer.MAX_VALUE

    override fun onBindViewHolder(holder: BannerViewHolder, position: Int) {
        if (movies.isNotEmpty()) {
            val movie = movies[position % movies.size]
            holder.bind(movie)
        }
    }

    fun updateMovies(newMovies: List<MovieItem>) {
        movies = newMovies
        notifyDataSetChanged()
    }

    inner class BannerViewHolder(
        private val binding: ItemBannerSlideBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(movie: MovieItem) {
            binding.tvMovieTitle.text = movie.title

            val imageUrl = if (!movie.backdropPath.isNullOrEmpty()) {
                "https://image.tmdb.org/t/p/w780${movie.backdropPath}"
            } else {
                "https://image.tmdb.org/t/p/w780${movie.posterPath}"
            }

            Glide.with(binding.root.context)
                .load(imageUrl)
                .placeholder(R.color.input_gray)
                .error(R.color.input_gray)
                .centerCrop()
                .into(binding.ivMoviePoster)

            binding.root.setOnClickListener {
                onMovieClick(movie)
            }
        }
    }
}
