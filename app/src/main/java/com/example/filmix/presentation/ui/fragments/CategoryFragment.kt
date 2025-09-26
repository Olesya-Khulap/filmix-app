package com.example.filmix.presentation.ui.fragments

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.RecyclerView
import com.example.filmix.databinding.FragmentCategoryBinding
import com.example.filmix.domain.models.MovieItem
import com.example.filmix.presentation.adapters.MovieCardAdapter
import com.example.filmix.utils.RecyclerViewUtils
import com.example.filmix.presentation.viewmodels.CategoryViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CategoryFragment : BaseFragment<FragmentCategoryBinding>() {

    private val viewModel: CategoryViewModel by viewModels()
    private val args: CategoryFragmentArgs by navArgs()

    private lateinit var movieAdapter: MovieCardAdapter
    private lateinit var moviesRecyclerView: RecyclerView

    override fun getViewBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentCategoryBinding {
        return FragmentCategoryBinding.inflate(inflater, container, false)
    }

    override fun initViews() {
        setupToolbar()
        setupRecyclerView()
        loadMovies()
    }

    override fun observeViewModel() {
        viewModel.movies.observe(viewLifecycleOwner) { result ->
            when (result) {
                is com.example.filmix.domain.usecases.base.Result.Loading -> {

                }
                is com.example.filmix.domain.usecases.base.Result.Success -> {
                    movieAdapter.updateMovies(result.data)
                }
                is com.example.filmix.domain.usecases.base.Result.Error -> {
                }
            }
        }
    }

    private fun setupToolbar() {
        binding.tvCategoryTitle.text = args.categoryTitle
        binding.btnBack.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun setupRecyclerView() {
        movieAdapter = MovieCardAdapter(emptyList()) { movie ->
            navigateToMovieDetail(movie)
        }

        moviesRecyclerView = RecyclerView(requireContext()).apply {
            id = android.view.View.generateViewId()
        }

        binding.moviesContainer.addView(
            moviesRecyclerView,
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )

        RecyclerViewUtils.setupMovieGrid(moviesRecyclerView, movieAdapter)
    }

    private fun loadMovies() {
        viewModel.loadMoviesByCategory(args.categoryType)
    }

    private fun navigateToMovieDetail(movie: MovieItem) {
        findNavController().navigate(
            CategoryFragmentDirections.actionCategoryFragmentToMovieDetailFragment(
                movieId = movie.id,
                movieTitle = movie.title,
                movieYear = movie.year,
                moviePoster = movie.posterPath,
                movieRating = movie.rating.toFloat(),
                movieBackdrop = movie.backdropPath ?: "",
                movieMediaType = movie.mediaType ?: "movie"
            )
        )
    }
}
