package gz.dam.simondiceduo


import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import gz.dam.simondiceduo.ui.theme.SimonBlue
import gz.dam.simondiceduo.ui.theme.SimonGreen
import gz.dam.simondiceduo.ui.theme.SimonRed
import gz.dam.simondiceduo.ui.theme.SimonYellow

// IMPORTACIONES REQUERIDAS DE TU ARCHIVO Color.kt
import gz.dam.simondiceduo.ui.theme.SimonRedDark
import gz.dam.simondiceduo.ui.theme.SimonGreenDark
import gz.dam.simondiceduo.ui.theme.SimonBlueDark
import gz.dam.simondiceduo.ui.theme.SimonYellowDark

/**
 * Clase sellada que representa todos los estados posibles del juego
 */
sealed class GameState {
    object Inicio : GameState()
    object Preparando : GameState()
    object MostrandoSecuencia : GameState()
    object EsperandoJugador : GameState()
    object ProcesandoInput : GameState()
    object SecuenciaCorrecta : GameState()
    data class GameOver(val rondaAlcanzada: Int) : GameState()
}

