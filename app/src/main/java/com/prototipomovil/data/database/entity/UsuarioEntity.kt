package com.prototipomovil.data.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "usuario",
    indices = [Index(value = ["correo"], unique = true)]
)
data class UsuarioEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id_usuario")
    val idUsuario: Int = 0,
    val nombre: String = "",
    val correo: String = "",
    @ColumnInfo(name = "password_hash")
    val passwordHash: String = "",
    @ColumnInfo(name = "fecha_registro")
    val fechaRegistro: Long = 0L
)