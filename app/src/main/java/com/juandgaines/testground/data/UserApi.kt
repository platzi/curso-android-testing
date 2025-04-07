package com.juandgaines.testground.data

import com.juandgaines.testground.domain.Place
import com.juandgaines.testground.domain.Profile
import com.juandgaines.testground.domain.User
import retrofit2.http.GET
import retrofit2.http.Path

interface UserApi {
    @GET("users/{userId}")
    suspend fun getUser(@Path("userId") userId: String): User

    @GET("users/{userId}/places")
    suspend fun getPlaces(@Path("userId") userId: String): List<Place>

    @GET("users/{userId}/profile")
    suspend fun getProfile(@Path("userId") userId: String): Profile
}