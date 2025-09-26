package com.example.filmix.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.filmix.R
import com.example.filmix.databinding.ItemMovieCardBinding
import com.example.filmix.domain.models.MovieItem
import java.util.Locale

class GridMovieAdapter(
    private var movies: List<MovieItem>,
    private val onMovieClick: (MovieItem) -> Unit
) : RecyclerView.Adapter<GridMovieAdapter.GridMovieViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GridMovieViewHolder {
        val binding = ItemMovieCardBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return GridMovieViewHolder(binding)
    }

    override fun getItemCount(): Int = movies.size

    override fun onBindViewHolder(holder: GridMovieViewHolder, position: Int) {
        holder.bind(movies[position])
    }

    fun updateMovies(newMovies: List<MovieItem>) {
        movies = newMovies
        notifyDataSetChanged()
    }

    inner class GridMovieViewHolder(
        private val binding: ItemMovieCardBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(movie: MovieItem) {
            binding.tvMovieTitle.text = movie.title
            binding.tvMovieYear.text = movie.year
            binding.tvMovieRating.text = String.format(Locale.US, "%.1f", movie.rating)

            val ratingBackground = when {
                movie.rating >= 8.5 -> R.drawable.bg_rating_gold
                movie.rating >= 7.0 -> R.drawable.bg_rating_green
                movie.rating >= 5.0 -> R.drawable.bg_rating_gray
                else -> R.drawable.bg_rating_red
            }

            binding.tvMovieRating.background = ContextCompat.getDrawable(binding.root.context, ratingBackground)

            val imageUrl = "https://image.tmdb.org/t/p/w342${movie.posterPath}"
            Glide.with(binding.root.context)
                .load(imageUrl)
                .placeholder(R.color.input_gray)
                .error(R.color.input_gray)
                .into(binding.ivMoviePoster)

            binding.root.setOnClickListener {
                onMovieClick(movie)
            }
        }
    }
}
