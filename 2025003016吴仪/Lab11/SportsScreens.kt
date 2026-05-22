package com.example.sports.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.sports.model.Sport

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SportsApp() {
    val sportsList = listOf(
        Sport(
            id = 1,
            title = "Baseball",
            subtitle = "9 athletes • Olympic",
            playerCount = 9,
            olympic = true,
            details = "Baseball details...",
            icon = Icons.Default.Home
        ),
        Sport(
            id = 2,
            title = "Badminton",
            subtitle = "1 athlete • Olympic",
            playerCount = 1,
            olympic = true,
            details = "Badminton details...",
            icon = Icons.Default.Edit
        ),
        Sport(
            id = 3,
            title = "Basketball",
            subtitle = "5 athletes • Olympic",
            playerCount = 5,
            olympic = true,
            details = "Basketball details...",
            icon = Icons.Default.Favorite
        ),
        Sport(
            id = 4,
            title = "Bowling",
            subtitle = "1 athlete • Not Olympic",
            playerCount = 1,
            olympic = false,
            details = "Bowling details...",
            icon = Icons.Default.Info
        ),
        Sport(
            id = 5,
            title = "Cycling",
            subtitle = "1 athlete • Olympic",
            playerCount = 1,
            olympic = true,
            details = "Cycling details...",
            icon = Icons.Default.AccountBox
        ),
        Sport(
            id = 6,
            title = "Golf",
            subtitle = "1 athlete • Not Olympic",
            playerCount = 1,
            olympic = false,
            details = "Golf details...",
            icon = Icons.Default.Share
        ),
        Sport(
            id = 7,
            title = "Running",
            subtitle = "1 athlete • Olympic",
            playerCount = 1,
            olympic = true,
            details = "Running details...",
            icon = Icons.Default.Send
        ),
        Sport(
            id = 8,
            title = "Soccer",
            subtitle = "11 athletes • Olympic",
            playerCount = 11,
            olympic = true,
            details = "Soccer details...",
            icon = Icons.Default.LocationOn
        ),
        Sport(
            id = 9,
            title = "Swimming",
            subtitle = "1 athlete • Olympic",
            playerCount = 1,
            olympic = true,
            details = "Swimming details...",
            icon = Icons.Default.Phone
        ),
        Sport(
            id = 10,
            title = "Table Tennis",
            subtitle = "1 athlete • Olympic",
            playerCount = 1,
            olympic = true,
            details = "Table Tennis details...",
            icon = Icons.Default.Refresh
        ),
        Sport(
            id = 11,
            title = "Tennis",
            subtitle = "1 athlete • Olympic",
            playerCount = 1,
            olympic = true,
            details = "Tennis details...",
            icon = Icons.Default.Settings
        )
    )

    var currentSport by remember { mutableStateOf<Sport?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Sports") },
                navigationIcon = {
                    if (currentSport != null) {
                        IconButton(onClick = { currentSport = null }) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                        }
                    }
                }
            )
        }
    ) { padding ->
        if (currentSport == null) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
            ) {
                items(sportsList) { sport ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        onClick = { currentSport = sport }
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = sport.icon,
                                contentDescription = sport.title,
                                modifier = Modifier.size(40.dp)
                            )
                            Spacer(Modifier.width(16.dp))
                            Column {
                                Text(sport.title, style = MaterialTheme.typography.headlineSmall)
                                Spacer(Modifier.height(4.dp))
                                Text(sport.subtitle, style = MaterialTheme.typography.bodyMedium)
                            }
                        }
                    }
                }
            }
        } else {
            BackHandler { currentSport = null }
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp)
            ) {
                Text(currentSport!!.title, style = MaterialTheme.typography.headlineLarge)
                Spacer(Modifier.height(8.dp))
                Text(currentSport!!.subtitle, style = MaterialTheme.typography.titleMedium)
                Spacer(Modifier.height(16.dp))
                Text(currentSport!!.details, style = MaterialTheme.typography.bodyLarge)
            }
        }
    }
}