package com.prototipomovil.data.di

import com.prototipomovil.data.repository.TareaRepositoryImpl
import com.prototipomovil.data.repository.UsuarioRepositoryImpl
import com.prototipomovil.domain.repository.TareaRepository
import com.prototipomovil.domain.repository.UsuarioRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindUsuarioRepository(
        impl: UsuarioRepositoryImpl
    ): UsuarioRepository

    @Binds
    @Singleton
    abstract fun bindTareaRepository(
        impl: TareaRepositoryImpl
    ): TareaRepository
}