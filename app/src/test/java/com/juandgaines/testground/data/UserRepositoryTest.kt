package com.juandgaines.testground.data

import com.google.common.truth.Truth
import com.juandgaines.testground.util.MainDispatcherRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class UserRepositoryTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var repository: UserRepositoryImpl
    private lateinit var api: UserApiFake

    @Before
    fun setUp() {
        // Setup for fake API tests
        api = UserApiFake()
        repository = UserRepositoryImpl(api)
    }


    @Test
    fun givenValidUserId_whenGetProfileWithFakeApi_thenReturnsProfile() = runTest {
        // Act
        val profileResult = repository.getProfile("1")

        // Assert
        Truth.assertThat(profileResult.isSuccess).isTrue()
        Truth.assertThat(profileResult.getOrThrow().user.id).isEqualTo("1")

        val expectedPlaces = api.places.filter { it.id == "1" }
        Truth.assertThat(profileResult.getOrThrow().places).isEqualTo(expectedPlaces)
    }

}