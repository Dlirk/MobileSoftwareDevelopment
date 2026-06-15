package com.example.flightsearch.data.entity

data class Favorite(
    val id: Int = 0,
    val departureCode: String,
    val destinationCode: String
)

data class FavoriteWithNames(
    val id: Int,
    val departureCode: String,
    val destinationCode: String,
    val departureName: String,
    val destinationName: String
)