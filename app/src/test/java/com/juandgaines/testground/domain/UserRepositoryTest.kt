package com.juandgaines.testground.domain

import com.google.common.truth.Truth
import com.juandgaines.testground.data.UserRepositoryImpl
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class UserRepositoryTest {

    private lateinit var userRepository: UserRepositoryImpl
    private lateinit var api: UserFakeApi

    @Before
    fun setUp(){
        api = UserFakeApi()
        userRepository = UserRepositoryImpl( api)
    }


    @Test
    fun givenValidUserId_whenGetProfileWithFakeApi_thenReturnsProfile() = runTest {
        //Given
        val userid= "1"
        //Act
        val profileResult = userRepository.getProfile(userid)

        //Assert

        Truth.assertThat(profileResult.isSuccess).isTrue()
        Truth.assertThat(profileResult.getOrThrow().user.id).isEqualTo("1")

        val expectedPlaces= api.places.filter { it.id == "1" }
        Truth.assertThat(profileResult.getOrThrow().places).isEqualTo(expectedPlaces)


    }
}