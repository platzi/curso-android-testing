package com.juandgaines.testground.domain

class ExperienceCalculator {
    
    /**
     * Calculates the total experience points based on visited places.
     * Experience scale:
     * - Tourist spots: 5 points
     * - Cultural places: 4 points
     * - Parks and nature: 3 points
     * - Local neighborhoods: 2 points
     * - Others: 1 point
     */
    fun calculateExperience(visitedPlaces: List<Place>): Int {
        return visitedPlaces.sumOf { place ->
            getPlaceScore(place)
        }
    }
    
    private fun getPlaceScore(place: Place): Int {
        // Simple scoring based on coordinates ranges
        return when {
            // Tourist spots (around popular coordinates)
            isInTouristArea(place.coordinates) -> 5
            // Cultural places (specific coordinate ranges)
            isInCulturalArea(place.coordinates) -> 4
            // Parks and nature (based on coordinate patterns)
            isInNatureArea(place.coordinates) -> 3
            // Local neighborhoods
            isInLocalArea(place.coordinates) -> 2
            // Default score for any other place
            else -> 1
        }
    }
    
    private fun isInTouristArea(coordinates: Coordinates): Boolean {
        // Example: Consider tourist areas to be around specific coordinates
        return coordinates.latitude in 40.0..41.0 && coordinates.longitude in -74.0..-73.0
    }
    
    private fun isInCulturalArea(coordinates: Coordinates): Boolean {
        // Example: Cultural areas might be in specific city centers
        return coordinates.latitude in 38.0..39.0 && coordinates.longitude in -77.0..-76.0
    }
    
    private fun isInNatureArea(coordinates: Coordinates): Boolean {
        // Example: Nature areas might be in national parks coordinates
        return coordinates.latitude in 36.0..37.0 && coordinates.longitude in -119.0..-118.0
    }
    
    private fun isInLocalArea(coordinates: Coordinates): Boolean {
        // Example: Local areas might be in residential coordinates
        return coordinates.latitude in 42.0..43.0 && coordinates.longitude in -71.0..-70.0
    }
}