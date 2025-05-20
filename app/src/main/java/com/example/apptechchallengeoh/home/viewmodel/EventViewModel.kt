package com.example.apptechchallengeoh.home.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.apptechchallengeoh.home.domain.model.CategoryModel
import com.example.apptechchallengeoh.home.domain.model.Event
import com.example.apptechchallengeoh.home.domain.model.UiState
import com.example.apptechchallengeoh.home.domain.usecase.EventUseCase
import com.example.apptechchallengeoh.utils.optimizeSchedule
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EventViewModel @Inject constructor(private val eventUseCase: EventUseCase) : ViewModel() {

    private val _categoriesState = MutableStateFlow<UiState<List<CategoryModel>>>(UiState.Idle)
    val categoriesState: StateFlow<UiState<List<CategoryModel>>> get() = _categoriesState

    // Estado de eventos (carga, éxito, error)
    private val _eventsState = MutableStateFlow<UiState<List<Event>>?>(null)
    val eventsState: StateFlow<UiState<List<Event>>?> = _eventsState

    private val _eventsAddState = MutableStateFlow<UiState<Unit>>(UiState.Idle)
    val eventsAddState: StateFlow<UiState<Unit>> get() = _eventsAddState

    private val _category = MutableStateFlow("")
    val category:  StateFlow<String> = _category.asStateFlow()

    // Estado para la prioridad seleccionada (por defecto: bajo)
    private val _selectedPriority = MutableStateFlow("Baja")
    val selectedPriority: StateFlow<String> = _selectedPriority

    init {
        // Inicializar la obtención de eventos
        getCategories()  // Inicializar la obtención de categorías
    }

    // Obtener eventos desde Firestore en tiempo real
    fun getEvents() {
        _eventsState.value = UiState.Loading  // Mostrar el estado de carga
        viewModelScope.launch {
            try {
                eventUseCase.getEvents().collect {
                    _eventsState.value = UiState.Success(optimizeSchedule(it))  // Actualizamos el estado con el resultado
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
        _eventsAddState.value = UiState.Loading
        viewModelScope.launch {
            try {
                eventUseCase.addEvent(event)
                _eventsAddState.value = UiState.Success(Unit)
            } catch (e: Exception) {
                _eventsAddState.value = UiState.Error("Error al crear el evento: ${e.message}")  // Manejo de errores al agregar el evento
            }
        }
    }

    // Eliminar un evento
    fun deleteEvent(eventId: String) {
        viewModelScope.launch {
            try {
                eventUseCase.deleteEvent(eventId)
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

    fun setPriority(priority: String) {
        _selectedPriority.value = priority
    }





    fun onCategorySelectChanged(newMonthSelect: String) {
        _category.value = newMonthSelect
    }

}
