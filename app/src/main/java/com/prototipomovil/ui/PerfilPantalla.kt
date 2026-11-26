package com.prototipomovil.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.prototipomovil.R
import com.prototipomovil.data.AppScreens

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PerfilPantalla(
    navController: NavController,
    viewModel: MainViewModel = hiltViewModel()
) {
    val usuarioActivo by viewModel.usuarioActivo.collectAsState()
    val usuarios by viewModel.usuarios.collectAsState()
    val tareas by viewModel.tareas.collectAsState()

    val defaultName = stringResource(R.string.default_user_name)
    val defaultEmail = stringResource(R.string.default_user_email)
    val defaultInitial = stringResource(R.string.default_user_initial)

    val usuarioMostrado = usuarioActivo ?: usuarios.firstOrNull()
    val nombreMostrado = usuarioMostrado?.nombre ?: defaultName
    val correoMostrado = usuarioMostrado?.correo ?: defaultEmail
    val inicial = nombreMostrado.firstOrNull()?.uppercase() ?: defaultInitial

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.profile_top_title),
                        fontWeight = FontWeight.Bold
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        },
        bottomBar = {
            BarraInferiorApp(
                rutaActual = AppScreens.PerfilPantalla.route,
                navController = navController
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Surface(
                modifier = Modifier
                    .size(72.dp)
                    .clip(CircleShape),
                color = MaterialTheme.colorScheme.primaryContainer
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        text = inicial,
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }

            Text(
                text = nombreMostrado,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = correoMostrado,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            ElevatedCard(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.elevatedCardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainerLow
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = stringResource(R.string.profile_db_summary_title),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(stringResource(R.string.profile_users_count_format, usuarios.size))
                    Text(stringResource(R.string.profile_tasks_count_format, tareas.size))
                }
            }

            Button(
                onClick = { navController.navigate(AppScreens.RegistroPantalla.route) },
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
            ) {
                Text(stringResource(R.string.profile_register_new_button))
            }

            OutlinedButton(
                onClick = {
                    viewModel.cerrarSesion()
                    navController.navigate(AppScreens.PrimeraPantalla.route) {
                        popUpTo(AppScreens.PrimeraPantalla.route) { inclusive = true }
                    }
                },
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
            ) {
                Text(stringResource(R.string.profile_logout_button))
            }
        }
    }
}