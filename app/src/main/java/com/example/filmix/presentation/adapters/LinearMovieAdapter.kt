package com.example.filmix.presentation.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.example.filmix.R
import com.example.filmix.domain.models.MovieItem
import java.util.*

class LinearMovieAdapter(
    private val context: Context,
    private val onMovieClick: (MovieItem) -> Unit
) {

    fun populateMovies(container: LinearLayout, movies: List<MovieItem>) {
        container.removeAllViews()

        movies.forEach { movie ->
            val itemView = LayoutInflater.from(context).inflate(R.layout.item_movie_linear, container, false)

            val poster = itemView.findViewById<ImageView>(R.id.iv_movie_poster)
            val title = itemView.findViewById<TextView>(R.id.tv_movie_title)
            val year = itemView.findViewById<TextView>(R.id.tv_movie_year)
            val rating = itemView.findViewById<TextView>(R.id.tv_movie_rating)

            title.text = movie.title
            year.text = movie.year
            rating.text = String.format(Locale.US, "%.1f", movie.rating)

            // Set rating background color
            val ratingBackground = when {
                movie.rating >= 8.5 -> R.drawable.bg_rating_gold
                movie.rating >= 7.0 -> R.drawable.bg_rating_green
                movie.rating >= 5.0 -> R.drawable.bg_rating_gray
                else -> R.drawable.bg_rating_red
            }

            rating.background = ContextCompat.getDrawable(context, ratingBackground)

            val imageUrl = "https://image.tmdb.org/t/p/w342${movie.posterPath}"
            Glide.with(context)
                .load(imageUrl)
                .placeholder(R.color.input_gray)
                .error(R.color.input_gray)
                .into(poster)

            itemView.setOnClickListener {
                onMovieClick(movie)
            }

            container.addView(itemView)
        }
    }
}
