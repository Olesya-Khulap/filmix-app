package com.example.filmix.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.filmix.R
import com.example.filmix.databinding.ItemMovieCardBinding
import com.example.filmix.domain.models.MovieItem
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.*

class PersonFilmographyAdapter(
    private var movies: List<MovieItem>,
    private val onMovieClick: (MovieItem) -> Unit
) : RecyclerView.Adapter<PersonFilmographyAdapter.MovieViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val binding = ItemMovieCardBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return MovieViewHolder(binding)
    }

    override fun getItemCount(): Int = movies.size

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.bind(movies[position])
    }

    fun updateMovies(newMovies: List<MovieItem>) {
        movies = newMovies
        notifyDataSetChanged()
    }

    inner class MovieViewHolder(
        private val binding: ItemMovieCardBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(movie: MovieItem) {
            binding.tvMovieTitle.text = movie.title
            binding.tvMovieYear.text = movie.year

            val symbols = DecimalFormatSymbols(Locale.US)
            val decimalFormat = DecimalFormat("0.0", symbols)
            binding.tvMovieRating.text = decimalFormat.format(movie.rating)

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
                .apply(RequestOptions()
                    .transform(RoundedCorners(8))
                    .placeholder(R.color.input_gray)
                    .error(R.color.input_gray)
                )
                .into(binding.ivMoviePoster)

            binding.root.setOnClickListener {
                onMovieClick(movie)
            }
        }
    }
}
