package PitufoBros;
import AplicacionFrontend.StartControllerDAO;
import PitufoBros.GameEngine;
import java.util.ArrayList;

public class GameThreadClass extends Thread implements StartControllerDAO {
    private ArrayList<Object> result;

    @Override
    public void run() {
        // Ejecuta el juego en un hilo separado
        result = new GameEngine().startGame();
    }

    @Override
    public ArrayList<Object> startGame() {
        this.start(); // Llama al método start() de Thread
        try {
            this.join(); // Espera a que el hilo termine
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return result; // Devuelve el resultado después de ejecutar
    }
}
