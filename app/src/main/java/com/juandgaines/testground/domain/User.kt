package com.juandgaines.testground.domain

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val id: String,
    val username: String
)