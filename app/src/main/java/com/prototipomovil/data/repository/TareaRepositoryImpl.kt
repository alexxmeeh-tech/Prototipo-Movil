package com.prototipomovil.data.repository

import com.prototipomovil.data.database.dao.TareaDao
import com.prototipomovil.data.mapper.toDomain
import com.prototipomovil.data.mapper.toEntity
import com.prototipomovil.domain.model.Tarea
import com.prototipomovil.domain.repository.TareaRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TareaRepositoryImpl @Inject constructor(
    private val tareaDao: TareaDao
) : TareaRepository {

    override suspend fun insertarTarea(tarea: Tarea) {
        tareaDao.insertarTarea(tarea.toEntity())
    }

    override fun obtenerTareas(): Flow<List<Tarea>> {
        return tareaDao.obtenerTareas().map { lista ->
            lista.map { it.toDomain() }
        }
    }
}