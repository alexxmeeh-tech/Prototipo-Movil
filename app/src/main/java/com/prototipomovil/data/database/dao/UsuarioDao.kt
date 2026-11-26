package com.prototipomovil.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.prototipomovil.data.database.entity.UsuarioEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UsuarioDao {

    // Operación 1: Insertar usuario
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertarUsuario(usuario: UsuarioEntity)

    // Operación 2: Consultar usuarios
    @Query("SELECT * FROM usuario ORDER BY id_usuario DESC")
    fun obtenerUsuarios(): Flow<List<UsuarioEntity>>
}