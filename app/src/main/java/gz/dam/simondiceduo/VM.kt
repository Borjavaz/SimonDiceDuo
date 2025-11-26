package gz.dam.simondiceduo


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * ViewModel del juego Simon Dice
 */
class VM : ViewModel() {

    // Estados reactivos usando StateFlow
    private val _gameState = MutableStateFlow<GameState>(GameState.Inicio)
    val gameState: StateFlow<GameState> = _gameState.asStateFlow()

    private val _ronda = MutableStateFlow(0)
    val ronda: StateFlow<Int> = _ronda.asStateFlow()

    private val _record = MutableStateFlow(0)
    val record: StateFlow<Int> = _record.asStateFlow()

    private val _text = MutableStateFlow("PRESIONA START")
    val text: StateFlow<String> = _text.asStateFlow()

    private val _colorActivo = MutableStateFlow(-1)
    val colorActivo: StateFlow<Int> = _colorActivo.asStateFlow()

    private val _botonesBrillantes = MutableStateFlow(false)
    val botonesBrillantes: StateFlow<Boolean> = _botonesBrillantes.asStateFlow()

    private val _sonidoEvent = MutableStateFlow<SonidoEvent?>(null)
    val sonidoEvent: StateFlow<SonidoEvent?> = _sonidoEvent.asStateFlow()

    // Velocidades para mejor visibilidad
    private val velocidadMostrarColor = 800L
    private val velocidadPausaEntreColores = 400L
    private val velocidadTiempoApagado = 200L
    private val velocidadPausaEntreRondas = 1200L

    // Secuencias del juego
    private val secuencia = mutableListOf<Int>()
    private val secuenciaUsuario = mutableListOf<Int>()

    init {
        // Inicializar el juego usando Datos
        Datos.reiniciarJuego()
    }

    fun generaNumero(): Int = (0..3).random()

    fun comenzarJuego() {
        if (_gameState.value == GameState.Inicio || _gameState.value is GameState.GameOver) {
            reiniciarDatos()
            _gameState.value = GameState.Preparando
            _text.value = "PREPARADO..."
            _botonesBrillantes.value = false

            viewModelScope.launch {
                delay(1000)
                comenzarNuevaRonda()
            }
        }
    }

    private fun reiniciarDatos() {
        secuencia.clear()
        secuenciaUsuario.clear()
        _ronda.value = 0
        _colorActivo.value = -1
        _botonesBrillantes.value = false
        Datos.reiniciarJuego()
    }

    private fun comenzarNuevaRonda() {
        viewModelScope.launch {
            _gameState.value = GameState.MostrandoSecuencia
            _text.value = "OBSERVA LA SECUENCIA"
            _botonesBrillantes.value = false

            delay(500)

            // Agregar nuevo color a la secuencia
            val nuevoColor = generaNumero()
            secuencia.add(nuevoColor)
            _ronda.value = secuencia.size

            // Mostrar secuencia completa
            mostrarSecuenciaCompleta()
        }
    }

    private suspend fun mostrarSecuenciaCompleta() {
        for ((index, colorInt) in secuencia.withIndex()) {
            //ILUMINA Y DISPARA EVENTO DE SONIDO
            _colorActivo.value = colorInt
            _sonidoEvent.value = SonidoEvent.ColorSound(colorInt)
            delay(velocidadMostrarColor)

            // APAGA
            _colorActivo.value = -1

            //PAUSA (Tiempo de apagado mínimo)
            delay(velocidadTiempoApagado)

            // PAUSA ENTRE COLORES (Solo si no es el último)
            if (index < secuencia.size - 1) {
                delay(velocidadPausaEntreColores)
            }
        }

        delay(500)
        prepararTurnoJugador()
    }

    private fun prepararTurnoJugador() {
        secuenciaUsuario.clear()
        _gameState.value = GameState.EsperandoJugador
        _text.value = "TU TURNO - REPITE LA SECUENCIA"
        _botonesBrillantes.value = true
    }

}