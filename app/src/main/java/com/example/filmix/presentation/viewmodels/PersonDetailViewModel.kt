package com.example.filmix.presentation.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.filmix.domain.models.MovieItem
import com.example.filmix.domain.models.PersonDetails
import com.example.filmix.domain.usecases.base.Result
import com.example.filmix.domain.usecases.person.GetPersonDetailsUseCase
import com.example.filmix.domain.usecases.person.GetPersonFilmographyUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PersonDetailViewModel @Inject constructor(
    private val getPersonDetailsUseCase: GetPersonDetailsUseCase,
    private val getPersonFilmographyUseCase: GetPersonFilmographyUseCase
) : ViewModel() {

    private val _personDetails = MutableLiveData<Result<PersonDetails>>()
    val personDetails: LiveData<Result<PersonDetails>> = _personDetails

    private val _filmography = MutableLiveData<Result<List<MovieItem>>>()
    val filmography: LiveData<Result<List<MovieItem>>> = _filmography

    fun loadPersonDetails(personId: Int) {
        viewModelScope.launch {
            getPersonDetailsUseCase(GetPersonDetailsUseCase.Params(personId))
                .collect { result ->
                    _personDetails.value = result
                }

            getPersonFilmographyUseCase(GetPersonFilmographyUseCase.Params(personId))
                .collect { result ->
                    _filmography.value = result
                }
        }
    }
}
