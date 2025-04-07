package com.juandgaines.testground.presentation

import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import com.juandgaines.testground.domain.Coordinates
import com.juandgaines.testground.domain.Place
import com.juandgaines.testground.domain.Profile
import com.juandgaines.testground.domain.User
import org.junit.Rule
import org.junit.Test

class ProfileScreenTest {

    @get:Rule
    val composeRule = createComposeRule()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun profileScreen_whenProfileLoaded_showsUserAndPlaces() {
        // Arrange
        val state = previewProfileState()

        // Act
        composeRule.setContent {
            MaterialTheme {
                ProfileScreen(
                    state = state,
                    onPlaceClick = {}
                )
            }
        }

        // Assert
        composeRule.onNodeWithText("Welcome Test User!").assertIsDisplayed()
        composeRule.onNodeWithText("Place 1").assertIsDisplayed()
        composeRule.onNodeWithText("Lat: 1.0").assertIsDisplayed()
        composeRule.onNodeWithText("Long: 1.0").assertIsDisplayed()
    }

    @Test
    fun profileScreen_whenLoading_showsLoadingIndicator() {
        composeRule.setContent {
            MaterialTheme {
                ProfileScreen(
                    state = ProfileState(
                        isLoading = true
                    ),
                    onPlaceClick = {}
                )
            }
        }

        // Assert loading state using semantics
        composeRule.onNodeWithContentDescription("Loading Profile").assertIsDisplayed()
    }

    @Test
    fun profileScreen_whenError_showsErrorMessage() {
        val errorMessage = "Error loading profile"
        
        composeRule.setContent {
            MaterialTheme {
                ProfileScreen(
                    state = ProfileState(
                        errorMessage = errorMessage
                    ), onPlaceClick = {}
                )
            }
        }

        composeRule.onNodeWithText(errorMessage).assertIsDisplayed()
    }
}

fun previewProfileState() = ProfileState(
    profile = Profile(
        user = User(
            id = "test-user",
            username = "Test User"
        ),
        places = (1..3).map {
            Place(
                id = it.toString(),
                name = "Place $it",
                coordinates = Coordinates(
                    latitude = it.toDouble(),
                    longitude = it.toDouble()
                )
            )
        }
    ),
    isLoading = false
) 