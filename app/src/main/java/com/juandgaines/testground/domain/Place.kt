package com.juandgaines.testground.domain

import kotlinx.serialization.Serializable

@Serializable
data class Place(
    val id: String,
    val name: String,
    val coordinates: Coordinates
)

@Serializable
data class Coordinates(
    val latitude: Double,
    val longitude: Double
)