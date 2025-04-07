package com.juandgaines.testground.presentation

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.juandgaines.testground.domain.Coordinates
import com.juandgaines.testground.domain.Place
import com.juandgaines.testground.domain.Profile
import com.juandgaines.testground.domain.User
import com.juandgaines.testground.domain.UserRepository
import com.juandgaines.testground.util.MainDispatcherRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.util.UUID

@OptIn(ExperimentalCoroutinesApi::class)
class ProfileViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var viewModel: ProfileViewModel
    private lateinit var repository: UserRepositoryFake

    @Before
    fun setUp() {
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

class UserRepositoryFake : UserRepository {
    var profileToReturn = profile()
    var errorToReturn: Exception? = null

    override suspend fun getProfile(userId: String): Result<Profile> {
        return if (errorToReturn != null) {
            Result.failure(errorToReturn!!)
        } else Result.success(profileToReturn)
    }

    override suspend fun getPlaces(userId: String): Result<List<Place>> {
        return Result.success(profileToReturn.places)
    }
}

private fun user(): User {
    return User(
        id = UUID.randomUUID().toString(),
        username = "test-user"
    )
}

private fun place(userId: String): Place {
    return Place(
        id = UUID.randomUUID().toString(),
        name = "test place",
        coordinates = Coordinates(1.0, 1.0)
    )
}

private fun profile(): Profile {
    val user = user()
    return Profile(
        user = user,
        places = (1..10).map {
            place(user.id)
        }
    )
} 