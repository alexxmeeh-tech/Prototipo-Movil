package com.prototipomovil.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.prototipomovil.data.database.dao.TareaDao
import com.prototipomovil.data.database.dao.UsuarioDao
import com.prototipomovil.data.database.entity.TareaEntity
import com.prototipomovil.data.database.entity.UsuarioEntity

@Database(
    entities = [UsuarioEntity::class, TareaEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun usuarioDao(): UsuarioDao
    abstract fun tareaDao(): TareaDao
}