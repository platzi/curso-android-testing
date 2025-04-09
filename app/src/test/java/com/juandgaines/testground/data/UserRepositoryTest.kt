package com.juandgaines.testground.data

import com.google.common.truth.Truth
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.juandgaines.testground.domain.Coordinates
import com.juandgaines.testground.domain.Place
import com.juandgaines.testground.domain.User
import com.juandgaines.testground.util.MainDispatcherRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import retrofit2.HttpException
import retrofit2.Retrofit

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