package com.juandgaines.testground.presentation

import com.juandgaines.testground.domain.Coordinates
import com.juandgaines.testground.domain.Place
import com.juandgaines.testground.domain.Profile
import com.juandgaines.testground.domain.User
import com.juandgaines.testground.domain.UserRepository
import java.util.UUID

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