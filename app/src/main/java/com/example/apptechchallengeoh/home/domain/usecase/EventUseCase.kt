package com.example.apptechchallengeoh.home.domain.usecase

import com.example.apptechchallengeoh.home.data.repository.EventRepository
import com.example.apptechchallengeoh.home.domain.model.Event
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class EventUseCase @Inject constructor(private val eventRepository: EventRepository) {

    // Obtener eventos
    fun getEvents(): Flow<List<Event>> = eventRepository.getEvents()

    // Agregar un nuevo evento
    suspend fun addEvent(event: Event) {
        eventRepository.addEvent(event)
    }

    // Eliminar un evento
    suspend fun deleteEvent(eventId: String) {
        eventRepository.deleteEvent(eventId)
    }

    // Marcar un evento como completado
    suspend fun markEventAsCompleted(eventId: String) {
        eventRepository.markEventAsCompleted(eventId)
    }

    // Obtener categorías desde la API
    suspend fun getCategories(): List<String> {
        return eventRepository.getCategories()
    }
}
