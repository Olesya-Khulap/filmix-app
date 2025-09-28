package com.example.filmix.presentation.ui.fragments

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DecodeFormat
import com.bumptech.glide.request.RequestOptions
import com.example.filmix.R
import com.example.filmix.data.local.ProfileImageManager
import com.example.filmix.databinding.FragmentProfileBinding
import com.example.filmix.domain.models.CategoryType
import com.example.filmix.domain.models.MovieItem
import com.example.filmix.presentation.adapters.MovieCardHorizontalAdapter
import com.example.filmix.presentation.viewmodels.ProfileViewModel
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment : BaseFragment<FragmentProfileBinding>() {

    private val viewModel: ProfileViewModel by viewModels()
    private lateinit var favoritesAdapter: MovieCardHorizontalAdapter
    private lateinit var watchLaterAdapter: MovieCardHorizontalAdapter
    private lateinit var viewedAdapter: MovieCardHorizontalAdapter

    private val imagePickerLauncher = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let { selectedImageUri ->
            handleSelectedImage(selectedImageUri)
        }
    }

    private val permissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            openImagePicker()
        } else {
            Toast.makeText(requireContext(), "Permission denied to access gallery", Toast.LENGTH_SHORT).show()
        }
    }

    private var fullFavorites: List<MovieItem> = emptyList()
    private var fullWatchLater: List<MovieItem> = emptyList()
    private var fullViewed: List<MovieItem> = emptyList()

    companion object {
        private const val TAG = "ProfileFragment"
    }

    override fun getViewBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentProfileBinding {
        return FragmentProfileBinding.inflate(inflater, container, false)
    }

    override fun initViews() {
        Log.d(TAG, "ProfileFragment: initViews() called")
        
        initializeAdapters()
    }

    private fun initializeAdapters() {
        Log.d(TAG, "Initializing adapters...")

        favoritesAdapter = MovieCardHorizontalAdapter(emptyList()) { movie ->
            navigateToMovieDetail(movie)
        }

        watchLaterAdapter = MovieCardHorizontalAdapter(emptyList()) { movie ->
            navigateToMovieDetail(movie)
        }

        viewedAdapter = MovieCardHorizontalAdapter(emptyList()) { movie ->
            navigateToMovieDetail(movie)
        }

        Log.d(TAG, "Adapters initialized successfully")
    }

    override fun observeViewModel() {
        Log.d(TAG, "ProfileFragment: Setting up observers")

        viewModel.userLists.observe(viewLifecycleOwner) { result ->
            when (result) {
                is com.example.filmix.domain.usecases.base.Result.Success -> {
                    Log.d(TAG, "User lists loaded successfully: ${result.data}")
                    updateUserLists(result.data)
                }
                is com.example.filmix.domain.usecases.base.Result.Error -> {
                    Log.e(TAG, "Error loading user lists: ${result.message}")
                }
                is com.example.filmix.domain.usecases.base.Result.Loading -> {
                    Log.d(TAG, "Loading user lists...")
                }
            }
        }

        viewModel.isLoggedIn.observe(viewLifecycleOwner) { isLoggedIn ->
            Log.d(TAG, "Auth state changed: isLoggedIn = $isLoggedIn")
            updateUIForAuthState(isLoggedIn)
        }

        viewModel.userName.observe(viewLifecycleOwner) { userName ->
            Log.d(TAG, "👤 User name changed: $userName")
            if (!userName.isNullOrBlank()) {
                binding.tvUsername.text = userName
            }
        }

        viewModel.profileImageSaveResult.observe(viewLifecycleOwner) { success ->
            if (success) {
                Toast.makeText(requireContext(), "Profile picture updated successfully", Toast.LENGTH_SHORT).show()
                loadProfileImage()
            } else {
                Toast.makeText(requireContext(), "Failed to update profile picture", Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.profileImageUpdated.observe(viewLifecycleOwner) { updated ->
            if (updated) {
                loadProfileImage()
            }
        }
    }

    private fun updateUIForAuthState(isLoggedIn: Boolean) {
        Log.d(TAG, "Updating UI for auth state: isLoggedIn = $isLoggedIn")

        if (isLoggedIn) {
            Log.d(TAG, "User is logged in - showing profile content")
            binding.profileContent.visibility = View.VISIBLE
            binding.authContent.visibility = View.GONE

            setupRecyclerViews()
            setupLoggedInClickListeners()

            loadProfileImage()

            Log.d(TAG, "Loading user lists...")
        } else {
            Log.d(TAG, "User not logged in - showing auth buttons")
            binding.profileContent.visibility = View.GONE
            binding.authContent.visibility = View.VISIBLE

            binding.tvUsername.text = ""

            setupAuthClickListeners()
        }
    }

    private fun setupRecyclerViews() {
        Log.d(TAG, "📱 Setting up RecyclerViews...")

        // ТОЛЬКО НАСТРАИВАЕМ RECYCLERVIEWS (адаптеры уже созданы в initViews):
        binding.recyclerFavourites.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = favoritesAdapter
        }

        binding.recyclerWatchLater.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = watchLaterAdapter
        }

        binding.recyclerViewed.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = viewedAdapter
        }
        
        if (fullFavorites.isNotEmpty() || fullWatchLater.isNotEmpty() || fullViewed.isNotEmpty()) {
            Log.d(TAG, "Applying previously loaded data to adapters")
            favoritesAdapter.updateMovies(fullFavorites.take(5))
            watchLaterAdapter.updateMovies(fullWatchLater.take(5))
            viewedAdapter.updateMovies(fullViewed.take(5))
        }

        Log.d(TAG, "RecyclerViews setup complete")
    }

    private fun setupLoggedInClickListeners() {
        Log.d(TAG, "Setting up logged in click listeners")

        binding.ivProfileAvatar.setOnClickListener {
            Log.d(TAG, "Avatar clicked")
            checkPermissionAndOpenGallery()
        }

        binding.btnFavouritesAll.setOnClickListener {
            Log.d(TAG, "Favorites All clicked")
            navigateToCategory(CategoryType.CUSTOM_FAVOURITES, "Favourites")
        }

        binding.btnWatchLaterAll.setOnClickListener {
            Log.d(TAG, "Watch Later All clicked")
            navigateToCategory(CategoryType.CUSTOM_WATCH_LATER, "Watch Later")
        }

        binding.btnViewedAll.setOnClickListener {
            Log.d(TAG, "Viewed All clicked")
            navigateToCategory(CategoryType.CUSTOM_VIEWED, "Viewed")
        }

        binding.btnLogout.setOnClickListener {
            Log.d(TAG, "Logout clicked")
            viewModel.logout()
        }
    }

    private fun setupAuthClickListeners() {
        Log.d(TAG, "Setting up auth click listeners")

        binding.btnLogin.setOnClickListener {
            Log.d(TAG, "Login clicked - navigating to auth")
            findNavController().navigate(
                ProfileFragmentDirections.actionProfileFragmentToAuthFragment()
            )
        }

        binding.btnSignup.setOnClickListener {
            Log.d(TAG, "Signup clicked - navigating to register")
            findNavController().navigate(
                ProfileFragmentDirections.actionProfileFragmentToRegisterFragment()
            )
        }
    }

    private fun checkPermissionAndOpenGallery() {
        when {
            ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED -> {
                openImagePicker()
            }
            else -> {
                permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
            }
        }
    }

    private fun openImagePicker() {
        imagePickerLauncher.launch("image/*")
    }

    private fun handleSelectedImage(imageUri: Uri) {
        Log.d(TAG, "Handling selected image: $imageUri")
        viewModel.saveProfileImage(requireContext(), imageUri)
    }

    private fun loadProfileImage() {
        Log.d(TAG, "Loading profile image...")
        if (FirebaseAuth.getInstance().currentUser == null) return

        val savedBitmap = ProfileImageManager.loadProfileImage(requireContext())

        if (savedBitmap != null) {
            Log.d(TAG, "Loading saved profile image")
            Glide.with(this)
                .load(savedBitmap)
                .apply(
                    RequestOptions()
                        .circleCrop()
                        .format(DecodeFormat.PREFER_ARGB_8888)
                        .override(400, 400)
                        .placeholder(R.drawable.ic_avatar)
                        .error(R.drawable.ic_avatar)
                )
                .into(binding.ivProfileAvatar)
        } else {
            Log.d(TAG, "Loading default avatar")
            Glide.with(this)
                .load(R.drawable.ic_avatar)
                .apply(
                    RequestOptions()
                        .circleCrop()
                        .format(DecodeFormat.PREFER_ARGB_8888)
                )
                .into(binding.ivProfileAvatar)
        }
    }

    private fun updateUserLists(lists: com.example.filmix.domain.usecases.profile.GetUserListsUseCase.UserLists) {
        Log.d(TAG, "Updating user lists:")
        Log.d(TAG, "Favorites: ${lists.favorites.size} items")
        Log.d(TAG, "Watch Later: ${lists.watchLater.size} items")
        Log.d(TAG, "Viewed: ${lists.viewed.size} items")

        fullFavorites = lists.favorites
        fullWatchLater = lists.watchLater
        fullViewed = lists.viewed
        
        favoritesAdapter.updateMovies(lists.favorites.take(5))
        watchLaterAdapter.updateMovies(lists.watchLater.take(5))
        viewedAdapter.updateMovies(lists.viewed.take(5))

        updateFavoritesSection(lists.favorites)
        updateWatchLaterSection(lists.watchLater)
        updateViewedSection(lists.viewed)

        Log.d(TAG, "User lists updated in UI")
    }

    private fun updateFavoritesSection(favorites: List<MovieItem>) {
        if (favorites.isNotEmpty()) {
            binding.recyclerFavourites.visibility = View.VISIBLE
            binding.tvFavouritesEmpty.visibility = View.GONE
            binding.btnFavouritesAll.visibility = if (favorites.size > 3) View.VISIBLE else View.GONE
        } else {
            binding.recyclerFavourites.visibility = View.GONE
            binding.tvFavouritesEmpty.visibility = View.VISIBLE
            binding.btnFavouritesAll.visibility = View.GONE
        }
    }

    private fun updateWatchLaterSection(watchLater: List<MovieItem>) {
        if (watchLater.isNotEmpty()) {
            binding.recyclerWatchLater.visibility = View.VISIBLE
            binding.tvWatchLaterEmpty.visibility = View.GONE
            binding.btnWatchLaterAll.visibility = if (watchLater.size > 3) View.VISIBLE else View.GONE
        } else {
            binding.recyclerWatchLater.visibility = View.GONE
            binding.tvWatchLaterEmpty.visibility = View.VISIBLE
            binding.btnWatchLaterAll.visibility = View.GONE
        }
    }

    private fun updateViewedSection(viewed: List<MovieItem>) {
        if (viewed.isNotEmpty()) {
            binding.recyclerViewed.visibility = View.VISIBLE
            binding.tvViewedEmpty.visibility = View.GONE
            binding.btnViewedAll.visibility = if (viewed.size > 3) View.VISIBLE else View.GONE
        } else {
            binding.recyclerViewed.visibility = View.GONE
            binding.tvViewedEmpty.visibility = View.VISIBLE
            binding.btnViewedAll.visibility = View.GONE
        }
    }

    private fun navigateToCategory(categoryType: CategoryType, categoryTitle: String) {
        Log.d(TAG, "Navigating to category: $categoryTitle")
        findNavController().navigate(
            ProfileFragmentDirections.actionProfileFragmentToCategoryFragment(
                categoryType = categoryType,
                categoryTitle = categoryTitle
            )
        )
    }

    private fun navigateToMovieDetail(movie: MovieItem) {
        Log.d(TAG, "Navigating to movie detail: ${movie.title}")
        findNavController().navigate(
            ProfileFragmentDirections.actionProfileFragmentToMovieDetailFragment(
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
