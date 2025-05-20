package com.example.apptechchallengeoh.home.ui

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SmallTopAppBar
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.apptechchallengeoh.home.domain.model.CategoryModel
import com.example.apptechchallengeoh.home.domain.model.Event
import com.example.apptechchallengeoh.home.domain.model.UiState
import com.example.apptechchallengeoh.home.viewmodel.EventViewModel
import com.example.apptechchallengeoh.ui.ViewLoading
import com.google.firebase.Timestamp
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import kotlin.math.log

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateEventScreen(
    navController: NavHostController,
    eventViewModel: EventViewModel = hiltViewModel(),
) {
    val context = LocalContext.current
    val calendar = remember { Calendar.getInstance() }

    // Estado para gestionar la expansión del menú
    var expanded by remember { mutableStateOf(false) }

    // Obtener el estado de las categorías
    val categoriesState by eventViewModel.categoriesState.collectAsState()
    // Obtener los eventos
    val eventsState by eventViewModel.eventsState.collectAsState()
    // Registro de evento
    val eventsAddState by eventViewModel.eventsAddState.collectAsState()

    // Variables de estado para el formulario
    var eventName by remember { mutableStateOf("") }
    var eventDescription by remember { mutableStateOf("") }

    // Solo accedemos a las categorías cuando el estado es `Success`
    var eventCategory by remember {
        mutableStateOf(
            when (categoriesState) {
                is UiState.Success -> (categoriesState as UiState.Success<List<CategoryModel>>).data?.firstOrNull()?.name
                    ?: ""
                else -> ""
            }
        )
    }

    var eventDate by remember { mutableStateOf("") }
    var eventTime by remember { mutableStateOf("") }

    val selectedPriority by eventViewModel.selectedPriority.collectAsState()

    LaunchedEffect(eventsAddState) {
        if (eventsAddState is UiState.Success) {
            // Navegar a la pestaña Lista Eventos
            navController.navigate("eventList") {
                // Opcional: limpiar el stack para evitar regresar a esta pantalla
                popUpTo("createEvent") { inclusive = true }
            }
        }
    }

    // Mostrar un loading mientras se obtiene la data
    if (categoriesState is UiState.Loading) {
        ViewLoading()
    } else if (categoriesState is UiState.Error) {
        Text(
            text = (categoriesState as UiState.Error).message,
            color = MaterialTheme.colorScheme.error,
            modifier = Modifier.padding(bottom = 16.dp)
        )
    }

    // Estados para la categoria
    var expandedCategory by remember { mutableStateOf(false) }
    var selectedCategory by remember { mutableStateOf("") }
    var isFocusedCategory by remember { mutableStateOf(false) }

    when (eventsAddState) {
        is UiState.Loading -> {
            ViewLoading()
        }
        is UiState.Error -> {
            val error = (eventsAddState as UiState.Error).message
            Text("Error: $error", color = Color.Red)
        }
        UiState.Idle -> { }
        is UiState.Success -> { }
    }

    // Si ya se han obtenido las categorías, mostrar el formulario
    if (categoriesState is UiState.Success) {
        // Diálogos para seleccionar fecha y hora
        val datePickerDialog = remember {
            DatePickerDialog(
                context,
                { _, year, month, dayOfMonth ->
                    eventDate = "%02d/%02d/%04d".format(dayOfMonth, month + 1, year)
                    calendar.set(year, month, dayOfMonth)
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            )
        }

        // Para TimePickerDialog
        val timePickerDialog = TimePickerDialog(
            context,
            { _, hourOfDay, minute ->
                eventTime = "%02d:%02d".format(hourOfDay, minute)
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
                calendar.set(Calendar.MINUTE, minute)
            },
            calendar.get(Calendar.HOUR_OF_DAY),
            calendar.get(Calendar.MINUTE),
            true
        )

        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState())
                    .fillMaxWidth(),
            ) {
                Text(
                    text = "Crear Evento",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.align(Alignment.Start)
                )
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
                    label = { Text("Nombre del Evento", color = Color(0xFFABB5B2)) },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Next
                    ),
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFFffffff))
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Campo para descripción del evento
                OutlinedTextField(
                    value = eventDescription,
                    onValueChange = { eventDescription = it },
                    label = { Text("Descripción del Evento", color = Color(0xFFABB5B2)) },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Next
                    ),
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFFffffff)),
                    maxLines = 3
                )

                Spacer(modifier = Modifier.height(16.dp))


                // 3. Seleccionar categoria
                ExposedDropdownMenuBox(
                    expanded = expandedCategory,
                    onExpandedChange = {
                        expandedCategory = !expandedCategory
                    }
                ) {
                    // TextField que actúa como el menú desplegable
                    OutlinedTextField(
                        value = selectedCategory,
                        onValueChange = { selectedCategory = it },
                        trailingIcon = {
                            Icon(
                                imageVector = if (expandedCategory) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                                contentDescription = if (expandedCategory) "Cerrar menú" else "Abrir menú",
                                modifier = Modifier
                                    .clickable { expandedCategory = !expandedCategory }

                            )
                        },
                        textStyle = TextStyle(
                            fontSize = 16.sp,
                            lineHeight = 24.sp,
                            fontWeight = FontWeight(400)
                        ),
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor()
                            .background(Color.White, shape = RoundedCornerShape(4.dp))
                            .onFocusChanged { focusState ->
                                isFocusedCategory = focusState.isFocused
                            }
                            .then(
                                if (isFocusedCategory) Modifier
                                    .shadow(
                                        elevation = 8.dp,
                                        shape = RoundedCornerShape(4.dp),
                                        ambientColor = Color(0xFFff0705),
                                        spotColor = Color(0xFFff0705)
                                    )
                                    .border(1.dp, Color(0xFFff0705), RoundedCornerShape(4.dp))
                                else Modifier
                            ),
                        shape = RoundedCornerShape(4.dp),
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White,
                            cursorColor = Color(0xFFff0705),
                            focusedIndicatorColor = Color(0xFFff0705)/*if (name.isEmpty()) Color(0xFF006241) else Color.Red*/,
                            unfocusedIndicatorColor = Color(0xFF000000)
                        ),
                        // Etiqueta encima del campo de texto
                        placeholder = {
                            Text(
                                "Selecciona...",
                                color = Color(0xFFABB5B2)
                            )
                        },
                        readOnly = true
                    )

                    // DropdownMenu con las opciones
                    ExposedDropdownMenu(
                        modifier = Modifier
                            .background(Color.White)
                            .heightIn(140.dp),
                        expanded = expandedCategory,
                        onDismissRequest = { expandedCategory = false }
                    ) {
                        (categoriesState as UiState.Success<List<CategoryModel>>).data?.forEach { category ->
                            DropdownMenuItem(
                                text = {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween // Distribuye los elementos
                                    ) {
                                        Text(
                                            text = category.name.toString(),
                                            fontSize = 16.sp,
                                            lineHeight = 24.sp,
                                            fontWeight = FontWeight(400)
                                        )

                                        if (selectedCategory == category.name) {
                                            Icon(
                                                imageVector = Icons.Default.Check,
                                                contentDescription = "Categoría del Evento",
                                                tint = Color(0xFFff0705)
                                            )
                                        }
                                    }
                                },
                                onClick = {
                                    eventViewModel.onCategorySelectChanged(category.name ?: "")
                                    selectedCategory = category.name.toString()
                                    expandedCategory =
                                        false
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // 4. Fecha del evento
                DatePickerFieldOverlay(eventDate = eventDate, onClick = {
                    datePickerDialog.show()
                })
                Spacer(modifier = Modifier.height(16.dp))
                // 5. Hora evento
                TimePickerFieldOverlay(
                    eventTime = eventTime,
                    onClick = {
                        timePickerDialog.show()
                    }
                )

                Spacer(modifier = Modifier.height(16.dp))

                PrioritySelector(
                    selected = selectedPriority,
                    onPrioritySelected = { eventViewModel.setPriority(it) }
                )

                Button(
                    onClick = {
                        val format = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
                        val date: Date? = format.parse("$eventDate $eventTime")
                        val timestamp: Timestamp? = date?.let { Timestamp(it) }
                        val newEvent = Event(
                            name = eventName,
                            description = eventDescription,
                            date = timestamp,
                            category = eventCategory,
                            priority = selectedPriority
                        )
                        Log.v("Crear Evento", newEvent.toString())
                        eventViewModel.addEvent(newEvent)
                        //navController.popBackStack() // Volver a la lista de eventos
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Crear Evento")
                }

            }

        }
    }
}

@Composable
fun DatePickerFieldOverlay(
    eventDate: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val interactionSource = remember { MutableInteractionSource() }
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(Color(0xFFffffff))
    ) {
        OutlinedTextField(
            value = eventDate,
            onValueChange = {},
            label = { Text("Fecha", color = Color(0xFFABB5B2)) },
            readOnly = true,
            enabled = true, // mantenemos enabled para estilo correcto
            trailingIcon = {
                Icon(Icons.Default.DateRange, contentDescription = "Seleccionar Fecha")
            },
            modifier = Modifier.fillMaxWidth()
        )
        // Box invisible que intercepta clicks y está encima
        Box(
            modifier = Modifier
                .matchParentSize()
                .background(Color.Transparent)
                .clickable(
                    interactionSource = interactionSource,
                    indication = null,
                    onClick = { onClick() }
                )
        )
    }
}

@Composable
fun TimePickerFieldOverlay(
    eventTime: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val interactionSource = remember { MutableInteractionSource() }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(Color(0xFFffffff))
    ) {
        OutlinedTextField(
            value = if (eventTime.isNotEmpty()) "${eventTime} hrs." else "",
            onValueChange = {},
            label = {
                Text(
                    "Hora",
                    color = Color(0xFFABB5B2)
                )
            },
            readOnly = true,
            enabled = true,
            trailingIcon = {
                Icon(Icons.Default.DateRange, contentDescription = "Seleccionar Hora")
            },
            modifier = Modifier.fillMaxWidth()
        )
        Box(
            modifier = Modifier
                .matchParentSize()
                .background(Color.Transparent)
                .clickable(
                    interactionSource = interactionSource,
                    indication = null, // Sin efecto ripple
                    onClick = { onClick() }
                )
        )
    }
}

@Composable
fun PrioritySelector(
    selected: String,
    onPrioritySelected: (String) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "Prioridad:",
            style = MaterialTheme.typography.labelLarge,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Row(verticalAlignment = Alignment.CenterVertically) {
            listOf("Alta", "Media", "Baja").forEach { priority ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .padding(end = 16.dp)
                        .clickable { onPrioritySelected(priority) }
                ) {
                    RadioButton(
                        selected = selected == priority,
                        onClick = { onPrioritySelected(priority) }
                    )
                    Text(priority)
                }
            }
        }
    }
}

