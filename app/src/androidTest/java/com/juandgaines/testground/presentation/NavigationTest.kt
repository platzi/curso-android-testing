package com.juandgaines.testground.presentation

import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.juandgaines.testground.domain.Coordinates
import com.juandgaines.testground.domain.Place
import com.juandgaines.testground.domain.Profile
import com.juandgaines.testground.domain.User
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class NavigationTest {

    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun navigation_fromProfileToDetail_worksCorrectly() {
        // Arrange
        val testPlace = Place(
            id = "1",
            name = "Test Place",
            coordinates = Coordinates(1.0, 1.0)
        )
        val testProfile = Profile(
            user = User("test-user", "Test User"),
            places = listOf(testPlace)
        )

        // Act
        composeRule.setContent {
            MaterialTheme {
                val navController = rememberNavController()
                NavHost(
                    navController = navController,
                    startDestination = "profile"
                ) {
                    composable("profile") {
                        ProfileScreen(
                            state = ProfileState(profile = testProfile),
                            onPlaceClick = { place ->
                                navController.navigate("detail/${place.id}")
                            }
                        )
                    }
                    composable("detail/{placeId}") { backStackEntry ->
                        val placeId = backStackEntry.arguments?.getString("placeId")
                        val place = testProfile.places.find { it.id == placeId }
                        if (place != null) {
                            DetailPlaceScreen(
                                place = place,
                                onNavigateBack = { navController.popBackStack() }
                            )
                        }
                    }
                }
            }
        }

        // Assert - Verify initial screen
        composeRule.onNodeWithText("Welcome Test User!").assertIsDisplayed()
        composeRule.onNodeWithContentDescription("Place card: Test Place").assertIsDisplayed()

        // Act - Navigate to detail
        composeRule.onNodeWithContentDescription("Place card: Test Place").performClick()

        // Assert - Verify detail screen
        composeRule.onNodeWithText("Place Details").assertIsDisplayed()
        composeRule.onNodeWithText("Test Place").assertIsDisplayed()
        composeRule.onNodeWithText("Latitude").assertIsDisplayed()
        composeRule.onNodeWithText("1.0°").assertIsDisplayed()

        // Act - Navigate back
        composeRule.onNodeWithContentDescription("Navigate back").performClick()

        // Assert - Verify back on profile screen
        composeRule.onNodeWithText("Welcome Test User!").assertIsDisplayed()
        composeRule.onNodeWithContentDescription("Place card: Test Place").assertIsDisplayed()
    }
}

