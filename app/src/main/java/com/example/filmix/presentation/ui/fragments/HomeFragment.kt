package com.example.filmix.presentation.ui.fragments

import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.example.filmix.R
import com.example.filmix.databinding.FragmentHomeBinding
import com.example.filmix.domain.models.CategoryType
import com.example.filmix.domain.models.MovieItem
import com.example.filmix.presentation.adapters.BannerAdapter
import com.example.filmix.presentation.adapters.MovieCardHorizontalAdapter
import com.example.filmix.presentation.viewmodels.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>() {

    private val viewModel: HomeViewModel by viewModels()
    private lateinit var bannerAdapter: BannerAdapter
    private lateinit var popularMoviesAdapter: MovieCardHorizontalAdapter
    private lateinit var popularSeriesAdapter: MovieCardHorizontalAdapter
    private lateinit var newReleasesAdapter: MovieCardHorizontalAdapter
    private lateinit var topRatedAdapter: MovieCardHorizontalAdapter
    private var bannerHandler: Handler? = null
    private var bannerRunnable: Runnable? = null
    private val dots = mutableListOf<View>()

    companion object {
        private const val TAG = "HomeFragment"
    }

    override fun getViewBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentHomeBinding {
        return FragmentHomeBinding.inflate(inflater, container, false)
    }

    override fun initViews() {
        Log.d(TAG, "HomeFragment: Initializing views...")
        setupBanner()
        setupRecyclerViews()
        setupClickListeners()
        
        Log.d(TAG, "HomeFragment: Loading cached data if available...")
        viewModel.loadCachedDataIfAvailable()
    }

    override fun observeViewModel() {
        Log.d(TAG, "HomeFragment: Setting up ViewModel observers...")

        viewModel.homeContent.observe(viewLifecycleOwner) { result ->
            when (result) {
                is com.example.filmix.domain.usecases.base.Result.Loading -> {
                    Log.d(TAG, "HomeFragment: Loading home content...")
                    showLoading(true)
                }
                is com.example.filmix.domain.usecases.base.Result.Success -> {
                    Log.d(TAG, "HomeFragment: Home content loaded successfully")
                    showLoading(false)
                    populateContent(result.data)
                }
                is com.example.filmix.domain.usecases.base.Result.Error -> {
                    Log.e(TAG, "HomeFragment: Error loading home content: ${result.message}")
                    showLoading(false)
                }
            }
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            Log.d(TAG, "HomeFragment: Loading state changed: $isLoading")
        }
    }

    private fun showLoading(isLoading: Boolean) {
    }

    private fun populateContent(content: com.example.filmix.domain.usecases.home.GetHomeContentUseCase.HomeContent) {
        Log.d(TAG, "HomeFragment: Populating UI with content...")
        Log.d(TAG, "Banner movies: ${content.bannerMovies.size}")
        Log.d(TAG, "Popular movies: ${content.popularMovies.size}")
        Log.d(TAG, "Popular series: ${content.popularTvShows.size}")
        Log.d(TAG, "New releases: ${content.newReleases.size}")
        Log.d(TAG, "Top rated: ${content.topRated.size}")
        
        bannerAdapter.updateMovies(content.bannerMovies)
        setupDotsIndicator(content.bannerMovies.size)


        popularMoviesAdapter.updateMovies(content.popularMovies)
        popularSeriesAdapter.updateMovies(content.popularTvShows)
        newReleasesAdapter.updateMovies(content.newReleases)
        topRatedAdapter.updateMovies(content.topRated)

        Log.d(TAG, "HomeFragment: UI populated successfully")
        
        if (content.bannerMovies.isNotEmpty()) {
            startAutoBanner()
        }
    }

    private fun setupBanner() {
        Log.d(TAG, "HomeFragment: Setting up banner...")
        bannerAdapter = BannerAdapter(emptyList()) { movie ->
            navigateToMovieDetail(movie)
        }
        binding.bannerSlider.adapter = bannerAdapter
        
        setupDotsIndicator(0)

        binding.bannerSlider.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                if (dots.isNotEmpty()) {
                    val realPosition = position % dots.size
                    updateDots(realPosition)
                }
            }
        })

        Log.d(TAG, "HomeFragment: Banner setup complete")
    }

    private fun setupRecyclerViews() {
        Log.d(TAG, "📱 HomeFragment: Setting up RecyclerViews...")
        
        popularMoviesAdapter = MovieCardHorizontalAdapter(emptyList()) { movie ->
            navigateToMovieDetail(movie)
        }
        binding.rvPopularMovies.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = popularMoviesAdapter
            clipToPadding = false
            setPadding(0, 0, 16, 0)
        }
        
        popularSeriesAdapter = MovieCardHorizontalAdapter(emptyList()) { movie ->
            navigateToMovieDetail(movie)
        }
        binding.rvPopularSeries.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = popularSeriesAdapter
            clipToPadding = false
            setPadding(0, 0, 16, 0)
        }
        
        newReleasesAdapter = MovieCardHorizontalAdapter(emptyList()) { movie ->
            navigateToMovieDetail(movie)
        }
        binding.rvNewReleases.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = newReleasesAdapter
            clipToPadding = false
            setPadding(0, 0, 16, 0)
        }
        
        topRatedAdapter = MovieCardHorizontalAdapter(emptyList()) { movie ->
            navigateToMovieDetail(movie)
        }
        binding.rvTopRated.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = topRatedAdapter
            clipToPadding = false
            setPadding(0, 0, 16, 0)
        }

        Log.d(TAG, "HomeFragment: RecyclerViews setup complete")
    }

    private fun setupClickListeners() {
        Log.d(TAG, "HomeFragment: Setting up click listeners...")

        binding.btnAllMovies.setOnClickListener {
            Log.d(TAG, "All Movies clicked")
            findNavController().navigate(
                HomeFragmentDirections.actionHomeFragmentToCategoryFragment(
                    categoryType = CategoryType.POPULARMOVIES,
                    categoryTitle = "Popular Movies"
                )
            )
        }

        binding.btnAllSeries.setOnClickListener {
            Log.d(TAG, "All Series clicked")
            findNavController().navigate(
                HomeFragmentDirections.actionHomeFragmentToCategoryFragment(
                    categoryType = CategoryType.POPULARSERIES,
                    categoryTitle = "Popular Series"
                )
            )
        }

        binding.btnAllNew.setOnClickListener {
            Log.d(TAG, "All New clicked")
            findNavController().navigate(
                HomeFragmentDirections.actionHomeFragmentToCategoryFragment(
                    categoryType = CategoryType.NEWRELEASES,
                    categoryTitle = "New Releases"
                )
            )
        }

        binding.btnAllTopRated.setOnClickListener {
            Log.d(TAG, "All Top Rated clicked")
            findNavController().navigate(
                HomeFragmentDirections.actionHomeFragmentToCategoryFragment(
                    categoryType = CategoryType.TOPRATED,
                    categoryTitle = "Top Rated"
                )
            )
        }

        Log.d(TAG, "HomeFragment: Click listeners setup complete")
    }

    private fun setupDotsIndicator(size: Int) {
        binding.dotsIndicator.removeAllViews()
        dots.clear()

        if (size == 0) return

        repeat(size) { index ->
            val dot = View(context).apply {
                val dotSize = resources.getDimensionPixelSize(R.dimen.dot_size)
                val dotMargin = resources.getDimensionPixelSize(R.dimen.dot_margin)
                layoutParams = ViewGroup.MarginLayoutParams(dotSize, dotSize).apply {
                    if (index != size - 1) {
                        rightMargin = dotMargin
                    }
                }
                setBackgroundResource(
                    if (index == 0) R.drawable.dot_active else R.drawable.dot_inactive
                )
            }
            binding.dotsIndicator.addView(dot)
            dots.add(dot)
        }

        Log.d(TAG, "Created ${dots.size} dots for banner")
    }

    private fun updateDots(selectedPosition: Int) {
        dots.forEachIndexed { index, dot ->
            dot.setBackgroundResource(
                if (index == selectedPosition) R.drawable.dot_active else R.drawable.dot_inactive
            )
        }
    }

    private fun startAutoBanner() {
        if (dots.isEmpty()) return

        stopAutoBanner()

        Log.d(TAG, "Starting auto banner with ${dots.size} items")

        bannerHandler = Handler(Looper.getMainLooper())
        bannerRunnable = Runnable {
            val currentItem = binding.bannerSlider.currentItem
            val nextItem = currentItem + 1
            binding.bannerSlider.setCurrentItem(nextItem, true)
            bannerHandler?.postDelayed(bannerRunnable!!, 3000)
        }
        bannerHandler?.postDelayed(bannerRunnable!!, 3000)
    }

    private fun stopAutoBanner() {
        bannerRunnable?.let {
            bannerHandler?.removeCallbacks(it)
        }
        Log.d(TAG, "Auto banner stopped")
    }

    private fun navigateToMovieDetail(movie: MovieItem) {
        Log.d(TAG, "Navigating to movie detail: ${movie.title}")
        findNavController().navigate(
            HomeFragmentDirections.actionHomeFragmentToMovieDetailFragment(
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

    fun refreshContent() {
        Log.d(TAG, "Manual refresh requested")
        viewModel.refreshContent()
    }

    override fun onPause() {
        super.onPause()
        stopAutoBanner()
    }

    override fun onResume() {
        super.onResume()
        viewModel.homeContent.value?.let { result ->
            if (result is com.example.filmix.domain.usecases.base.Result.Success && result.data.bannerMovies.isNotEmpty()) {
                startAutoBanner()
            }
        }
    }

    override fun onDestroyView() {
        stopAutoBanner()
        super.onDestroyView()
    }
}
