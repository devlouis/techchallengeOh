package com.example.apptechchallengeoh.home.data.repository

import com.example.apptechchallengeoh.home.data.network.ApiService
import com.example.apptechchallengeoh.home.domain.model.CategoryModel
import com.example.apptechchallengeoh.home.domain.model.Event
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject


class EventRepository @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val apiService: ApiService // Inyección del servicio API
) {

    private val eventCollection = firestore.collection("events")

    // Obtener eventos desde Firestore en tiempo real
    fun getEvents(): Flow<List<Event>> = callbackFlow {
        val snapshotListener = eventCollection.addSnapshotListener { snapshot, error ->
            if (error != null) {
                close(error)
                return@addSnapshotListener
            }

            val events = snapshot?.documents?.map { document ->
                document.toObject(Event::class.java)?.copy(id = document.id) ?: return@addSnapshotListener
            } ?: emptyList()

            trySend(events)
        }
        awaitClose { snapshotListener.remove() }
    }

    // Agregar un nuevo evento a Firestore
    suspend fun addEvent(event: Event) {
        eventCollection.add(event)
    }

    // Eliminar un evento de Firestore
    suspend fun deleteEvent(eventId: String) {
        eventCollection.document(eventId).delete()
    }

    // Marcar un evento como completado
    suspend fun markEventAsCompleted(eventId: String) {
        eventCollection.document(eventId).update("isCompleted", true)
    }

    // Obtener categorías desde la API REST
    suspend fun getCategories(): List<CategoryModel> {
        return apiService.getCategories()
    }
}
