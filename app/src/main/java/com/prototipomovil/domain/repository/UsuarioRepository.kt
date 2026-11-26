package com.prototipomovil.domain.repository

import com.prototipomovil.domain.model.Usuario
import kotlinx.coroutines.flow.Flow

interface UsuarioRepository {
    suspend fun insertarUsuario(usuario: Usuario)
    fun obtenerUsuarios(): Flow<List<Usuario>>
}