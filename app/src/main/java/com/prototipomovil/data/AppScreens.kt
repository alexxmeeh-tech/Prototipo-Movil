package com.prototipomovil.data

sealed class AppScreens(val route: String) {
    data object PrimeraPantalla : AppScreens("primera_pantalla")
    data object RegistroPantalla : AppScreens("registro_pantalla")
    data object SegundaPantalla : AppScreens("segunda_pantalla")
    data object TerceraPantalla : AppScreens("tercera_pantalla")
    data object ListaTareasPantalla : AppScreens("lista_tareas_pantalla")
    data object BuscarPantalla : AppScreens("buscar_pantalla")
    data object AlertasPantalla : AppScreens("alertas_pantalla")
    data object PerfilPantalla : AppScreens("perfil_pantalla")

    data object SeccionTareas : AppScreens("seccion_tareas/{categoria}") {
        fun crearRuta(categoria: String): String = "seccion_tareas/$categoria"
    }
}