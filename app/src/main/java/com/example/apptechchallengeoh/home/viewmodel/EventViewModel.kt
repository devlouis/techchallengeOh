package com.example.apptechchallengeoh.home.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.apptechchallengeoh.home.domain.model.CategoryModel
import com.example.apptechchallengeoh.home.domain.model.Event
import com.example.apptechchallengeoh.home.domain.model.UiState
import com.example.apptechchallengeoh.home.domain.usecase.EventUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EventViewModel @Inject constructor(private val eventUseCase: EventUseCase) : ViewModel() {

    // Estado para eventos
    private val _events = MutableStateFlow<List<Event>>(emptyList())  // Lista de eventos
    val events: StateFlow<List<Event>> get() = _events  // Exponemos el estado para ser observado

    // Estado para categorías (ahora es una lista de Category)
    private val _categoriesState = MutableStateFlow<UiState<List<CategoryModel>>>(UiState.Idle)
    val categoriesState: StateFlow<UiState<List<CategoryModel>>> get() = _categoriesState

    // Estado de eventos (carga, éxito, error)
    private val _eventsState = MutableStateFlow<UiState<List<Event>>>(UiState.Idle)
    val eventsState: StateFlow<UiState<List<Event>>> get() = _eventsState

    init {
        getEvents()  // Inicializar la obtención de eventos
        getCategories()  // Inicializar la obtención de categorías
    }

    // Obtener eventos desde Firestore en tiempo real
    private fun getEvents() {
        _eventsState.value = UiState.Loading  // Mostrar el estado de carga
        viewModelScope.launch {
            try {
                eventUseCase.getEvents().collect {
                    _events.value = it  // Asignamos los eventos a la lista
                    _eventsState.value = UiState.Success(it)  // Actualizamos el estado con el resultado
                }
            } catch (e: Exception) {
                _eventsState.value = UiState.Error("Error al obtener los eventos: ${e.message}")  // Manejo de errores
            }
        }
    }

    // Obtener categorías desde la API
    private fun getCategories() {
        _categoriesState.value = UiState.Loading  // Mostrar el estado de carga para categorías
        viewModelScope.launch {
            try {
                val categories = eventUseCase.getCategories()  // Devuelve una lista de Category
                _categoriesState.value = UiState.Success(categories)  // Actualizamos el estado con el resultado
            } catch (e: Exception) {
                _categoriesState.value = UiState.Error("Error al obtener las categorías: ${e.message}")  // Manejo de errores
            }
        }
    }

    // Agregar un nuevo evento
    fun addEvent(event: Event) {
        viewModelScope.launch {
            try {
                eventUseCase.addEvent(event)  // Agregamos el evento
            } catch (e: Exception) {
                _eventsState.value = UiState.Error("Error al crear el evento: ${e.message}")  // Manejo de errores al agregar el evento
            }
        }
    }

    // Eliminar un evento
    fun deleteEvent(eventId: String) {
        viewModelScope.launch {
            try {
                eventUseCase.deleteEvent(eventId)  // Eliminamos el evento
            } catch (e: Exception) {
                _eventsState.value = UiState.Error("Error al eliminar el evento: ${e.message}")  // Manejo de errores al eliminar el evento
            }
        }
    }

    // Marcar un evento como completado
    fun markEventAsCompleted(eventId: String) {
        viewModelScope.launch {
            try {
                eventUseCase.markEventAsCompleted(eventId)  // Marcamos el evento como completado
            } catch (e: Exception) {
                _eventsState.value = UiState.Error("Error al marcar el evento como completado: ${e.message}")  // Manejo de errores
            }
        }
    }
}
