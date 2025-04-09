package com.juandgaines.testground.presentation

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ProfileViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    private lateinit var viewModel: ProfileViewModel
    private lateinit var repository: UserRepositoryFake

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)

        repository = UserRepositoryFake()
        viewModel = ProfileViewModel(
            repository = repository,
            savedStateHandle = SavedStateHandle(
                initialState = mapOf(
                    "userId" to repository.profileToReturn.user.id
                )
            )
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun givenValidUserId_whenLoadProfile_thenProfileIsLoaded() = runTest {
        // Act
        viewModel.loadProfile()
        advanceUntilIdle()

        // Assert
        assertThat(viewModel.state.value.profile).isEqualTo(repository.profileToReturn)
        assertThat(viewModel.state.value.isLoading).isFalse()
    }

    @Test
    fun givenRepositoryError_whenLoadProfile_thenErrorStateIsSet() = runTest {
        // Arrange
        repository.errorToReturn = Exception("Test exception")

        // Act
        viewModel.loadProfile()
        advanceUntilIdle()

        // Assert
        assertThat(viewModel.state.value.profile).isNull()
        assertThat(viewModel.state.value.errorMessage).isEqualTo("Test exception")
        assertThat(viewModel.state.value.isLoading).isFalse()
    }

    @Test
    fun givenLoadingState_whenLoadProfile_thenStateUpdatesCorrectly() = runTest {
        // Act & Assert
        viewModel.state.test {
            val emission1 = awaitItem()
            assertThat(emission1.isLoading).isFalse()

            viewModel.loadProfile()

            val emission2 = awaitItem()
            assertThat(emission2.isLoading).isTrue()

            val emission3 = awaitItem()
            assertThat(emission3.isLoading).isFalse()
            assertThat(emission3.profile).isEqualTo(repository.profileToReturn)
        }
    }
}
