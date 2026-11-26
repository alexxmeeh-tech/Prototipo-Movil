package com.prototipomovil.domain.model

data class Tarea(
    val idTarea: Int = 0,
    val idUsuario: Int = 0,
    val titulo: String = "",
    val descripcion: String = "",
    val estado: String = "PENDIENTE",
    val fechaLimite: Long = 0L,
    val fechaCreacion: Long = 0L
)