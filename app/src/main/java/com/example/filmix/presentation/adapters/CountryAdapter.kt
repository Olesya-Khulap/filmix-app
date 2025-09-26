package com.example.filmix.presentation.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.example.filmix.R
import com.example.filmix.domain.models.Country

class CountryAdapter(
    private val context: Context,
    private val allCountries: List<Country>,
    private val selectedCountryNames: MutableSet<String>
) : BaseAdapter(), Filterable {

    private var filteredCountries = allCountries

    override fun getCount(): Int = filteredCountries.size

    override fun getItem(position: Int): Country = filteredCountries[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = convertView ?: LayoutInflater.from(context)
            .inflate(R.layout.item_country_dropdown, parent, false)

        val country = getItem(position)
        val nameTextView = view.findViewById<TextView>(R.id.tv_country_name)
        val checkImageView = view.findViewById<ImageView>(R.id.iv_check)

        nameTextView.text = country.english_name

        if (selectedCountryNames.contains(country.english_name)) {
            checkImageView.visibility = View.VISIBLE
            checkImageView.setImageResource(R.drawable.ic_check)
            checkImageView.setColorFilter(ContextCompat.getColor(context, R.color.main_yellow))
        } else {
            checkImageView.visibility = View.INVISIBLE
        }

        return view
    }

    fun toggleCountry(countryName: String): Boolean {
        return if (selectedCountryNames.contains(countryName)) {
            selectedCountryNames.remove(countryName)
            notifyDataSetChanged()
            false
        } else {
            selectedCountryNames.add(countryName)
            notifyDataSetChanged()
            true
        }
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val filterString = constraint?.toString()?.lowercase() ?: ""

                val results = if (filterString.isEmpty()) {
                    allCountries
                } else {
                    allCountries.filter {
                        it.english_name.lowercase().contains(filterString)
                    }
                }

                return FilterResults().apply {
                    values = results
                    count = results.size
                }
            }

            @Suppress("UNCHECKED_CAST")
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                filteredCountries = results?.values as? List<Country> ?: emptyList()
                notifyDataSetChanged()
            }
        }
    }
}
