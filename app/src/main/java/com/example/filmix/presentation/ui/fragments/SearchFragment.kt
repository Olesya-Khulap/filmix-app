package com.example.filmix.presentation.ui.fragments

import android.content.res.ColorStateList
import android.graphics.Color
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AutoCompleteTextView
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
import com.example.filmix.databinding.FragmentSearchBinding
import com.example.filmix.domain.models.Country
import com.example.filmix.domain.models.Genre
import com.example.filmix.domain.models.MovieItem
import com.example.filmix.domain.models.SearchFilters
import com.example.filmix.presentation.adapters.CountryAdapter
import com.example.filmix.presentation.adapters.MovieCardAdapter
import com.example.filmix.utils.RecyclerViewUtils
import com.example.filmix.presentation.viewmodels.SearchViewModel
import com.example.filmix.presentation.ui.views.CustomAutoCompleteTextView
import com.google.android.flexbox.FlexboxLayout
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchFragment : BaseFragment<FragmentSearchBinding>() {

    private val viewModel: SearchViewModel by viewModels()
    private lateinit var searchAdapter: MovieCardAdapter
    private lateinit var searchRecyclerView: RecyclerView

    override fun getViewBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentSearchBinding {
        return FragmentSearchBinding.inflate(inflater, container, false)
    }

    override fun initViews() {
        setupRecyclerView()
        setupSearchInput()
        setupClickListeners()
        loadInitialContent()
    }

    override fun observeViewModel() {
        viewModel.searchResults.observe(viewLifecycleOwner) { result ->
            when (result) {
                is com.example.filmix.domain.usecases.base.Result.Success -> {
                    searchAdapter.updateMovies(result.data)
                    searchRecyclerView.visibility = View.VISIBLE
                }
                is com.example.filmix.domain.usecases.base.Result.Error -> {
                    searchAdapter.updateMovies(emptyList())
                }
                is com.example.filmix.domain.usecases.base.Result.Loading -> {
                    searchRecyclerView.visibility = View.VISIBLE
                }
            }
        }

        viewModel.isFiltered.observe(viewLifecycleOwner) { isFiltered -> // ИСПРАВЛЕНО: убрал Result
            binding.clearFiltersButton.visibility = if (isFiltered) View.VISIBLE else View.GONE
        }
    }


    private fun setupRecyclerView() {
        searchAdapter = MovieCardAdapter(emptyList()) { item ->
            navigateToDetail(item)
        }

        searchRecyclerView = RecyclerView(requireContext()).apply {
            id = View.generateViewId()
        }

        binding.searchResultsContainer.addView(
            searchRecyclerView,
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )

        RecyclerViewUtils.setupMovieGrid(searchRecyclerView, searchAdapter)
    }

    private fun setupSearchInput() {
        binding.etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                val query = s?.toString()?.trim()
                if (query.isNullOrBlank()) {
                    loadInitialContent()
                } else if (query.length >= 3) {
                    viewModel.searchMovies(query)
                }
            }
        })
    }

    private fun setupClickListeners() {
        binding.btnFilter.setOnClickListener {
            showFiltersDialog()
        }

        binding.clearFiltersButton.setOnClickListener {
            clearFilters()
        }
    }

    private fun loadInitialContent() {
        viewModel.loadAllMovies()
    }

    private fun clearFilters() {
        viewModel.clearFilters()
    }

    private fun showFiltersDialog() {
        val dialog = BottomSheetDialog(requireContext(), R.style.BottomSheetDialogTheme)
        val view = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_filter_search, null)
        dialog.setContentView(view)
        dialog.behavior.peekHeight = (resources.displayMetrics.heightPixels * 0.75).toInt()
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
        val selectedStatuses = mutableSetOf<String>()
        val selectedTypes = mutableSetOf<String>()

        viewModel.getCurrentFilters()?.let { filters ->
            selectedTypes.addAll(filters.types)
            filters.yearFrom?.let { year ->
                view.findViewById<EditText>(R.id.et_year_from).setText(year.toString())
            }
            filters.yearTo?.let { year ->
                view.findViewById<EditText>(R.id.et_year_to).setText(year.toString())
            }

            filters.minRating?.let { min -> selectedRatings.add(min.toInt()) }
            filters.maxRating?.let { max -> selectedRatings.add(max.toInt()) }

            selectedStatuses.addAll(filters.statuses)
        }

        setupType(view, selectedTypes, selectedStatuses)
        setupGenres(view, selectedGenres)
        setupCountryInput(view, selectedCountries)
        setupRating(view, selectedRatings)

        view.findViewById<LinearLayout>(R.id.btn_apply_filters).setOnClickListener {
            val yearFromText = view.findViewById<EditText>(R.id.et_year_from).text.toString()
            val yearToText = view.findViewById<EditText>(R.id.et_year_to).text.toString()

            val filters = SearchFilters(
                types = selectedTypes.toList(),
                genreIds = selectedGenres.map { it.id },
                countryIds = selectedCountries.map { it.iso_3166_1 },
                minRating = selectedRatings.minOrNull()?.toDouble(),
                maxRating = selectedRatings.maxOrNull()?.toDouble(),
                yearFrom = yearFromText.toIntOrNull(),
                yearTo = yearToText.toIntOrNull(),
                statuses = selectedStatuses.toList()
            )

            viewModel.applyFilters(filters)
            dialog.dismiss()
        }
    }

    private fun setupType(view: View, selectedTypes: MutableSet<String>, selectedStatuses: MutableSet<String>) {
        val movieCheckbox = view.findViewById<CheckBox>(R.id.cb_movie)
        val tvCheckbox = view.findViewById<CheckBox>(R.id.cb_tv)
        val statusSection = view.findViewById<LinearLayout>(R.id.status_section)
        val finishedCheckbox = view.findViewById<CheckBox>(R.id.cb_finished)
        val inProgressCheckbox = view.findViewById<CheckBox>(R.id.cb_in_progress)

        val yellowColorStateList = ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.main_yellow))
        movieCheckbox.buttonTintList = yellowColorStateList
        tvCheckbox.buttonTintList = yellowColorStateList
        finishedCheckbox.buttonTintList = yellowColorStateList
        inProgressCheckbox.buttonTintList = yellowColorStateList

        movieCheckbox.isChecked = selectedTypes.contains("movie")
        tvCheckbox.isChecked = selectedTypes.contains("tv")
        finishedCheckbox.isChecked = selectedStatuses.contains("Ended")
        inProgressCheckbox.isChecked = selectedStatuses.contains("Returning Series")

        statusSection.visibility = if (tvCheckbox.isChecked) View.VISIBLE else View.GONE

        movieCheckbox.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                selectedTypes.add("movie")
            } else {
                selectedTypes.remove("movie")
            }
        }

        tvCheckbox.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                selectedTypes.add("tv")
                statusSection.visibility = View.VISIBLE
            } else {
                selectedTypes.remove("tv")
                statusSection.visibility = View.GONE
                selectedStatuses.clear()
                finishedCheckbox.isChecked = false
                inProgressCheckbox.isChecked = false
            }
        }

        finishedCheckbox.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                selectedStatuses.add("Ended")
            } else {
                selectedStatuses.remove("Ended")
            }
        }

        inProgressCheckbox.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                selectedStatuses.add("Returning Series")
            } else {
                selectedStatuses.remove("Returning Series")
            }
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
        val input = view.findViewById<CustomAutoCompleteTextView>(R.id.et_country) // ВЕРНУТЬ CustomAutoCompleteTextView
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
                input.addTextChangedListener(object : android.text.TextWatcher {
                    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
                    override fun afterTextChanged(s: android.text.Editable?) {
                        adapter.filter.filter(s?.toString())
                    }
                })

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

        val yellowColorStateList = ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.main_yellow))
        checkbox.buttonTintList = yellowColorStateList

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

    private fun navigateToDetail(item: MovieItem) {
        findNavController().navigate(
            SearchFragmentDirections.actionSearchFragmentToMovieDetailFragment(
                movieId = item.id,
                movieTitle = item.title,
                movieYear = item.year,
                moviePoster = item.posterPath,
                movieRating = item.rating.toFloat(),
                movieBackdrop = item.backdropPath ?: "",
                movieMediaType = item.mediaType ?: "movie"
            )
        )
    }
}
