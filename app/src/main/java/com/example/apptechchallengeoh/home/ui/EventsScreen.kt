package com.example.apptechchallengeoh.home.ui

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.apptechchallengeoh.home.domain.model.Event
import com.example.apptechchallengeoh.home.domain.model.UiState
import com.example.apptechchallengeoh.home.viewmodel.EventViewModel
import com.example.apptechchallengeoh.ui.ViewLoading
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@Composable
fun EventsScreen(
    eventViewModel: EventViewModel = hiltViewModel(),
) {
    val eventsState by eventViewModel.eventsState.collectAsState()
    var eventsList by remember { mutableStateOf<List<Event>?>(null) }

    LaunchedEffect(eventViewModel) {
        eventViewModel.getEvents()
    }


    eventsState?.let {
        when (it) {
            is UiState.Loading -> {
                ViewLoading()
            }
            is UiState.Error -> {
                val error = (eventsState as UiState.Error).message
                Text("Error: $error", color = Color.Red)

            }
            UiState.Idle -> { }
            is UiState.Success -> {
                Log.v("Success", (eventsState as UiState.Success<List<Event>>).data.toString() )
                eventsList = it.data
            }

        }
    }



    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
                .fillMaxWidth(),
        ) {
            eventsList?.forEach { event ->
                EventListItem(event)
            }
        }
    }
}


@Composable
fun EventListItem(event: Event) {
    // Convierte Firestore Timestamp a LocalDateTime y formatea
    val formattedDate = event.date
        ?.toDate()
        ?.toInstant()
        ?.atZone(ZoneId.systemDefault())
        ?.toLocalDateTime()
        ?.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))
        ?: "--/--/---- --:--"

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .background(
                color = MaterialTheme.colorScheme.surfaceVariant,
                shape = RoundedCornerShape(8.dp)
            )
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Prioridad a la izquierda
        Text(
            text = event.priority.orEmpty(),
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier
                .widthIn(min = 64.dp)
                .padding(end = 12.dp)
        )

        // Nombre y fecha/hora
        Column {
            Text(
                text = event.name.orEmpty(),
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = formattedDate,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}