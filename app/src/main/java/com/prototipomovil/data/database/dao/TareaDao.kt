package com.prototipomovil.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.prototipomovil.data.database.entity.TareaEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TareaDao {

    // Operación 1: Insertar tarea
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarTarea(tarea: TareaEntity)

    // Operación 2: Consultar tareas
    @Query("SELECT * FROM tarea ORDER BY id_tarea DESC")
    fun obtenerTareas(): Flow<List<TareaEntity>>
}