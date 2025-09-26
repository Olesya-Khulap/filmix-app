package com.example.filmix.presentation.ui.fragments

import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.filmix.R
import com.example.filmix.databinding.FragmentFilmsBinding
import com.example.filmix.domain.models.Country
import com.example.filmix.domain.models.Genre
import com.example.filmix.domain.models.MovieFilters
import com.example.filmix.domain.models.MovieItem
import com.example.filmix.presentation.adapters.CountryAdapter
import com.example.filmix.presentation.adapters.MovieCardAdapter
import com.example.filmix.utils.RecyclerViewUtils
import com.example.filmix.presentation.viewmodels.FilmsViewModel
import com.example.filmix.presentation.ui.views.CustomAutoCompleteTextView
import com.google.android.flexbox.FlexboxLayout
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FilmsFragment : BaseFragment<FragmentFilmsBinding>() {

    private val viewModel: FilmsViewModel by viewModels()
    private lateinit var moviesAdapter: MovieCardAdapter
    private lateinit var moviesRecyclerView: RecyclerView

    override fun getViewBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentFilmsBinding {
        return FragmentFilmsBinding.inflate(inflater, container, false)
    }

    override fun initViews() {
        setupRecyclerView()
        setupClickListeners()
        loadMovies()
    }

    override fun observeViewModel() {
        viewModel.movies.observe(viewLifecycleOwner) { result ->
            when (result) {
                is com.example.filmix.domain.usecases.base.Result.Success -> {
                    moviesAdapter.updateMovies(result.data)
                }
                is com.example.filmix.domain.usecases.base.Result.Error -> {
                }
                is com.example.filmix.domain.usecases.base.Result.Loading -> {
                }
            }
        }

        viewModel.isFiltered.observe(viewLifecycleOwner) { isFiltered ->
            binding.clearFiltersButton.visibility = if (isFiltered) View.VISIBLE else View.GONE
        }
    }

    private fun setupRecyclerView() {
        moviesAdapter = MovieCardAdapter(emptyList()) { movie ->
            navigateToMovieDetail(movie)
        }

        moviesRecyclerView = RecyclerView(requireContext()).apply {
            id = View.generateViewId()
        }

        binding.moviesContainer.addView(
            moviesRecyclerView,
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )

        RecyclerViewUtils.setupMovieGrid(moviesRecyclerView, moviesAdapter)
    }

    private fun setupClickListeners() {
        binding.filtersButton.setOnClickListener {
            showFiltersDialog()
        }

        binding.clearFiltersButton.setOnClickListener {
            clearFilters()
        }
    }

    private fun loadMovies() {
        viewModel.loadMovies()
    }

    private fun clearFilters() {
        viewModel.clearFilters()
    }

    private fun showFiltersDialog() {
        val dialog = BottomSheetDialog(requireContext(), R.style.BottomSheetDialogTheme)
        val view = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_filters_films, null)
        dialog.setContentView(view)
        dialog.behavior.peekHeight = (resources.displayMetrics.heightPixels * 0.65).toInt()
        dialog.behavior.state = BottomSheetBehavior.STATE_EXPANDED
        dialog.window?.statusBarColor = Color.parseColor("#80000000")
        dialog.window?.navigationBarColor = Color.parseColor("#80000000")
        setupFiltersDialog(view, dialog)
        dialog.show()
    }

    private fun setupFiltersDialog(view: View, dialog: BottomSheetDialog) {
        view.findViewById<ImageView>(R.id.btn_close).setOnClickListener { dialog.dismiss() }

        val selectedGenres = mutableSetOf<Genre>()
        val selectedCountries = mutableSetOf<Country>()
        val selectedRatings = mutableSetOf<Int>()

        viewModel.getCurrentFilters()?.let { filters ->
            filters.yearFrom?.let { year ->
                view.findViewById<EditText>(R.id.et_year_from).setText(year.toString())
            }
            filters.yearTo?.let { year ->
                view.findViewById<EditText>(R.id.et_year_to).setText(year.toString())
            }

            filters.minRating?.let { min -> selectedRatings.add(min.toInt()) }
            filters.maxRating?.let { max -> selectedRatings.add(max.toInt()) }
        }

        setupGenres(view, selectedGenres)
        setupCountryInput(view, selectedCountries)
        setupRating(view, selectedRatings)

        view.findViewById<LinearLayout>(R.id.btn_apply_filters).setOnClickListener {
            val yearFromText = view.findViewById<EditText>(R.id.et_year_from).text.toString()
            val yearToText = view.findViewById<EditText>(R.id.et_year_to).text.toString()

            val filters = MovieFilters(
                genreIds = selectedGenres.map { it.id },
                countryIds = selectedCountries.map { it.iso_3166_1 },
                minRating = selectedRatings.minOrNull()?.toDouble(),
                maxRating = selectedRatings.maxOrNull()?.toDouble(),
                yearFrom = yearFromText.toIntOrNull(),
                yearTo = yearToText.toIntOrNull()
            )

            viewModel.applyFilters(filters)
            dialog.dismiss()
        }
    }

    private fun setupGenres(view: View, selectedGenres: MutableSet<Genre>) {
        val container = view.findViewById<FlexboxLayout>(R.id.genres_container)
        val more = view.findViewById<TextView>(R.id.btn_more_genres)

        viewModel.loadGenres()
        viewModel.genres.observe(viewLifecycleOwner) { result ->
            if (result is com.example.filmix.domain.usecases.base.Result.Success) {
                val genres = result.data

                viewModel.getCurrentFilters()?.genreIds?.let { savedGenreIds ->
                    val savedGenres = genres.filter { it.id in savedGenreIds }
                    selectedGenres.addAll(savedGenres)
                }

                var showAll = false
                fun refresh() {
                    container.removeAllViews()
                    val list = if (showAll) genres else genres.take(6)
                    list.forEach { g -> container.addView(createGenreChip(g, selectedGenres)) }
                    more.text = if (showAll) "Less..." else "More..."
                }
                refresh()
                more.setOnClickListener {
                    showAll = !showAll
                    refresh()
                }
            }
        }
    }

    private fun createGenreChip(genre: Genre, selectedGenres: MutableSet<Genre>): TextView {
        val chip = TextView(requireContext()).apply {
            text = genre.name
            background = ContextCompat.getDrawable(requireContext(), R.drawable.bg_genre_chip)
            textSize = 14f
            typeface = ResourcesCompat.getFont(requireContext(), R.font.montserrat_medium)
            setPadding(32, 24, 32, 24)
            isClickable = true
            isFocusable = true
        }

        val isInitiallySelected = selectedGenres.contains(genre)
        chip.isSelected = isInitiallySelected
        chip.setTextColor(
            ContextCompat.getColor(
                requireContext(),
                if (isInitiallySelected) R.color.main_dark else R.color.white
            )
        )

        val params = FlexboxLayout.LayoutParams(
            FlexboxLayout.LayoutParams.WRAP_CONTENT,
            FlexboxLayout.LayoutParams.WRAP_CONTENT
        )
        params.setMargins(0, 0, 24, 16)
        chip.layoutParams = params

        chip.setOnClickListener {
            chip.isSelected = !chip.isSelected
            chip.setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    if (chip.isSelected) R.color.main_dark else R.color.white
                )
            )

            if (chip.isSelected) {
                selectedGenres.add(genre)
            } else {
                selectedGenres.remove(genre)
            }
        }
        return chip
    }

    private fun setupCountryInput(view: View, selectedCountries: MutableSet<Country>) {
        val input = view.findViewById<CustomAutoCompleteTextView>(R.id.et_country)
        val container = view.findViewById<FlexboxLayout>(R.id.selected_countries_container)

        viewModel.loadCountries()
        viewModel.countries.observe(viewLifecycleOwner) { result ->
            if (result is com.example.filmix.domain.usecases.base.Result.Success) {
                val countries = result.data
                val selectedCountryNames = mutableSetOf<String>()

                viewModel.getCurrentFilters()?.countryIds?.let { savedCountryIds ->
                    val savedCountries = countries.filter { it.iso_3166_1 in savedCountryIds }
                    selectedCountries.addAll(savedCountries)
                    selectedCountryNames.addAll(savedCountries.map { it.english_name })

                    savedCountries.forEach { country ->
                        addSelectedCountryChip(container, country, selectedCountries, null)
                    }
                }

                val adapter = CountryAdapter(requireContext(), countries, selectedCountryNames)
                input.setAdapter(adapter)
                input.threshold = 1

                input.setOnItemClickListener { _, _, position, _ ->
                    val country = adapter.getItem(position)

                    val nowSelected = adapter.toggleCountry(country.english_name)

                    if (nowSelected) {
                        selectedCountries.add(country)
                        addSelectedCountryChip(container, country, selectedCountries, adapter)
                    } else {
                        selectedCountries.remove(country)
                        removeSelectedCountryChip(container, country.english_name)
                    }

                    input.setText("")
                }

                input.setOnFocusChangeListener { _, hasFocus ->
                    if (hasFocus) {
                        input.showDropDown()
                    }
                }

                input.setOnClickListener {
                    input.showDropDown()
                }
            }
        }
    }

    private fun addSelectedCountryChip(
        container: FlexboxLayout,
        country: Country,
        selectedCountries: MutableSet<Country>,
        adapter: CountryAdapter?
    ) {
        for (i in 0 until container.childCount) {
            val existingChip = container.getChildAt(i)
            val existingName = existingChip.findViewById<TextView>(R.id.tv_country_name)?.text?.toString()
            if (existingName == country.english_name) {
                return
            }
        }

        val chip = LayoutInflater.from(requireContext()).inflate(R.layout.item_selected_country_chip, container, false)
        chip.findViewById<TextView>(R.id.tv_country_name).text = country.english_name

        chip.findViewById<ImageView>(R.id.iv_remove).setOnClickListener {
            selectedCountries.remove(country)
            container.removeView(chip)
            adapter?.toggleCountry(country.english_name)
        }

        val params = FlexboxLayout.LayoutParams(
            FlexboxLayout.LayoutParams.WRAP_CONTENT,
            FlexboxLayout.LayoutParams.WRAP_CONTENT
        )
        params.setMargins(0, 0, 8, 8)
        chip.layoutParams = params

        container.addView(chip)
    }

    private fun removeSelectedCountryChip(container: FlexboxLayout, countryName: String) {
        for (i in 0 until container.childCount) {
            val child = container.getChildAt(i)
            val name = child.findViewById<TextView>(R.id.tv_country_name)?.text?.toString()
            if (name == countryName) {
                container.removeView(child)
                break
            }
        }
    }

    private fun setupRating(view: View, selectedRatings: MutableSet<Int>) {
        val container = view.findViewById<LinearLayout>(R.id.rating_container)
        val more = view.findViewById<TextView>(R.id.btn_more_ratings)

        viewModel.getCurrentFilters()?.let { filters ->
            filters.minRating?.let { min -> selectedRatings.add(min.toInt()) }
            filters.maxRating?.let { max -> selectedRatings.add(max.toInt()) }
        }

        var showAll = selectedRatings.any { it > 5 }

        fun refresh() {
            container.removeAllViews()
            val list = if (showAll) (1..10).toList() else (1..5).toList()
            list.forEach { r -> container.addView(createRatingCheckbox(r, selectedRatings)) }
            more.text = if (showAll) "Less..." else "More..."
        }

        refresh()

        more.setOnClickListener {
            showAll = !showAll
            refresh()
        }
    }

    private fun createRatingCheckbox(r: Int, selectedRatings: MutableSet<Int>): CheckBox {
        val checkbox = CheckBox(requireContext())
        checkbox.text = r.toString()
        checkbox.setTextColor(ContextCompat.getColor(requireContext(), R.color.main_light))
        checkbox.buttonTintList = ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.main_yellow))

        checkbox.isChecked = selectedRatings.contains(r)

        val params = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        params.setMargins(0, 0, 0, 8)
        checkbox.layoutParams = params

        checkbox.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                selectedRatings.add(r)
            } else {
                selectedRatings.remove(r)
            }
        }

        return checkbox
    }

    private fun navigateToMovieDetail(movie: MovieItem) {
        findNavController().navigate(
            FilmsFragmentDirections.actionFilmsFragmentToMovieDetailFragment(
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
