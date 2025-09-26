package com.example.filmix.presentation.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.filmix.domain.usecases.base.Result
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor() : ViewModel() {

    private val auth = FirebaseAuth.getInstance()

    private val _authResult = MutableLiveData<Result<Boolean>>()
    val authResult: LiveData<Result<Boolean>> = _authResult

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _authResult.value = Result.Loading()
            try {
                auth.signInWithEmailAndPassword(email, password).await()
                _authResult.value = Result.Success(true)
            } catch (e: Exception) {
                _authResult.value = Result.Error(e.message ?: "Authentication failed")
            }
        }
    }

    fun register(email: String, password: String) {
        viewModelScope.launch {
            _authResult.value = Result.Loading()
            try {
                auth.createUserWithEmailAndPassword(email, password).await()
                _authResult.value = Result.Success(true)
            } catch (e: Exception) {
                _authResult.value = Result.Error(e.message ?: "Registration failed")
            }
        }
    }
}
