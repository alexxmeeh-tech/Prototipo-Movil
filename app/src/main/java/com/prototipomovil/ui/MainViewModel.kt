package com.prototipomovil.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prototipomovil.domain.model.Tarea
import com.prototipomovil.domain.model.Usuario
import com.prototipomovil.domain.repository.TareaRepository
import com.prototipomovil.domain.repository.UsuarioRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val usuarioRepository: UsuarioRepository,
    private val tareaRepository: TareaRepository
) : ViewModel() {

    val usuarios: StateFlow<List<Usuario>> = usuarioRepository.obtenerUsuarios()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )

    private val _todasLasTareas: StateFlow<List<Tarea>> = tareaRepository.obtenerTareas()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )

    private val _usuarioActivo = MutableStateFlow<Usuario?>(null)
    val usuarioActivo: StateFlow<Usuario?> = _usuarioActivo.asStateFlow()

    private val _tareasOcultas = MutableStateFlow<Set<Int>>(emptySet())
    val tareasOcultas: StateFlow<Set<Int>> = _tareasOcultas.asStateFlow()

    val tareas: StateFlow<List<Tarea>> = combine(
        _todasLasTareas,
        _usuarioActivo,
        _tareasOcultas
    ) { listaTareas, usuario, ocultas ->
        val porUsuario = if (usuario != null && usuario.idUsuario != 0) {
            listaTareas.filter { it.idUsuario == usuario.idUsuario }
        } else {
            emptyList()
        }
        porUsuario.filterNot { it.idTarea in ocultas }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = emptyList()
    )

    private val _mensajeEstado = MutableStateFlow<String?>(null)
    val mensajeEstado: StateFlow<String?> = _mensajeEstado.asStateFlow()

    fun limpiarMensaje() {
        _mensajeEstado.value = null
    }

    fun registrarUsuario(
        nombre: String,
        correo: String,
        password: String,
        onSuccess: () -> Unit = {}
    ) {
        val cleanNombre = nombre.trim()
        val cleanCorreo = correo.trim()
        val cleanPassword = password.trim()

        if (cleanNombre.isEmpty() || cleanCorreo.isEmpty() || cleanPassword.isEmpty()) {
            _mensajeEstado.value = "Completa nombre, correo y contrasena."
            return
        }

        viewModelScope.launch {
            try {
                val ahora = System.currentTimeMillis()
                val nuevoUsuario = Usuario(
                    nombre = cleanNombre,
                    correo = cleanCorreo,
                    passwordHash = cleanPassword.hashCode().toString(),
                    fechaRegistro = ahora
                )
                usuarioRepository.insertarUsuario(nuevoUsuario)
                val insertado = usuarioRepository.obtenerUsuarios().first()
                    .lastOrNull { it.correo.equals(cleanCorreo, ignoreCase = true) }
                _usuarioActivo.value = insertado ?: nuevoUsuario
                _tareasOcultas.value = emptySet()
                _mensajeEstado.value = "Usuario guardado correctamente."
                onSuccess()
            } catch (e: Exception) {
                _mensajeEstado.value = "Error al guardar usuario: ${e.message ?: "error desconocido"}"
            }
        }
    }

    fun iniciarSesion(
        correo: String,
        password: String,
        onResult: (Boolean) -> Unit = {}
    ) {
        val cleanCorreo = correo.trim()
        val cleanPassword = password.trim()
        if (cleanCorreo.isEmpty()) {
            _mensajeEstado.value = "Ingresa tu correo electronico."
            onResult(false)
            return
        }

        viewModelScope.launch {
            try {
                val lista = usuarioRepository.obtenerUsuarios().first()
                val encontrado = lista.firstOrNull {
                    it.correo.equals(cleanCorreo, ignoreCase = true) &&
                            (cleanPassword.isEmpty() || it.passwordHash == cleanPassword.hashCode().toString())
                }
                if (encontrado != null) {
                    _usuarioActivo.value = encontrado
                    _tareasOcultas.value = emptySet()
                    _mensajeEstado.value = null
                    onResult(true)
                } else {
                    _mensajeEstado.value = "Usuario o contrasena no encontrados."
                    onResult(false)
                }
            } catch (e: Exception) {
                _mensajeEstado.value = "Error al consultar usuarios: ${e.message ?: "error desconocido"}"
                onResult(false)
            }
        }
    }

    fun cerrarSesion() {
        _usuarioActivo.value = null
        _tareasOcultas.value = emptySet()
    }

    fun guardarTareaDesdeDetalle(
        titulo: String,
        descripcion: String,
        recordatorioActivo: Boolean,
        etiquetas: List<String>,
        prioridadTexto: String,
        onSuccess: () -> Unit = {}
    ) {
        val cleanTitulo = titulo.trim()
        if (cleanTitulo.isEmpty()) {
            _mensajeEstado.value = "Escribe un titulo para la tarea."
            return
        }

        viewModelScope.launch {
            try {
                val usuarioId = _usuarioActivo.value?.idUsuario
                if (usuarioId == null || usuarioId == 0) {
                    _mensajeEstado.value = "Inicia sesion o registrate antes de crear tareas."
                    return@launch
                }

                val detalleBuilder = StringBuilder()
                if (descripcion.isNotBlank()) {
                    detalleBuilder.append(descripcion.trim())
                }
                detalleBuilder.append(" | Prioridad: $prioridadTexto")
                detalleBuilder.append(" | Recordatorio: ${if (recordatorioActivo) "Activado" else "Desactivado"}")
                if (etiquetas.isNotEmpty()) {
                    detalleBuilder.append(" | Etiquetas: ${etiquetas.joinToString(", ")}")
                }

                val ahora = System.currentTimeMillis()
                val nuevaTarea = Tarea(
                    idUsuario = usuarioId,
                    titulo = cleanTitulo,
                    descripcion = detalleBuilder.toString(),
                    estado = "PENDIENTE",
                    fechaLimite = ahora + 86_400_000L,
                    fechaCreacion = ahora
                )
                tareaRepository.insertarTarea(nuevaTarea)
                _mensajeEstado.value = "Tarea guardada en la base de datos."
                onSuccess()
            } catch (e: Exception) {
                _mensajeEstado.value = "Error al guardar tarea: ${e.message ?: "error desconocido"}"
            }
        }
    }

    fun alternarEstadoTarea(tarea: Tarea) {
        viewModelScope.launch {
            try {
                val nuevoEstado = if (tarea.estado.equals("COMPLETADA", ignoreCase = true)) {
                    "PENDIENTE"
                } else {
                    "COMPLETADA"
                }
                tareaRepository.insertarTarea(tarea.copy(estado = nuevoEstado))
                _mensajeEstado.value = "Estado actualizado a $nuevoEstado."
            } catch (e: Exception) {
                _mensajeEstado.value = "Error al actualizar tarea: ${e.message ?: "error desconocido"}"
            }
        }
    }

    fun eliminarTarea(tarea: Tarea) {
        _tareasOcultas.value = _tareasOcultas.value + tarea.idTarea
        _mensajeEstado.value = "Tarea quitada de la vista."
    }
}