package com.juandgaines.testground.domain

import kotlinx.serialization.Serializable

@Serializable
data class Profile(
    val user: User,
    val places: List<Place>
)