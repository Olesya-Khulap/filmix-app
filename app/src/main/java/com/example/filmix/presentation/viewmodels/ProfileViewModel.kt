package com.example.filmix.presentation.viewmodels

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.filmix.data.local.ProfileImageManager
import com.example.filmix.domain.usecases.base.Result
import com.example.filmix.domain.usecases.profile.GetUserListsUseCase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val getUserListsUseCase: GetUserListsUseCase
) : ViewModel() {

    private val auth = FirebaseAuth.getInstance()

    private val _userLists = MutableLiveData<Result<GetUserListsUseCase.UserLists>>()
    val userLists: LiveData<Result<GetUserListsUseCase.UserLists>> = _userLists

    private val _isLoggedIn = MutableLiveData<Boolean>()
    val isLoggedIn: LiveData<Boolean> = _isLoggedIn

    private val _userName = MutableLiveData<String?>()
    val userName: LiveData<String?> = _userName

    private val _profileImageUpdated = MutableLiveData<Boolean>()
    val profileImageUpdated: LiveData<Boolean> = _profileImageUpdated

    private val _profileImageSaveResult = MutableLiveData<Boolean>()
    val profileImageSaveResult: LiveData<Boolean> = _profileImageSaveResult

    private var authStateListener: FirebaseAuth.AuthStateListener? = null

    companion object {
        private const val TAG = "ProfileViewModel"
    }

    init {
        setupAuthStateListener()
    }

    private fun setupAuthStateListener() {
        Log.d(TAG, "ProfileViewModel: Setting up AuthStateListener")
        authStateListener = FirebaseAuth.AuthStateListener { firebaseAuth ->
            val user = firebaseAuth.currentUser
            Log.d(TAG, "AuthStateListener: User changed - ${user?.email ?: "null"}")

            val isLoggedIn = user != null
            _isLoggedIn.value = isLoggedIn

            if (user != null) {
                updateUserInfo(user)
                loadUserLists()
            } else {
                _userName.value = null
                _userLists.value = Result.Success(
                    GetUserListsUseCase.UserLists(
                        favorites = emptyList(),
                        watchLater = emptyList(),
                        viewed = emptyList(),
                        favoritesCount = 0,
                        watchLaterCount = 0,
                        viewedCount = 0
                    )
                )
            }
        }

        authStateListener?.let { listener ->
            auth.addAuthStateListener(listener)
        }
    }

    private fun updateUserInfo(user: FirebaseUser) {
        Log.d(TAG, "ProfileViewModel: Updating user info")
        Log.d(TAG, "Email: ${user.email}")
        Log.d(TAG, "DisplayName: ${user.displayName}")

        val name = when {
            !user.displayName.isNullOrBlank() -> user.displayName
            !user.email.isNullOrBlank() -> user.email
            else -> "User"
        }

        Log.d(TAG, "Final name: $name")
        _userName.value = name
    }

    fun checkAuthState() {
        Log.d(TAG, "ProfileViewModel: Manual auth state check...")
        val user = auth.currentUser
        Log.d(TAG, "Current user: ${user?.email ?: "null"}")

        if (user != null) {
            updateUserInfo(user)
            _isLoggedIn.value = true
        } else {
            _userName.value = null
            _isLoggedIn.value = false
        }
    }

    fun loadUserLists() {
        Log.d(TAG, "ProfileViewModel: Loading user lists...")
        val user = auth.currentUser
        if (user != null) {
            Log.d(TAG, "User authenticated, loading lists for: ${user.email}")
            viewModelScope.launch {
                try {
                    _userLists.value = Result.Loading()
                    getUserListsUseCase()
                        .collect { result ->
                            Log.d(TAG, "Received result: $result")
                            _userLists.value = result
                        }
                } catch (e: Exception) {
                    Log.e(TAG, "Error loading user lists", e)
                    _userLists.value = Result.Error(e.message ?: "Unknown error")
                }
            }
        } else {
            Log.w(TAG, "Cannot load user lists - user not authenticated")

            _userLists.value = Result.Success(
                GetUserListsUseCase.UserLists(
                    favorites = emptyList(),
                    watchLater = emptyList(),
                    viewed = emptyList(),
                    favoritesCount = 0,
                    watchLaterCount = 0,
                    viewedCount = 0
                )
            )
        }
    }

    fun logout() {
        Log.d(TAG, "ProfileViewModel: Logging out user")
        auth.signOut()
        Log.d(TAG, "ProfileViewModel: User logged out successfully")
    }

    fun saveProfileImage(context: Context, imageUri: Uri) {
        Log.d(TAG, "ProfileViewModel: Saving profile image...")
        viewModelScope.launch {
            try {
                val result = withContext(Dispatchers.IO) {
                    ProfileImageManager.saveProfileImage(context, imageUri)
                }

                Log.d(TAG, "Save result: $result")
                _profileImageSaveResult.value = result

                if (result) {
                    _profileImageUpdated.value = true
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error saving profile image", e)
                _profileImageSaveResult.value = false
            }
        }
    }

    fun hasProfileImage(context: Context): Boolean {
        return ProfileImageManager.hasProfileImage(context)
    }

    fun clearProfileImage(context: Context) {
        Log.d(TAG, "ProfileViewModel: Clearing profile image...")
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                ProfileImageManager.clearProfileImage(context)
            }
            _profileImageUpdated.value = true
        }
    }

    override fun onCleared() {
        super.onCleared()
        authStateListener?.let { listener ->
            auth.removeAuthStateListener(listener)
        }
        Log.d(TAG, "ProfileViewModel: Cleaned up AuthStateListener")
    }
}
