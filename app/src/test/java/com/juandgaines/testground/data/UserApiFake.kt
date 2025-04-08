package com.juandgaines.testground.data

import com.juandgaines.testground.domain.Coordinates
import com.juandgaines.testground.domain.Place
import com.juandgaines.testground.domain.Profile
import com.juandgaines.testground.domain.User
import java.util.UUID

class UserApiFake : UserApi {
    var users = (1..10).map {
        User(
            id = it.toString(),
            username = "User$it"
        )
    }

    var places = (1..10).map {
        Place(
            id = UUID.randomUUID().toString(),
            name = "Place$it",
            coordinates = Coordinates(it.toDouble(), it.toDouble())
        )
    }

    override suspend fun getUser(userId: String): User {
        return users.find { it.id == userId } ?: throw Exception("User not found")
    }

    override suspend fun getPlaces(userId: String): List<Place> {
        return places.filter { it.id == userId }
    }

    override suspend fun getProfile(userId: String): Profile {
        val user = getUser(userId)
        return Profile(user = user, places = getPlaces(userId))
    }
}