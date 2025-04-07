package com.juandgaines.testground.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.juandgaines.testground.domain.Profile
import com.juandgaines.testground.domain.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val repository: UserRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val userId = savedStateHandle.get<String>("userId")

    private val _state = MutableStateFlow(ProfileState())
    val state = _state.asStateFlow()

    fun loadProfile() {
        viewModelScope.launch {
            userId?.let { id ->
                _state.update { it.copy(isLoading = true) }

                val result = repository.getProfile(id)

                _state.update {
                    it.copy(
                        profile = result.getOrNull(),
                        errorMessage = result.exceptionOrNull()?.message,
                        isLoading = false
                    )
                }
            }
        }
    }
}

data class ProfileState(
    val profile: Profile? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
) 