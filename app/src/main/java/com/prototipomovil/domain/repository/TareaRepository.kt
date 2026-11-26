package com.prototipomovil.domain.repository

import com.prototipomovil.domain.model.Tarea
import kotlinx.coroutines.flow.Flow

interface TareaRepository {
    suspend fun insertarTarea(tarea: Tarea)
    fun obtenerTareas(): Flow<List<Tarea>>
}