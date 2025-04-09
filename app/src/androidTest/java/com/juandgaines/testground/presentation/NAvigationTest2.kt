package com.juandgaines.testground.presentation

import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.performClick
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.testing.TestNavHostController
import com.google.common.truth.Truth
import com.juandgaines.testground.domain.Coordinates
import com.juandgaines.testground.domain.Place
import com.juandgaines.testground.domain.Profile
import com.juandgaines.testground.domain.User
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class NavigationTest2 {

    @get:Rule
    val composeRule = createComposeRule()

    private lateinit var navController: TestNavHostController
    private lateinit var testPlace: Place
    private lateinit var testProfile: Profile

    @Before
    fun setup() {

        testPlace = Place(
            id = "1",
            name = "Test Place",
            coordinates = Coordinates(1.0, 1.0)
        )
        testProfile = Profile(
            user = User("test-user", "Test User"),
            places = listOf(testPlace)
        )
    }

    @Test
    fun navigation_state_is_correct_when_navigating_to_detail_screen() {
        // Arrange
        composeRule.setContent {
            navController = TestNavHostController(LocalContext.current)
            navController.navigatorProvider.addNavigator(ComposeNavigator())
            MaterialTheme {
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
        // Act - Navigate to detail screen
        composeRule.onNodeWithContentDescription("Place card: Test Place").performClick()

        Truth.assertThat(navController.previousBackStackEntry?.destination?.route).isEqualTo("profile")

        // Act - Navigate back
        composeRule.onNodeWithContentDescription("Navigate back").performClick()
        // Assert - Verify back navigation state
        Truth.assertThat(navController.currentDestination?.route).isEqualTo("profile")
    }

    @Test
    fun navigation_state_is_correct_when_popping_back_stack(){
        // Arrange
        composeRule.setContent {
            navController = TestNavHostController(LocalContext.current)
            navController.navigatorProvider.addNavigator(ComposeNavigator())
            MaterialTheme {
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

        // Act - Navigate to detail screen
        composeRule.onNodeWithContentDescription("Place card: Test Place").performClick()
        composeRule.waitForIdle()

        composeRule.onNodeWithContentDescription("Navigate back").performClick()
        composeRule.waitForIdle()
        // Assert - Verify back stack state
        Truth.assertThat(navController.currentDestination?.route).contains("profile")
        Truth.assertThat(navController.previousBackStackEntry).isNull()

    }

    @Test
    fun navigation_state_Is_Correct_When_Navigating_with_arguments() {
        // Arrange
        composeRule.setContent {
            navController = TestNavHostController(LocalContext.current)
            navController.navigatorProvider.addNavigator(ComposeNavigator())
            MaterialTheme {
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

        // Act - Navigate to detail screen
        composeRule.onNodeWithContentDescription("Place card: Test Place").performClick()

        // Assert - Verify navigation arguments
        val currentDestination = navController.currentDestination
        Truth.assertThat(currentDestination?.route).contains("detail")
    }
}