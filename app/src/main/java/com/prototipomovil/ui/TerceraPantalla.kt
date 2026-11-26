package com.prototipomovil.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.prototipomovil.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TerceraPantalla(
    navController: NavController,
    viewModel: MainViewModel = hiltViewModel()
) {
    val tituloInicial = stringResource(R.string.detail_default_title)
    val descInicial = stringResource(R.string.detail_default_desc)

    var titulo by rememberSaveable { mutableStateOf(tituloInicial) }
    var descripcion by rememberSaveable { mutableStateOf(descInicial) }
    var recordatorioActivo by rememberSaveable { mutableStateOf(true) }

    var etiquetaTrabajo by rememberSaveable { mutableStateOf(true) }
    var etiquetaPersonal by rememberSaveable { mutableStateOf(false) }
    var etiquetaDiseno by rememberSaveable { mutableStateOf(true) }
    var etiquetaIdeas by rememberSaveable { mutableStateOf(false) }

    var prioridadSlider by rememberSaveable { mutableFloatStateOf(1f) }
    val prioridadTexto = when (prioridadSlider.toInt()) {
        0 -> stringResource(R.string.priority_low)
        2 -> stringResource(R.string.priority_high)
        else -> stringResource(R.string.priority_medium)
    }
    val tagTrabajoText = stringResource(R.string.tag_work)
    val tagPersonalText = stringResource(R.string.tag_personal)
    val tagDisenoText = stringResource(R.string.tag_design)
    val tagIdeasText = stringResource(R.string.tag_ideas)
    val mensajeEstado by viewModel.mensajeEstado.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.detail_top_title),
                        fontWeight = FontWeight.Bold
                    )
                },
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
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Text(
                text = stringResource(R.string.detail_project_tag),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.SemiBold
            )

            OutlinedTextField(
                value = titulo,
                onValueChange = { titulo = it },
                label = { Text(stringResource(R.string.detail_title_hint)) },
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = descripcion,
                onValueChange = { descripcion = it },
                label = { Text(stringResource(R.string.detail_desc_hint)) },
                minLines = 3,
                maxLines = 5,
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = stringResource(R.string.detail_reminder_title),
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = stringResource(R.string.detail_reminder_subtitle),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Switch(
                    checked = recordatorioActivo,
                    onCheckedChange = { recordatorioActivo = it }
                )
            }

            Text(
                text = stringResource(R.string.detail_tags_title),
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold
            )

            Row(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier.weight(1f),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = etiquetaTrabajo,
                        onCheckedChange = { etiquetaTrabajo = it }
                    )
                    Text(tagTrabajoText, style = MaterialTheme.typography.bodyMedium)
                }
                Row(
                    modifier = Modifier.weight(1f),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = etiquetaPersonal,
                        onCheckedChange = { etiquetaPersonal = it }
                    )
                    Text(tagPersonalText, style = MaterialTheme.typography.bodyMedium)
                }
            }

            Row(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier.weight(1f),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = etiquetaDiseno,
                        onCheckedChange = { etiquetaDiseno = it }
                    )
                    Text(tagDisenoText, style = MaterialTheme.typography.bodyMedium)
                }
                Row(
                    modifier = Modifier.weight(1f),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = etiquetaIdeas,
                        onCheckedChange = { etiquetaIdeas = it }
                    )
                    Text(tagIdeasText, style = MaterialTheme.typography.bodyMedium)
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = stringResource(R.string.detail_priority_title),
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = prioridadTexto,
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            Slider(
                value = prioridadSlider,
                onValueChange = { prioridadSlider = it },
                valueRange = 0f..2f,
                steps = 1
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = stringResource(R.string.priority_low),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = stringResource(R.string.priority_high),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            if (mensajeEstado != null) {
                Text(
                    text = mensajeEstado!!,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(
                    onClick = { navController.popBackStack() },
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp)
                ) {
                    Text(stringResource(R.string.detail_cancel_button))
                }

                Button(
                    onClick = {
                        val etiquetasSeleccionadas = buildList {
                            if (etiquetaTrabajo) add(tagTrabajoText)
                            if (etiquetaPersonal) add(tagPersonalText)
                            if (etiquetaDiseno) add(tagDisenoText)
                            if (etiquetaIdeas) add(tagIdeasText)
                        }
                        viewModel.guardarTareaDesdeDetalle(
                            titulo = titulo,
                            descripcion = descripcion,
                            recordatorioActivo = recordatorioActivo,
                            etiquetas = etiquetasSeleccionadas,
                            prioridadTexto = prioridadTexto
                        ) {
                            navController.popBackStack()
                        }
                    },
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp)
                ) {
                    Text(stringResource(R.string.detail_save_button))
                }
            }
        }
    }
}