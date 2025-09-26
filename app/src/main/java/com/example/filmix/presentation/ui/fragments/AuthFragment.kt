package com.example.filmix.presentation.ui.fragments

import android.text.InputType
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.filmix.R
import com.example.filmix.databinding.FragmentAuthBinding
import com.example.filmix.presentation.viewmodels.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AuthFragment : BaseFragment<FragmentAuthBinding>() {

    private val viewModel: AuthViewModel by viewModels()
    private var isPasswordVisible = false

    override fun getViewBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentAuthBinding {
        return FragmentAuthBinding.inflate(inflater, container, false)
    }

    override fun initViews() {
        setupClickListeners()
    }

    override fun observeViewModel() {
        viewModel.authResult.observe(viewLifecycleOwner) { result ->
            when (result) {
                is com.example.filmix.domain.usecases.base.Result.Loading -> {

                }
                is com.example.filmix.domain.usecases.base.Result.Success -> {
                    Toast.makeText(context, "Login successful!", Toast.LENGTH_SHORT).show()
                    findNavController().navigate(
                        AuthFragmentDirections.actionAuthFragmentToProfileFragment()
                    )
                }
                is com.example.filmix.domain.usecases.base.Result.Error -> {
                    Toast.makeText(context, result.message, Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun setupClickListeners() {
        binding.btnTogglePassword.setOnClickListener {
            togglePasswordVisibility()
        }

        binding.btnBack.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.btnLogin.setOnClickListener {
            val email = binding.etEmail.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(context, "Please fill in all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            viewModel.login(email, password)
        }

        binding.btnSignup.setOnClickListener {
            findNavController().navigate(
                AuthFragmentDirections.actionAuthFragmentToRegisterFragment()
            )
        }
    }

    private fun togglePasswordVisibility() {
        if (isPasswordVisible) {
            binding.etPassword.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            binding.btnTogglePassword.setImageResource(R.drawable.ic_eye_closed)
            isPasswordVisible = false
        } else {
            binding.etPassword.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
            binding.btnTogglePassword.setImageResource(R.drawable.ic_eye_open)
            isPasswordVisible = true
        }
        binding.etPassword.setSelection(binding.etPassword.text.length)
    }
}
