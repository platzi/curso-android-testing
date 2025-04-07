package com.juandgaines.testground.domain


interface UserRepository {
    suspend fun getProfile(userId: String): Result<Profile>
    suspend fun getPlaces(userId: String): Result<List<Place>>
}