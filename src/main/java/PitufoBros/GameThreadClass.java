package PitufoBros;
import PitufoBros.GameEngine;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.*;

public class GameThreadClass {
    private GameEngine gameEngine;
    private ExecutorService executorService;

    public GameThreadClass() {
        executorService = Executors.newSingleThreadExecutor(); // Hilo independiente
        gameEngine = new GameEngine();
    }

    // Iniciar el juego en un hilo independiente
    public void startGame() {
        executorService.submit(() -> {
            gameEngine.run(); // Ejecuta el juego en un hilo paralelo
            // Aquí podemos agregar cualquier lógica después de que el juego termine
            // Por ejemplo, notificar a la interfaz gráfica que el juego terminó.
            System.out.println("El juego ha terminado, pero la ventana principal sigue activa.");
        });
    }

    // Cerrar el servicio de hilos cuando ya no se necesite
    public void shutdown() {
        executorService.shutdown();
    }
}
