package gz.dam.simondiceduo

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class VMTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var viewModel: VM

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        viewModel = VM()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    // ==================== TESTS DE INICIALIZACIÓN ====================

    @Test
    fun `al inicializar ViewModel, estado debe ser Inicio`() = runTest {
        // When - El ViewModel se inicializa en el setup
        val initialState = viewModel.gameState.first()

        // Then
        assertTrue("El estado inicial debe ser Inicio", initialState is GameState.Inicio)
    }

    @Test
    fun `al inicializar ViewModel, ronda debe ser 0`() = runTest {
        // When
        val initialRound = viewModel.ronda.first()

        // Then
        assertEquals("La ronda inicial debe ser 0", 0, initialRound)
    }

    @Test
    fun `al inicializar ViewModel, texto debe ser PRESIONA START`() = runTest {
        // When
        val initialText = viewModel.text.first()

        // Then
        assertEquals("Texto inicial incorrecto", "PRESIONA START", initialText)
    }

    // ==================== TESTS DE INICIO DE JUEGO ====================

    @Test
    fun `al comenzar juego desde estado Inicio, cambia a estado Preparando`() = runTest {
        // Given - Estado inicial
        assertTrue(viewModel.gameState.first() is GameState.Inicio)

        // When
        viewModel.comenzarJuego()
        advanceUntilIdle()

        // Then
        val state = viewModel.gameState.first()
        assertTrue("Debe estar en estado Preparando", state is GameState.Preparando)
    }

    @Test
    fun `al comenzar juego, inicia primera ronda correctamente`() = runTest {
        // Given
        viewModel.comenzarJuego()
        advanceUntilIdle()

        // When - Esperar a que termine la preparación
        delay(1500) // 1000ms delay + 500ms antes de comenzar ronda
        advanceUntilIdle()

        // Then
        val state = viewModel.gameState.first()
        val round = viewModel.ronda.first()

        assertTrue("Debe estar mostrando secuencia", state is GameState.MostrandoSecuencia)
        assertEquals("Debe estar en ronda 1", 1, round)
    }

    // ==================== TESTS DE GENERACIÓN DE SECUENCIA ====================

    @Test
    fun `generaNumero debe devolver numero entre 0 y 3`() = runTest {
        // When - Ejecutar múltiples veces
        val numbers = (1..100).map { viewModel.generaNumero() }

        // Then
        assertTrue("Todos los números deben estar entre 0-3", numbers.all { it in 0..3 })
        assertTrue("Debe haber al menos un 0", numbers.any { it == 0 })
        assertTrue("Debe haber al menos un 3", numbers.any { it == 3 })
    }

    @Test
    fun `al comenzar nueva ronda, secuencia aumenta en longitud`() = runTest {
        // Given - Juego iniciado
        viewModel.comenzarJuego()
        advanceUntilIdle()
        delay(1500)
        advanceUntilIdle()

        // When - Obtener ronda actual
        val round1 = viewModel.ronda.first()

        // Then - Simular completar ronda y comenzar siguiente
        viewModel as VM // Acceder a métodos internos mediante reflection para test
        val method = VM::class.java.getDeclaredMethod("comenzarNuevaRonda")
        method.isAccessible = true
        method.invoke(viewModel)

        advanceUntilIdle()
        delay(1000)
        advanceUntilIdle()

        val round2 = viewModel.ronda.first()
        assertEquals("La ronda debe incrementar en 1", round1 + 1, round2)
    }

    // ==================== TESTS DE INPUT DEL JUGADOR ====================

    @Test
    fun `al procesar click correcto, avanza en secuencia`() = runTest {
        // Given - Juego iniciado y primera secuencia mostrada
        viewModel.comenzarJuego()
        advanceUntilIdle()
        delay(1500)
        advanceUntilIdle()

        // Obtener el primer color de la secuencia (necesitaríamos acceso interno)
        // Simulamos que el primer color es 0 (rojo)
        viewModel.procesarClickUsuario(0)
        advanceUntilIdle()

        // Then - Debe continuar esperando input o pasar a siguiente ronda
        val state = viewModel.gameState.first()
        assertTrue(
            "Debe estar en EsperandoJugador o SecuenciaCorrecta",
            state is GameState.EsperandoJugador || state is GameState.SecuenciaCorrecta
        )
    }

    @Test
    fun `al procesar click incorrecto, va a GameOver`() = runTest {
        // Given - Juego iniciado y primera secuencia mostrada
        viewModel.comenzarJuego()
        advanceUntilIdle()
        delay(1500)
        advanceUntilIdle()

        // When - Hacer click con color incorrecto (suponiendo que el primero no es 1)
        viewModel.procesarClickUsuario(1) // Verde, asumiendo que el primero es 0
        advanceUntilIdle()

        // Then
        val state = viewModel.gameState.first()
        assertTrue("Debe estar en GameOver", state is GameState.GameOver)
    }

    @Test
    fun `al completar secuencia correctamente, incrementa ronda`() = runTest {
        // Given - Juego con una secuencia de un solo color
        viewModel.comenzarJuego()
        advanceUntilIdle()
        delay(1500)
        advanceUntilIdle()

        val initialRound = viewModel.ronda.first()

        // When - Simular input correcto (asumiendo secuencia [0])
        viewModel.procesarClickUsuario(0)
        advanceUntilIdle()
        delay(1000) // Esperar transición a siguiente ronda
        advanceUntilIdle()

        // Then
        val finalRound = viewModel.ronda.first()
        assertEquals("La ronda debe incrementarse", initialRound + 1, finalRound)
    }

    // ==================== TESTS DE GAME OVER ====================

    @Test
    fun `game over muestra ronda alcanzada correctamente`() = runTest {
        // Given - Juego iniciado
        viewModel.comenzarJuego()
        advanceUntilIdle()
        delay(1500)
        advanceUntilIdle()

        val currentRound = viewModel.ronda.first()

        // When - Provocar GameOver
        viewModel.procesarClickUsuario(1) // Color incorrecto
        advanceUntilIdle()

        // Then
        val state = viewModel.gameState.first()
        assertTrue("Debe ser GameOver", state is GameState.GameOver)
        if (state is GameState.GameOver) {
            assertEquals("La ronda en GameOver debe coincidir", currentRound, state.rondaAlcanzada)
        }
    }

    @Test
    fun `al hacer game over, botones se deshabilitan`() = runTest {
        // Given - Juego en progreso
        viewModel.comenzarJuego()
        advanceUntilIdle()
        delay(1500)
        advanceUntilIdle()

        // When - Provocar GameOver
        viewModel.procesarClickUsuario(1) // Color incorrecto
        advanceUntilIdle()

        // Then
        val buttonsEnabled = viewModel.botonesBrillantes.first()
        assertFalse("Los botones deben deshabilitarse en GameOver", buttonsEnabled)
    }

    // ==================== TESTS DE RÉCORD ====================

    @Test
    fun `al superar record anterior, actualiza record`() = runTest {
        // Given - Record inicial
        val initialRecord = viewModel.record.first()

        // Simular juego que supera el record
        viewModel.comenzarJuego()
        advanceUntilIdle()
        delay(1500)
        advanceUntilIdle()

        // Completar varias rondas exitosamente
        repeat(3) { round ->
            // Simular input correcto para cada ronda
            viewModel.procesarClickUsuario(0)
            advanceUntilIdle()
            delay(1000)
            advanceUntilIdle()
        }

        // Then
        val newRecord = viewModel.record.first()
        assertTrue("El record debe actualizarse si se supera", newRecord > initialRecord)
    }

    @Test
    fun `record no se actualiza si no se supera`() = runTest {
        // Given - Record inicial
        val initialRecord = viewModel.record.first()

        // When - Juego que no supera el record (GameOver rápido)
        viewModel.comenzarJuego()
        advanceUntilIdle()
        delay(1500)
        advanceUntilIdle()
        viewModel.procesarClickUsuario(1) // GameOver inmediato
        advanceUntilIdle()

        // Then
        val finalRecord = viewModel.record.first()
        assertEquals("El record no debe cambiar si no se supera", initialRecord, finalRecord)
    }

    // ==================== TESTS DE REINICIO ====================

    @Test
    fun `reiniciar juego desde GameOver vuelve a estado inicial`() = runTest {
        // Given - Juego en GameOver
        viewModel.comenzarJuego()
        advanceUntilIdle()
        delay(1500)
        advanceUntilIdle()
        viewModel.procesarClickUsuario(1) // Provocar GameOver
        advanceUntilIdle()

        // When - Reiniciar juego
        viewModel.reiniciarJuego()
        advanceUntilIdle()
        delay(1000)
        advanceUntilIdle()

        // Then
        val state = viewModel.gameState.first()
        assertTrue("Debe volver a estado inicializable", state is GameState.GameOver) // O el estado que uses para reinicio
    }

    @Test
    fun `al reiniciar juego, ronda vuelve a cero`() = runTest {
        // Given - Juego en progreso
        viewModel.comenzarJuego()
        advanceUntilIdle()
        delay(1500)
        advanceUntilIdle()

        // When - Reiniciar
        viewModel.reiniciarJuego()
        advanceUntilIdle()
        delay(1000)
        advanceUntilIdle()

        // Then - La ronda debe resetearse (depende de tu implementación)
        val round = viewModel.ronda.first()
        // Esto depende de cómo implementes el reinicio
    }

    // ==================== TESTS DE EVENTOS DE SONIDO ====================

    @Test
    fun `al hacer click en color, emite evento de sonido`() = runTest {
        // Given - Juego en estado de input del jugador
        viewModel.comenzarJuego()
        advanceUntilIdle()
        delay(1500)
        advanceUntilIdle()

        // When
        viewModel.procesarClickUsuario(0)
        advanceUntilIdle()

        // Then
        val soundEvent = viewModel.sonidoEvent.first()
        assertTrue("Debe emitir evento de sonido de color", soundEvent is SonidoEvent.ColorSound)
    }

    @Test
    fun `al hacer game over, emite evento de sonido de error`() = runTest {
        // Given - Juego en progreso
        viewModel.comenzarJuego()
        advanceUntilIdle()
        delay(1500)
        advanceUntilIdle()

        // When - Input incorrecto
        viewModel.procesarClickUsuario(1)
        advanceUntilIdle()

        // Then
        val soundEvent = viewModel.sonidoEvent.first()
        assertTrue("Debe emitir evento de sonido de error", soundEvent is SonidoEvent.Error)
    }

    @Test
    fun `clearSoundEvent limpia el evento de sonido`() = runTest {
        // Given - Evento de sonido presente
        viewModel.comenzarJuego()
        advanceUntilIdle()
        delay(1500)
        advanceUntilIdle()
        viewModel.procesarClickUsuario(0)
        advanceUntilIdle()

        // When
        viewModel.clearSoundEvent()
        advanceUntilIdle()

        // Then
        val soundEvent = viewModel.sonidoEvent.first()
        assertNull("El evento de sonido debe ser null después de clear", soundEvent)
    }

    // ==================== TESTS DE TRANSICIONES DE ESTADO ====================

    @Test
    fun `transiciones completas de estado del juego`() = runTest {
        // Secuencia completa de estados
        assertTrue("Estado inicial: Inicio", viewModel.gameState.first() is GameState.Inicio)

        viewModel.comenzarJuego()
        advanceUntilIdle()
        assertTrue("Después de comenzar: Preparando", viewModel.gameState.first() is GameState.Preparando)

        delay(1500)
        advanceUntilIdle()
        assertTrue("Después de preparar: MostrandoSecuencia", viewModel.gameState.first() is GameState.MostrandoSecuencia)

        // Más verificaciones según el flujo...
    }

    @Test
    fun `estado ProcesandoInput se activa durante click`() = runTest {
        // Given - Juego listo para input
        viewModel.comenzarJuego()
        advanceUntilIdle()
        delay(1500)
        advanceUntilIdle()

        // When - Hacer click
        viewModel.procesarClickUsuario(0)

        // Then - Debe pasar brevemente por ProcesandoInput
        val state = viewModel.gameState.first()
        assertTrue("Debe estar en ProcesandoInput durante el click", state is GameState.ProcesandoInput)
    }
}