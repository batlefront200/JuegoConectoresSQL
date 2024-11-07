
package daos;

/* Implementar métodos savePlayerProgress(), updatePlayerProgress(), 
getTopPlayers(), y getGameStats() para la gestión del progreso y estadísticas 
de videojuegos. */

import clases.*;
import java.util.ArrayList;

public interface MySQL_DAO {
    public void savePlayerProgress(Videojuegos game, Jugador plr);
    public void updatePlayerProgress(Videojuegos game, Jugador plr);
    public ArrayList<Jugador> getTopPlayers(Videojuegos game);
    public ArrayList<Jugador> getGameStats(Videojuegos game, Jugador plr);
}
