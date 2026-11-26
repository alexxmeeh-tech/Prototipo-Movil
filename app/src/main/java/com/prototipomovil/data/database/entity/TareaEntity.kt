package com.prototipomovil.data.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "tarea",
    foreignKeys = [
        ForeignKey(
            entity = UsuarioEntity::class,
            parentColumns = ["id_usuario"],
            childColumns = ["id_usuario"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["id_usuario"])]
)
data class TareaEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id_tarea")
    val idTarea: Int = 0,
    @ColumnInfo(name = "id_usuario")
    val idUsuario: Int = 0,
    val titulo: String = "",
    val descripcion: String = "",
    val estado: String = "PENDIENTE",
    @ColumnInfo(name = "fecha_limite")
    val fechaLimite: Long = 0L,
    @ColumnInfo(name = "fecha_creacion")
    val fechaCreacion: Long = 0L
)