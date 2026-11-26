package com.prototipomovil.domain.model

data class Usuario(
    val idUsuario: Int = 0,
    val nombre: String = "",
    val correo: String = "",
    val passwordHash: String = "",
    val fechaRegistro: Long = 0L
)