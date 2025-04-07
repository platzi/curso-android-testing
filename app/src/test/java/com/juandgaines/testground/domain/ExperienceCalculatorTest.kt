package com.juandgaines.testground.domain

import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Test

class ExperienceCalculatorTest {

    private lateinit var experienceCalculator: ExperienceCalculator

    @Before
    fun setup() {
        experienceCalculator = ExperienceCalculator()
    }

    @Test
    fun givenTouristSpot_whenCalculateExperience_thenReturns5Points() {
        // Arrange
        val touristSpot = Place(
            id = "1",
            name = "Times Square",
            coordinates = Coordinates(40.5, -73.5)
        )

        // Act
        val result = experienceCalculator.calculateExperience(listOf(touristSpot))

        // Assert
        assertThat(result).isEqualTo(5)
    }

    @Test
    fun givenCulturalPlace_whenCalculateExperience_thenReturns4Points() {
        // Arrange
        val culturalPlace = Place(
            id = "2",
            name = "Smithsonian Museum",
            coordinates = Coordinates(38.5, -76.5)
        )

        // Act
        val result = experienceCalculator.calculateExperience(listOf(culturalPlace))

        // Assert
        assertThat(result).isEqualTo(4)
    }

    @Test
    fun givenMultiplePlaces_whenCalculateExperience_thenReturnsSumOfScores() {
        // Arrange
        val places = listOf(
            Place("1", "Tourist Spot", Coordinates(40.5, -73.5)), // 5 points
            Place("2", "Cultural Place", Coordinates(38.5, -76.5)), // 4 points
            Place("3", "Unknown Place", Coordinates(0.0, 0.0)) // 1 point
        )

        // Act
        val result = experienceCalculator.calculateExperience(places)

        // Assert
        assertThat(result).isEqualTo(10)
    }

    @Test
    fun givenEmptyList_whenCalculateExperience_thenReturnsZero() {
        // Act
        val result = experienceCalculator.calculateExperience(emptyList())

        // Assert
        assertThat(result).isEqualTo(0)
    }

    /**
     * Test naming convention explanation:
     * 
     * Format: `given[Condition]_when[Action]_then[ExpectedResult]`
     * 
     * Why this format?
     * 1. Uses backticks to allow spaces and natural language
     * 2. Clearly separates test conditions, actions, and expected results
     * 3. Follows BDD (Behavior-Driven Development) principles
     * 4. Makes test purpose immediately clear
     * 
     * Truth Assertions advantages:
     * 1. More readable than JUnit assertions
     * 2. Better error messages
     * 3. Fluent API with clear intent
     * 4. Chain-able assertions
     * 
     * Example assertions:
     * assertThat(value).isEqualTo(expected)
     * assertThat(list).hasSize(expected)
     * assertThat(string).contains(substring)
     * assertThat(boolean).isTrue()
     */
} 