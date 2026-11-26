package com.prototipomovil.data.mapper

import com.prototipomovil.data.database.entity.TareaEntity
import com.prototipomovil.data.database.entity.UsuarioEntity
import com.prototipomovil.domain.model.Tarea
import com.prototipomovil.domain.model.Usuario

fun UsuarioEntity.toDomain(): Usuario = Usuario(
    idUsuario = idUsuario,
    nombre = nombre,
    correo = correo,
    passwordHash = passwordHash,
    fechaRegistro = fechaRegistro
)

fun Usuario.toEntity(): UsuarioEntity = UsuarioEntity(
    idUsuario = idUsuario,
    nombre = nombre,
    correo = correo,
    passwordHash = passwordHash,
    fechaRegistro = fechaRegistro
)

fun TareaEntity.toDomain(): Tarea = Tarea(
    idTarea = idTarea,
    idUsuario = idUsuario,
    titulo = titulo,
    descripcion = descripcion,
    estado = estado,
    fechaLimite = fechaLimite,
    fechaCreacion = fechaCreacion
)

fun Tarea.toEntity(): TareaEntity = TareaEntity(
    idTarea = idTarea,
    idUsuario = idUsuario,
    titulo = titulo,
    descripcion = descripcion,
    estado = estado,
    fechaLimite = fechaLimite,
    fechaCreacion = fechaCreacion
)