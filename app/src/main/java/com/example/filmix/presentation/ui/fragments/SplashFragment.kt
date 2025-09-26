package com.example.filmix.presentation.ui.fragments

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.filmix.databinding.FragmentSplashBinding
import com.example.filmix.presentation.viewmodels.SplashViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SplashFragment : BaseFragment<FragmentSplashBinding>() {

    private val viewModel: SplashViewModel by viewModels()

    override fun getViewBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentSplashBinding {
        return FragmentSplashBinding.inflate(inflater, container, false)
    }

    override fun initViews() {
        startDataPreloading()
    }

    override fun observeViewModel() {
        viewModel.loadingProgress.observe(viewLifecycleOwner) { progress ->
            binding.progressBarSplash.progress = progress
        }

        viewModel.loadingText.observe(viewLifecycleOwner) { text ->
            binding.tvLoadingText.text = text
        }

        viewModel.navigationReady.observe(viewLifecycleOwner) { ready ->
            if (ready) {
                navigateToHome()
            }
        }
    }

    private fun startDataPreloading() {
        viewModel.startPreloading()
    }

    private fun navigateToHome() {
        findNavController().navigate(
            SplashFragmentDirections.actionSplashFragmentToHomeFragment()
        )
    }
}
