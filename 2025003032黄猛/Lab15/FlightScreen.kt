package com.example.flightsearch.data

import androidx.room.Embedded

data class AirportRoute(
    @Embedded(prefix = "departure_") val departure: Airport,
    @Embedded(prefix = "destination_") val destination: Airport,
)