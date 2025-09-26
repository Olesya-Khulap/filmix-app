package com.example.filmix.presentation.ui.fragments

import android.text.InputType
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.filmix.R
import com.example.filmix.databinding.FragmentRegisterBinding
import com.example.filmix.presentation.viewmodels.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterFragment : BaseFragment<FragmentRegisterBinding>() {

    private val viewModel: AuthViewModel by viewModels()
    private var isPasswordVisible = false
    private var isConfirmPasswordVisible = false

    override fun getViewBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentRegisterBinding {
        return FragmentRegisterBinding.inflate(inflater, container, false)
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
                    Toast.makeText(context, "Registration successful!", Toast.LENGTH_SHORT).show()
                    findNavController().navigate(
                        RegisterFragmentDirections.actionRegisterFragmentToProfileFragment()
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

        binding.btnToggleConfirmPassword.setOnClickListener {
            toggleConfirmPasswordVisibility()
        }

        binding.btnBack.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.btnRegister.setOnClickListener {
            val email = binding.etEmail.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()
            val confirmPassword = binding.etConfirmPassword.text.toString().trim()

            if (email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(context, "Please fill in all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (password != confirmPassword) {
                Toast.makeText(context, "Passwords do not match", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (password.length < 6) {
                Toast.makeText(context, "Password should be at least 6 characters", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            viewModel.register(email, password)
        }

        binding.btnLogin.setOnClickListener {
            findNavController().navigate(
                RegisterFragmentDirections.actionRegisterFragmentToAuthFragment()
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

    private fun toggleConfirmPasswordVisibility() {
        if (isConfirmPasswordVisible) {
            binding.etConfirmPassword.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            binding.btnToggleConfirmPassword.setImageResource(R.drawable.ic_eye_closed)
            isConfirmPasswordVisible = false
        } else {
            binding.etConfirmPassword.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
            binding.btnToggleConfirmPassword.setImageResource(R.drawable.ic_eye_open)
            isConfirmPasswordVisible = true
        }
        binding.etConfirmPassword.setSelection(binding.etConfirmPassword.text.length)
    }
}
