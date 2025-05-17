package com.example.apptechchallengeoh.home.ui

import androidx.compose.material3.Scaffold
import androidx.compose.material3.SmallTopAppBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController

/*@Composable
fun CreateEventScreen(navController: NavHostController, eventViewModel: EventViewModel = hiltViewModel()) {
    // Obtener las categorías desde el ViewModel
    val categories by eventViewModel.categories.collectAsState()

    // Variables de estado para el formulario
    var eventName by remember { mutableStateOf("") }
    var eventDescription by remember { mutableStateOf("") }
    var eventCategory by remember { mutableStateOf(categories.firstOrNull() ?: "") }
    var eventPriority by remember { mutableStateOf("Media") }
    var eventDate by remember { mutableStateOf("") }
    var eventTime by remember { mutableStateOf("") }

    // Estado de la UI
    var uiState by remember { mutableStateOf<UiState<String>>(UiState.Idle) }

    Scaffold(
        topBar = {
            SmallTopAppBar(
                title = { Text("Crear Evento") },
            )
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                    .padding(paddingValues),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Campo para nombre del evento
                OutlinedTextField(
                    value = eventName,
                    onValueChange = { eventName = it },
                    label = { Text("Nombre del Evento") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Campo para descripción del evento
                OutlinedTextField(
                    value = eventDescription,
                    onValueChange = { eventDescription = it },
                    label = { Text("Descripción del Evento") },
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 3
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Selector de categorías
                ExposedDropdownMenuBox(
                    expanded = true,
                    onExpandedChange = { *//* Control del desplegable *//* }
                ) {
                    OutlinedTextField(
                        value = eventCategory,
                        onValueChange = { eventCategory = it },
                        label = { Text("Categoría del Evento") },
                        readOnly = true,
                        modifier = Modifier.fillMaxWidth(),
                        trailingIcon = {
                            Icon(Icons.Filled.ArrowDropDown, contentDescription = "Dropdown Icon")
                        }
                    )
                    ExposedDropdownMenu(
                        expanded = true,
                        onDismissRequest = { *//* Cerrar el dropdown *//* }
                    ) {
                        categories.forEach { category ->
                            DropdownMenuItem(onClick = { eventCategory = category }) {
                                Text(text = category)
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Campo para la fecha del evento
                OutlinedTextField(
                    value = eventDate,
                    onValueChange = { eventDate = it },
                    label = { Text("Fecha del Evento") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Campo para la hora del evento
                OutlinedTextField(
                    value = eventTime,
                    onValueChange = { eventTime = it },
                    label = { Text("Hora del Evento") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Radio buttons para la prioridad
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text("Prioridad:")
                    RadioButton(
                        selected = eventPriority == "Alta",
                        onClick = { eventPriority = "Alta" }
                    )
                    Text("Alta")
                    RadioButton(
                        selected = eventPriority == "Media",
                        onClick = { eventPriority = "Media" }
                    )
                    Text("Media")
                    RadioButton(
                        selected = eventPriority == "Baja",
                        onClick = { eventPriority = "Baja" }
                    )
                    Text("Baja")
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Botón para crear el evento
                Button(
                    onClick = {
                        if (eventName.isNotBlank() && eventDescription.isNotBlank() && eventDate.isNotBlank() && eventTime.isNotBlank()) {
                            val newEvent = Event(
                                name = eventName,
                                description = eventDescription,
                                date = SimpleDateFormat("yyyy-MM-dd HH:mm").parse("$eventDate $eventTime")?.time ?: 0,
                                category = eventCategory,
                                priority = eventPriority
                            )
                            eventViewModel.addEvent(newEvent)
                            uiState = UiState.Success("Evento creado con éxito")
                            navController.popBackStack() // Volver a la lista de eventos
                        } else {
                            uiState = UiState.Error("Por favor, complete todos los campos.")
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Crear Evento")
                }

                // Mostrar el mensaje de error o éxito
                if (uiState is UiState.Error) {
                    val errorMessage = (uiState as UiState.Error).message
                    Text(
                        text = errorMessage,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }

                if (uiState is UiState.Success) {
                    val successMessage = (uiState as UiState.Success).data
                    Text(
                        text = successMessage,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
            }
        }
    )
}*/

