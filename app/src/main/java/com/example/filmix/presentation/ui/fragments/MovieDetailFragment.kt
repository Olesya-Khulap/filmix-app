package com.example.filmix.presentation.ui.fragments

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.example.filmix.R
import com.example.filmix.databinding.FragmentMovieDetailBinding
import com.example.filmix.domain.models.MovieItem
import com.example.filmix.presentation.adapters.CastGroupAdapter
import com.example.filmix.presentation.adapters.CrewGroupAdapter
import com.example.filmix.presentation.adapters.ImageSliderAdapter
import com.example.filmix.presentation.viewmodels.MovieDetailViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MovieDetailFragment : BaseFragment<FragmentMovieDetailBinding>() {

    private val viewModel: MovieDetailViewModel by viewModels()
    private val args: MovieDetailFragmentArgs by navArgs()

    private lateinit var imageSliderAdapter: ImageSliderAdapter
    private lateinit var castAdapter: CastGroupAdapter
    private lateinit var crewAdapter: CrewGroupAdapter

    private val dots = mutableListOf<ImageView>()
    private var currentImageUrls = mutableListOf<String>()

    override fun getViewBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentMovieDetailBinding {
        return FragmentMovieDetailBinding.inflate(inflater, container, false)
    }

    override fun initViews() {
        setupImageSlider()
        setupRecyclerViews()
        setupClickListeners()

        val movieItem = MovieItem(
            id = args.movieId,
            title = args.movieTitle,
            year = args.movieYear,
            posterPath = args.moviePoster,
            rating = args.movieRating.toDouble(),
            backdropPath = args.movieBackdrop.takeIf { it.isNotEmpty() },
            mediaType = args.movieMediaType
        )

        viewModel.loadMovieDetails(movieItem)
    }

    override fun observeViewModel() {
        viewModel.movieContent.observe(viewLifecycleOwner) { result ->
            when (result) {
                is com.example.filmix.domain.usecases.base.Result.Loading -> {
                    showLoading(true)
                }
                is com.example.filmix.domain.usecases.base.Result.Success -> {
                    showLoading(false)
                    populateMovieDetails(result.data)
                }
                is com.example.filmix.domain.usecases.base.Result.Error -> {
                    showLoading(false)
                    Toast.makeText(requireContext(), "Failed to load movie details", Toast.LENGTH_SHORT).show()
                }
            }
        }

        viewModel.authRequiredMessage.observe(viewLifecycleOwner) { message ->
            if (message.isNotEmpty()) {
                showAuthRequiredMessage(message)
                viewModel.clearAuthRequiredMessage()
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

    private fun setupImageSlider() {
        imageSliderAdapter = ImageSliderAdapter(requireContext(), emptyList()) { imageUrl, position ->
            navigateToFullScreenGallery(currentImageUrls, position)
        }
        binding.viewPagerImages.adapter = imageSliderAdapter

        binding.viewPagerImages.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                updateDots(position)
            }
        })
    }

    private fun setupRecyclerViews() {
        castAdapter = CastGroupAdapter(emptyList()) { castMember ->
            navigateToPersonDetail(castMember.id, castMember.name, castMember.profilePath ?: "")
        }
        binding.recyclerViewCast.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = castAdapter
        }

        crewAdapter = CrewGroupAdapter(emptyList()) { crewMember ->
            navigateToPersonDetail(crewMember.id, crewMember.name, crewMember.profilePath ?: "")
        }
        binding.recyclerViewCrew.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = crewAdapter
        }
    }

    private fun setupClickListeners() {
        binding.btnBack.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.btnFavourite.setOnClickListener {
            viewModel.toggleFavorite()
        }

        binding.btnWatchLater.setOnClickListener {
            viewModel.toggleWatchLater()
        }

        binding.btnViewed.setOnClickListener {
            viewModel.toggleViewed()
        }
    }

    private fun formatDuration(minutes: Int): String {
        if (minutes <= 0) return ""

        val hours = minutes / 60
        val remainingMinutes = minutes % 60

        return when {
            hours > 0 && remainingMinutes > 0 -> "${hours}h ${remainingMinutes}m"
            hours > 0 && remainingMinutes == 0 -> "${hours}h"
            hours == 0 && remainingMinutes > 0 -> "${remainingMinutes}m"
            else -> ""
        }
    }

    private fun populateMovieDetails(content: com.example.filmix.domain.usecases.detail.GetMovieDetailContentUseCase.MovieDetailContent) {
        binding.tvMovieTitle.text = content.details.title
        binding.tvYearGenres.text = "${content.details.year} • ${content.details.genres.joinToString(", ") { it.name }}"

        val isSeries = content.details.numberOfSeasons > 0

        if (isSeries) {
            binding.tvCountryDuration.text = content.details.productionCountries.joinToString(", ") { it.name }

            val seasonsText = if (content.details.numberOfSeasons == 1) "1 season" else "${content.details.numberOfSeasons} seasons"
            binding.tvSeasonStatus.text = "$seasonsText • ${content.details.status}"
            binding.tvSeasonStatus.visibility = View.VISIBLE
        } else {
            val durationText = if (content.details.runtime > 0) {
                val formattedDuration = formatDuration(content.details.runtime)
                if (formattedDuration.isNotEmpty()) " • $formattedDuration" else ""
            } else {
                ""
            }
            binding.tvCountryDuration.text = "${content.details.productionCountries.joinToString(", ") { it.name }}$durationText"

            binding.tvSeasonStatus.visibility = View.GONE
        }

        updateActionButtons(content.isFavorite, content.isInWatchLater, content.isViewed)

        val imageList = mutableListOf<String>()
        currentImageUrls.clear()

        if (args.moviePoster.isNotEmpty()) {
            val posterUrl = if (args.moviePoster.startsWith("http")) {
                args.moviePoster
            } else {
                "https://image.tmdb.org/t/p/w780${args.moviePoster}"
            }
            imageList.add(args.moviePoster)
            currentImageUrls.add(posterUrl)
        }

        content.images.backdrops.take(4).forEach { image ->
            val fullUrl = "https://image.tmdb.org/t/p/w780${image.filePath}"
            imageList.add(image.filePath)
            currentImageUrls.add(fullUrl)
        }

        imageSliderAdapter.updateImages(imageList)
        setupDotsIndicator(imageList.size)

        castAdapter.updateCast(content.credits.cast.take(15))
        crewAdapter.updateCrew(content.credits.crew.take(15))

        setupTrailerSection(content.videos.results)
    }

    private fun setupTrailerSection(videos: List<com.example.filmix.domain.models.VideoResult>) {
        val trailer = videos.firstOrNull { it.site == "YouTube" && it.type == "Trailer" }

        if (trailer != null) {
            binding.trailerSection.visibility = View.VISIBLE
            setupYouTubePlayer(trailer.key)
        } else {
            binding.trailerSection.visibility = View.GONE
        }
    }

    private fun updateActionButtons(isFavorite: Boolean, isInWatchLater: Boolean, isViewed: Boolean) {
        binding.ivFavourite.setImageResource(
            if (isFavorite) R.drawable.ic_favourite_filled else R.drawable.ic_favourite
        )

        binding.ivFavourite.setColorFilter(
            ContextCompat.getColor(
                requireContext(),
                if (isFavorite) R.color.main_orange else R.color.main_yellow
            )
        )

        binding.tvFavourite.setTextColor(
            ContextCompat.getColor(
                requireContext(),
                if (isFavorite) R.color.main_orange else R.color.main_yellow
            )
        )

        binding.ivLater.setImageResource(
            if (isInWatchLater) R.drawable.ic_later_filled else R.drawable.ic_later
        )

        binding.ivLater.setColorFilter(
            ContextCompat.getColor(
                requireContext(),
                if (isInWatchLater) R.color.main_orange else R.color.main_yellow
            )
        )

        binding.tvLater.setTextColor(
            ContextCompat.getColor(
                requireContext(),
                if (isInWatchLater) R.color.main_orange else R.color.main_yellow
            )
        )

        binding.ivViewed.setImageResource(
            if (isViewed) R.drawable.ic_viewed_filled else R.drawable.ic_viewed
        )

        binding.ivViewed.setColorFilter(
            ContextCompat.getColor(
                requireContext(),
                if (isViewed) R.color.main_orange else R.color.main_yellow
            )
        )

        binding.tvViewed.setTextColor(
            ContextCompat.getColor(
                requireContext(),
                if (isViewed) R.color.main_orange else R.color.main_yellow
            )
        )
    }

    private fun setupDotsIndicator(size: Int) {
        binding.dotsIndicator.removeAllViews()
        dots.clear()

        repeat(size) { index ->
            val dot = ImageView(context).apply {
                setImageDrawable(
                    ContextCompat.getDrawable(
                        context,
                        if (index == 0) R.drawable.dot_active else R.drawable.dot_inactive
                    )
                )

                layoutParams = LinearLayout.LayoutParams(
                    resources.getDimensionPixelSize(R.dimen.dot_size),
                    resources.getDimensionPixelSize(R.dimen.dot_size)
                ).apply {
                    if (index != size - 1) {
                        rightMargin = resources.getDimensionPixelSize(R.dimen.dot_margin)
                    }
                }
            }

            binding.dotsIndicator.addView(dot)
            dots.add(dot)
        }
    }

    private fun updateDots(selectedPosition: Int) {
        dots.forEachIndexed { index, dot ->
            dot.setImageDrawable(
                ContextCompat.getDrawable(
                    requireContext(),
                    if (index == selectedPosition) R.drawable.dot_active else R.drawable.dot_inactive
                )
            )
        }
    }

    private fun setupYouTubePlayer(videoId: String) {
        val youTubeHtml = """
            <html>
            <head>
                <meta name="viewport" content="width=device-width, initial-scale=1.0">
                <style>
                    body { margin: 0; padding: 0; background: #000; }
                    iframe { width: 100%; height: 100%; border: none; }
                </style>
            </head>
            <body>
                <iframe 
                    src="https://www.youtube.com/embed/$videoId?autoplay=0&controls=1&rel=0&showinfo=0&modestbranding=1&html5=1"
                    frameborder="0" 
                    allow="accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture" 
                    allowfullscreen>
                </iframe>
            </body>
            </html>
        """.trimIndent()

        binding.webViewTrailer.apply {
            settings.javaScriptEnabled = true
            settings.domStorageEnabled = true
            settings.loadWithOverviewMode = true
            settings.useWideViewPort = true
            loadDataWithBaseURL("https://www.youtube.com", youTubeHtml, "text/html", "utf-8", null)
        }
    }

    private fun navigateToFullScreenGallery(imageUrls: List<String>, position: Int) {
        val action = MovieDetailFragmentDirections.actionMovieDetailFragmentToFullScreenImageFragment(
            imageUrls = imageUrls.toTypedArray(),
            currentPosition = position
        )
        findNavController().navigate(action)
    }

    private fun navigateToPersonDetail(personId: Int, personName: String, personPhoto: String) {
        val action = MovieDetailFragmentDirections.actionMovieDetailFragmentToPersonDetailFragment(
            personId = personId,
            personName = personName,
            personPhoto = personPhoto
        )
        findNavController().navigate(action)
    }

    private fun showAuthRequiredMessage(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
    }
}
