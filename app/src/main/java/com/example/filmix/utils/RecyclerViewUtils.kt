package com.example.filmix.utils

import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.filmix.R

object RecyclerViewUtils {

    fun setupMovieGrid(
        recyclerView: RecyclerView,
        adapter: RecyclerView.Adapter<*>
    ) {
        val context = recyclerView.context
        val spanCount = 2
        val spacing = context.resources.getDimensionPixelSize(R.dimen.grid_spacing)

        recyclerView.apply {
            layoutManager = GridLayoutManager(context, spanCount)
            this.adapter = adapter

            addItemDecoration(GridSpacingItemDecoration(spanCount, spacing, false))

            clipToPadding = false
        }
    }
}
