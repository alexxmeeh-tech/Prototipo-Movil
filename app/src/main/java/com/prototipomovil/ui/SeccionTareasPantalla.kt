package com.prototipomovil.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.DeleteOutline
import androidx.compose.material.icons.rounded.RadioButtonUnchecked
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.prototipomovil.R
import com.prototipomovil.data.AppScreens
import com.prototipomovil.domain.model.Tarea

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SeccionTareasPantalla(
    categoria: String,
    navController: NavController,
    viewModel: MainViewModel = hiltViewModel()
) {
    val tareasVisibles by viewModel.tareas.collectAsState()

    val tituloSeccion = when (categoria.lowercase()) {
        "mi_dia" -> stringResource(R.string.card_my_day_title)
        "proyectos" -> stringResource(R.string.card_projects_title)
        "importantes" -> stringResource(R.string.card_important_title)
        "completadas" -> stringResource(R.string.card_completed_title)
        else -> stringResource(R.string.section_all_tasks_title)
    }

    val tareasFiltradas = remember(tareasVisibles, categoria) {
        when (categoria.lowercase()) {
            "completadas" -> tareasVisibles.filter {
                it.estado.equals("COMPLETADA", ignoreCase = true)
            }
            "proyectos" -> tareasVisibles.filter {
                it.descripcion.contains("Trabajo", ignoreCase = true) ||
                        it.descripcion.contains("Diseño", ignoreCase = true) ||
                        it.descripcion.contains("Diseno", ignoreCase = true) ||
                        it.titulo.contains("proyecto", ignoreCase = true)
            }
            "importantes" -> tareasVisibles.filter {
                it.descripcion.contains("Alta", ignoreCase = true) ||
                        it.descripcion.contains("Media", ignoreCase = true) ||
                        it.descripcion.contains("Recordatorio: Activado", ignoreCase = true)
            }
            "mi_dia" -> tareasVisibles.filter {
                !it.estado.equals("COMPLETADA", ignoreCase = true)
            }
            else -> tareasVisibles
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(tituloSeccion, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                            contentDescription = stringResource(R.string.cd_back)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate(AppScreens.TerceraPantalla.route) },
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                shape = RoundedCornerShape(16.dp)
            ) {
                Icon(
                    imageVector = Icons.Rounded.Add,
                    contentDescription = stringResource(R.string.fab_new_task_cd)
                )
            }
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(vertical = 16.dp)
        ) {
            if (tareasFiltradas.isEmpty()) {
                item {
                    ElevatedCard(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.elevatedCardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceContainerLow
                        ),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(20.dp)) {
                            Text(
                                text = stringResource(R.string.section_empty_title_format, tituloSeccion),
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = stringResource(R.string.section_empty_subtitle),
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            } else {
                items(tareasFiltradas, key = { it.idTarea }) { tarea ->
                    TareaItemCard(
                        tarea = tarea,
                        onToggleCompletada = { viewModel.alternarEstadoTarea(tarea) },
                        onEliminar = { viewModel.eliminarTarea(tarea) }
                    )
                }
            }
        }
    }
}

@Composable
private fun TareaItemCard(
    tarea: Tarea,
    onToggleCompletada: () -> Unit,
    onEliminar: () -> Unit
) {
    val esCompletada = tarea.estado.equals("COMPLETADA", ignoreCase = true)

    ElevatedCard(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerLow
        ),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onToggleCompletada) {
                Icon(
                    imageVector = if (esCompletada) {
                        Icons.Rounded.CheckCircle
                    } else {
                        Icons.Rounded.RadioButtonUnchecked
                    },
                    contentDescription = stringResource(R.string.task_toggle_status_cd),
                    tint = if (esCompletada) {
                        MaterialTheme.colorScheme.primary
                    } else {
                        MaterialTheme.colorScheme.onSurfaceVariant
                    }
                )
            }

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = tarea.titulo,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = tarea.descripcion,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = stringResource(R.string.task_status_format, tarea.estado),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            IconButton(onClick = onEliminar) {
                Icon(
                    imageVector = Icons.Rounded.DeleteOutline,
                    contentDescription = stringResource(R.string.task_delete_cd),
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}