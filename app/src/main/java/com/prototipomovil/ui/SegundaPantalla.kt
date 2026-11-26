package com.prototipomovil.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.prototipomovil.R
import com.prototipomovil.data.AppScreens
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun BarraInferiorApp(
    rutaActual: String,
    navController: NavController
) {
    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surface
    ) {
        NavigationBarItem(
            icon = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_task_logo),
                    contentDescription = stringResource(R.string.nav_home)
                )
            },
            label = { Text(stringResource(R.string.nav_home)) },
            selected = rutaActual == AppScreens.SegundaPantalla.route,
            onClick = {
                if (rutaActual != AppScreens.SegundaPantalla.route) {
                    navController.navigate(AppScreens.SegundaPantalla.route) {
                        popUpTo(AppScreens.SegundaPantalla.route) { inclusive = true }
                    }
                }
            }
        )
        NavigationBarItem(
            icon = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_task_item),
                    contentDescription = stringResource(R.string.nav_search)
                )
            },
            label = { Text(stringResource(R.string.nav_search)) },
            selected = rutaActual == AppScreens.BuscarPantalla.route,
            onClick = {
                if (rutaActual != AppScreens.BuscarPantalla.route) {
                    navController.navigate(AppScreens.BuscarPantalla.route)
                }
            }
        )
        NavigationBarItem(
            icon = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_task_logo),
                    contentDescription = stringResource(R.string.nav_alerts)
                )
            },
            label = { Text(stringResource(R.string.nav_alerts)) },
            selected = rutaActual == AppScreens.AlertasPantalla.route,
            onClick = {
                if (rutaActual != AppScreens.AlertasPantalla.route) {
                    navController.navigate(AppScreens.AlertasPantalla.route)
                }
            }
        )
        NavigationBarItem(
            icon = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_task_logo),
                    contentDescription = stringResource(R.string.nav_profile)
                )
            },
            label = { Text(stringResource(R.string.nav_profile)) },
            selected = rutaActual == AppScreens.PerfilPantalla.route,
            onClick = {
                if (rutaActual != AppScreens.PerfilPantalla.route) {
                    navController.navigate(AppScreens.PerfilPantalla.route)
                }
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SegundaPantalla(
    navController: NavController,
    viewModel: MainViewModel = hiltViewModel()
) {
    var searchQuery by rememberSaveable { mutableStateOf("") }
    val tareas by viewModel.tareas.collectAsState()
    val usuarioActivo by viewModel.usuarioActivo.collectAsState()

    val defaultName = stringResource(R.string.default_user_name)
    val defaultInitial = stringResource(R.string.default_user_initial)
    val nombreSaludo = usuarioActivo?.nombre?.substringBefore(" ")?.ifBlank { defaultName } ?: defaultName
    val inicialSaludo = nombreSaludo.firstOrNull()?.uppercase() ?: defaultInitial

    val tareasCompletadas = remember(tareas) {
        tareas.count { it.estado.equals("COMPLETADA", ignoreCase = true) }
    }
    val tareasPendientes = tareas.size - tareasCompletadas
    val proyectosEnMarcha = remember(tareas) {
        tareas.count {
            !it.estado.equals("COMPLETADA", ignoreCase = true) && (
                    it.descripcion.contains("Trabajo", ignoreCase = true) ||
                            it.descripcion.contains("Diseño", ignoreCase = true) ||
                            it.descripcion.contains("Diseno", ignoreCase = true) ||
                            it.titulo.contains("proyecto", ignoreCase = true)
                    )
        }
    }
    val importantesDestacadas = remember(tareas) {
        tareas.count {
            it.descripcion.contains("Alta", ignoreCase = true) ||
                    it.descripcion.contains("Media", ignoreCase = true) ||
                    it.descripcion.contains("Recordatorio: Activado", ignoreCase = true)
        }
    }

    Scaffold(
        bottomBar = {
            BarraInferiorApp(
                rutaActual = AppScreens.SegundaPantalla.route,
                navController = navController
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
                    painter = painterResource(id = R.drawable.ic_task_logo),
                    contentDescription = stringResource(R.string.fab_add_cd),
                    modifier = Modifier.size(24.dp)
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
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(vertical = 16.dp)
        ) {
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(R.string.greeting_format, nombreSaludo),
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        IconButton(
                            onClick = {
                                navController.navigate(
                                    AppScreens.SeccionTareas.crearRuta("todas")
                                )
                            }
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_task_item),
                                contentDescription = stringResource(R.string.cd_all_tasks),
                                tint = MaterialTheme.colorScheme.onSurface
                            )
                        }
                        Surface(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .clickable { navController.navigate(AppScreens.PerfilPantalla.route) },
                            color = MaterialTheme.colorScheme.primaryContainer
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(
                                    text = inicialSaludo,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer
                                )
                            }
                        }
                    }
                }
            }

            item {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = { Text(stringResource(R.string.search_placeholder)) },
                    leadingIcon = {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_task_item),
                            contentDescription = stringResource(R.string.cd_card_icon),
                            modifier = Modifier.size(20.dp)
                        )
                    },
                    trailingIcon = {
                        if (searchQuery.isNotBlank()) {
                            TextButton(onClick = { navController.navigate(AppScreens.BuscarPantalla.route) }) {
                                Text(stringResource(R.string.search_go))
                            }
                        }
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(24.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainerLow,
                        focusedContainerColor = MaterialTheme.colorScheme.surfaceContainerLow,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { navController.navigate(AppScreens.BuscarPantalla.route) }
                )
            }

            item {
                val fechaHoy = remember {
                    val formatter = DateTimeFormatter.ofPattern("EEEE, d 'DE' MMMM", Locale("es", "MX"))
                    LocalDate.now().format(formatter).uppercase(Locale("es", "MX"))
                }
                val totalTareas = tareas.size
                val progreso = if (totalTareas > 0) {
                    tareasCompletadas.toFloat() / totalTareas.toFloat()
                } else {
                    0f
                }
                val porcentajeTexto = "${(progreso * 100).toInt()}%"

                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            navController.navigate(AppScreens.SeccionTareas.crearRuta("completadas"))
                        }
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = fechaHoy,
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = if (totalTareas == 0) {
                                        stringResource(R.string.progress_empty_title)
                                    } else {
                                        stringResource(R.string.progress_active_title)
                                    },
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer
                                )
                            }
                            Text(
                                text = porcentajeTexto,
                                style = MaterialTheme.typography.headlineLarge,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        LinearProgressIndicator(
                            progress = { progreso },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(8.dp)
                                .clip(RoundedCornerShape(4.dp)),
                            color = MaterialTheme.colorScheme.primary,
                            trackColor = MaterialTheme.colorScheme.surfaceVariant
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = stringResource(
                                R.string.progress_summary_format,
                                tareasCompletadas,
                                totalTareas
                            ),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
                        )
                    }
                }
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(R.string.section_space_title),
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Text(
                        text = stringResource(R.string.active_tasks_format, tareas.size),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.clickable {
                            navController.navigate(AppScreens.SeccionTareas.crearRuta("todas"))
                        }
                    )
                }
            }

            item {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Box(modifier = Modifier.weight(1f)) {
                            DashboardMiniCard(
                                title = stringResource(R.string.card_my_day_title),
                                subtitle = stringResource(
                                    R.string.card_my_day_subtitle_format,
                                    tareasPendientes
                                ),
                                iconRes = R.drawable.ic_task_item,
                                onClick = {
                                    navController.navigate(AppScreens.SeccionTareas.crearRuta("mi_dia"))
                                }
                            )
                        }
                        Box(modifier = Modifier.weight(1f)) {
                            DashboardMiniCard(
                                title = stringResource(R.string.card_projects_title),
                                subtitle = stringResource(
                                    R.string.card_projects_subtitle_format,
                                    proyectosEnMarcha
                                ),
                                iconRes = R.drawable.ic_task_logo,
                                onClick = {
                                    navController.navigate(AppScreens.SeccionTareas.crearRuta("proyectos"))
                                }
                            )
                        }
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Box(modifier = Modifier.weight(1f)) {
                            DashboardMiniCard(
                                title = stringResource(R.string.card_important_title),
                                subtitle = stringResource(
                                    R.string.card_important_subtitle_format,
                                    importantesDestacadas
                                ),
                                iconRes = R.drawable.ic_task_logo,
                                onClick = {
                                    navController.navigate(AppScreens.SeccionTareas.crearRuta("importantes"))
                                }
                            )
                        }
                        Box(modifier = Modifier.weight(1f)) {
                            DashboardMiniCard(
                                title = stringResource(R.string.card_completed_title),
                                subtitle = stringResource(
                                    R.string.card_completed_subtitle_format,
                                    tareasCompletadas
                                ),
                                iconRes = R.drawable.ic_task_item,
                                onClick = {
                                    navController.navigate(AppScreens.SeccionTareas.crearRuta("completadas"))
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun DashboardMiniCard(
    title: String,
    subtitle: String,
    iconRes: Int,
    onClick: () -> Unit = {}
) {
    ElevatedCard(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerLow
        ),
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = MaterialTheme.colorScheme.secondaryContainer,
                    modifier = Modifier.size(36.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            painter = painterResource(id = iconRes),
                            contentDescription = stringResource(R.string.cd_card_icon),
                            tint = MaterialTheme.colorScheme.onSecondaryContainer,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
                Icon(
                    painter = painterResource(id = R.drawable.ic_task_item),
                    contentDescription = stringResource(R.string.cd_card_icon),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(16.dp)
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}