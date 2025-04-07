package com.juandgaines.testground.data

import com.juandgaines.testground.domain.Place
import com.juandgaines.testground.domain.Profile
import com.juandgaines.testground.domain.UserRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val api: UserApi
) : UserRepository {

    override suspend fun getProfile(userId: String): Result<Profile> {
        return coroutineScope {
            val userResult = async {
                try {
                    Result.success(api.getUser(userId))
                } catch (e: HttpException) {
                    Result.failure(e)
                } catch (e: IOException) {
                    Result.failure(e)
                }
            }
            val placesResult = async {
                try {
                    Result.success(api.getPlaces(userId))
                } catch (e: HttpException) {
                    Result.failure(e)
                } catch (e: IOException) {
                    Result.failure(e)
                }
            }

            val fetchedUser = userResult.await().getOrNull()
            if (fetchedUser != null) {
                Result.success(
                    Profile(
                        user = fetchedUser,
                        places = placesResult.await().getOrNull() ?: emptyList()
                    )
                )
            } else {
                Result.failure(
                    userResult.await().exceptionOrNull() ?: Exception("Unknown error")
                )
            }
        }
    }

    override suspend fun getPlaces(userId: String): Result<List<Place>> {
        return try {
            Result.success(api.getPlaces(userId))
        } catch (e: HttpException) {
            Result.failure(e)
        } catch (e: IOException) {
            Result.failure(e)
        }
    }
}