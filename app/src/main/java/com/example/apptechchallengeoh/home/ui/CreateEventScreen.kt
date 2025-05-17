package com.example.apptechchallengeoh.home.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SmallTopAppBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.apptechchallengeoh.home.domain.model.CategoryModel
import com.example.apptechchallengeoh.home.domain.model.Event
import com.example.apptechchallengeoh.home.domain.model.UiState
import com.example.apptechchallengeoh.home.viewmodel.EventViewModel
import java.text.SimpleDateFormat

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateEventScreen(navController: NavHostController, eventViewModel: EventViewModel = hiltViewModel()) {
    // Obtener el estado de las categorías
    val categoriesState by eventViewModel.categoriesState.collectAsState()

    // Variables de estado para el formulario
    var eventName by remember { mutableStateOf("") }
    var eventDescription by remember { mutableStateOf("") }

    // Solo accedemos a las categorías cuando el estado es `Success`
    var eventCategory by remember {
        mutableStateOf(
            when (categoriesState) {
                is UiState.Success -> (categoriesState as UiState.Success<List<CategoryModel>>).data?.firstOrNull()?.name ?: ""
                else -> ""
            }
        )
    }

    var eventPriority by remember { mutableStateOf("Media") }
    var eventDate by remember { mutableStateOf("") }
    var eventTime by remember { mutableStateOf("") }

    // Estado de la UI
    val eventsState by eventViewModel.eventsState.collectAsState()

    // Mostrar un loading mientras se obtiene la data
    if (categoriesState is UiState.Loading) {
        CircularProgressIndicator(modifier = Modifier.padding(top = 16.dp))
    } else if (categoriesState is UiState.Error) {
        Text(
            text = (categoriesState as UiState.Error).message,
            color = MaterialTheme.colorScheme.error,
            modifier = Modifier.padding(bottom = 16.dp)
        )
    }

    // Si ya se han obtenido las categorías, mostrar el formulario
    if (categoriesState is UiState.Success) {
        Scaffold(
            topBar = { SmallTopAppBar(title = { Text("Crear Evento") }) },
            content = { paddingValues ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                        .padding(paddingValues),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Mostrar errores si hay
                    if (eventsState is UiState.Error) {
                        Text(
                            text = (eventsState as UiState.Error).message,
                            color = MaterialTheme.colorScheme.error,
                            modifier = Modifier.padding(bottom = 16.dp)
                        )
                    }

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
                    // Selector de categorías (ExposedDropdownMenu)
                    var expanded by remember { mutableStateOf(false) }
                    // Selector de categorías
                    ExposedDropdownMenuBox(
                        expanded = true,
                        onExpandedChange = { /* Control del desplegable */ }
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
                        /*ExposedDropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false }
                        ) {
                            // Verificamos si categoriesState es un Success y tenemos datos disponibles
                            (categoriesState as UiState.Success<List<CategoryModel>>).data?.forEach { category ->
                                DropdownMenuItem(
                                    onClick = {
                                        eventCategory = category.name
                                            ?: "" // Asignamos el nombre de la categoría seleccionada
                                        expanded =
                                            false // Cerramos el dropdown después de seleccionar
                                    },
                                    text = TODO(),
                                    modifier = TODO(),
                                    leadingIcon = TODO(),
                                    trailingIcon = TODO(),
                                    enabled = TODO(),
                                    colors = TODO(),
                                    contentPadding = TODO()
                                ) {
                                    Text(text = category.name ?: "") // Mostramos el nombre de la categoría
                                }
                            }
                        }*/
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Resto de los campos (fecha, hora, prioridad)
                    OutlinedTextField(value = eventDate, onValueChange = { eventDate = it }, label = { Text("Fecha") })
                    OutlinedTextField(value = eventTime, onValueChange = { eventTime = it }, label = { Text("Hora") })

                    Row {
                        Text("Prioridad:")
                        RadioButton(selected = eventPriority == "Alta", onClick = { eventPriority = "Alta" })
                        Text("Alta")
                        RadioButton(selected = eventPriority == "Media", onClick = { eventPriority = "Media" })
                        Text("Media")
                        RadioButton(selected = eventPriority == "Baja", onClick = { eventPriority = "Baja" })
                        Text("Baja")
                    }

                    Button(
                        onClick = {
                            val newEvent = Event(
                                name = eventName,
                                description = eventDescription,
                                date = SimpleDateFormat("yyyy-MM-dd HH:mm").parse("$eventDate $eventTime")?.time ?: 0,
                                category = eventCategory,
                                priority = eventPriority
                            )
                            eventViewModel.addEvent(newEvent)
                            navController.popBackStack() // Volver a la lista de eventos
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Crear Evento")
                    }
                }
            }
        )
    }
}
