package com.example.filmix.presentation.ui.fragments

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.viewpager2.widget.ViewPager2
import com.example.filmix.databinding.FragmentFullScreenImageBinding
import com.example.filmix.presentation.adapters.FullScreenImageAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FullScreenImageFragment : BaseFragment<FragmentFullScreenImageBinding>() {

    private val args: FullScreenImageFragmentArgs by navArgs()
    private lateinit var fullScreenAdapter: FullScreenImageAdapter

    override fun getViewBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentFullScreenImageBinding {
        return FragmentFullScreenImageBinding.inflate(inflater, container, false)
    }

    override fun initViews() {
        setupFullScreenGallery()
        setupClickListeners()
    }

    override fun observeViewModel() {
    }

    private fun setupFullScreenGallery() {
        val imageUrls = args.imageUrls.toList()
        val currentPosition = args.currentPosition

        fullScreenAdapter = FullScreenImageAdapter(imageUrls)
        binding.viewPagerGallery.adapter = fullScreenAdapter
        binding.viewPagerGallery.setCurrentItem(currentPosition, false)

        setupDotsIndicator(imageUrls.size, currentPosition)

        binding.viewPagerGallery.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                updateDots(position)
            }
        })
    }

    private fun setupClickListeners() {
        binding.btnClose.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun setupDotsIndicator(size: Int, currentPosition: Int) {
        binding.dotsIndicator.removeAllViews()

        if (size <= 1) return

        repeat(size) { index ->
            val dot = View(requireContext()).apply {
                val dotSize = resources.getDimensionPixelSize(com.example.filmix.R.dimen.dot_size)
                val dotMargin = resources.getDimensionPixelSize(com.example.filmix.R.dimen.dot_margin)

                layoutParams = ViewGroup.MarginLayoutParams(dotSize, dotSize).apply {
                    if (index != size - 1) {
                        rightMargin = dotMargin
                    }
                }

                setBackgroundResource(
                    if (index == currentPosition) {
                        com.example.filmix.R.drawable.dot_active
                    } else {
                        com.example.filmix.R.drawable.dot_inactive
                    }
                )
            }
            binding.dotsIndicator.addView(dot)
        }
    }

    private fun updateDots(selectedPosition: Int) {
        for (i in 0 until binding.dotsIndicator.childCount) {
            val dot = binding.dotsIndicator.getChildAt(i)
            dot.setBackgroundResource(
                if (i == selectedPosition) {
                    com.example.filmix.R.drawable.dot_active
                } else {
                    com.example.filmix.R.drawable.dot_inactive
                }
            )
        }
    }
}
