package com.prototipomovil.data.repository

import com.prototipomovil.data.database.dao.UsuarioDao
import com.prototipomovil.data.mapper.toDomain
import com.prototipomovil.data.mapper.toEntity
import com.prototipomovil.domain.model.Usuario
import com.prototipomovil.domain.repository.UsuarioRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UsuarioRepositoryImpl @Inject constructor(
    private val usuarioDao: UsuarioDao
) : UsuarioRepository {

    override suspend fun insertarUsuario(usuario: Usuario) {
        usuarioDao.insertarUsuario(usuario.toEntity())
    }

    override fun obtenerUsuarios(): Flow<List<Usuario>> {
        return usuarioDao.obtenerUsuarios().map { lista ->
            lista.map { it.toDomain() }
        }
    }
}