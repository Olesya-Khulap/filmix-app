package com.example.filmix.presentation.ui.fragments

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.filmix.R
import com.example.filmix.databinding.FragmentPersonDetailBinding
import com.example.filmix.domain.models.MovieItem
import com.example.filmix.presentation.adapters.MovieCardHorizontalAdapter
import com.example.filmix.presentation.viewmodels.PersonDetailViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PersonDetailFragment : BaseFragment<FragmentPersonDetailBinding>() {

    private val viewModel: PersonDetailViewModel by viewModels()
    private val args: PersonDetailFragmentArgs by navArgs()

    private lateinit var filmographyAdapter: MovieCardHorizontalAdapter

    override fun getViewBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentPersonDetailBinding {
        return FragmentPersonDetailBinding.inflate(inflater, container, false)
    }

    override fun initViews() {
        setupClickListeners()
        setupRecyclerView()
        loadPersonDetails()
    }

    override fun observeViewModel() {
        viewModel.personDetails.observe(viewLifecycleOwner) { result ->
            when (result) {
                is com.example.filmix.domain.usecases.base.Result.Loading -> {
                    showLoading(true)
                }
                is com.example.filmix.domain.usecases.base.Result.Success -> {
                    showLoading(false)
                    populatePersonData(result.data)
                }
                is com.example.filmix.domain.usecases.base.Result.Error -> {
                    showLoading(false)
                    Toast.makeText(requireContext(), "Failed to load person details", Toast.LENGTH_SHORT).show()
                }
            }
        }

        viewModel.filmography.observe(viewLifecycleOwner) { result ->
            when (result) {
                is com.example.filmix.domain.usecases.base.Result.Success -> {
                    filmographyAdapter.updateMovies(result.data)
                }
                is com.example.filmix.domain.usecases.base.Result.Error -> {
                    // Handle filmography error quietly
                }
                else -> {}
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.loadingProgressBar.visibility = View.VISIBLE
            binding.contentLayout.visibility = View.GONE
        } else {
            binding.loadingProgressBar.visibility = View.GONE
            binding.contentLayout.visibility = View.VISIBLE
        }
    }

    private fun setupClickListeners() {
        binding.btnBack.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun setupRecyclerView() {
        filmographyAdapter = MovieCardHorizontalAdapter(emptyList()) { movie ->
            navigateToMovieDetail(movie)
        }

        binding.recyclerMovies.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = filmographyAdapter
            clipToPadding = false
            setPadding(0, 0, 16, 0)
        }
    }

    private fun loadPersonDetails() {
        viewModel.loadPersonDetails(args.personId)
    }

    private fun populatePersonData(person: com.example.filmix.domain.models.PersonDetails) {
        binding.tvPersonName.text = person.name

        val details = buildString {
            if (!person.birthday.isNullOrEmpty()) {
                append(person.birthday)
            }
            if (!person.placeOfBirth.isNullOrEmpty()) {
                if (isNotEmpty()) append(" • ")
                append(person.placeOfBirth)
            }
            if (isEmpty()) append("Details not available")
        }
        binding.tvPersonDetails.text = details

        binding.tvPersonBiography.text = if (person.biography.isNullOrEmpty()) {
            "Biography not available"
        } else {
            person.biography
        }

        val photoUrl = if (!person.profilePath.isNullOrEmpty()) {
            "https://image.tmdb.org/t/p/w500${person.profilePath}"
        } else null

        Glide.with(this)
            .load(photoUrl)
            .apply(RequestOptions()
                .transform(RoundedCorners(16))
                .placeholder(R.drawable.ic_person_placeholder)
                .error(R.drawable.ic_person_placeholder)
            )
            .into(binding.ivPersonPhoto)
    }

    private fun navigateToMovieDetail(movie: MovieItem) {
        findNavController().navigate(
            PersonDetailFragmentDirections.actionPersonDetailFragmentToMovieDetailFragment(
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
