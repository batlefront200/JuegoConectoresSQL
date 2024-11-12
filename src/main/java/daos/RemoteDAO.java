
package daos;

import clases.*;
import java.util.ArrayList;

public interface RemoteDAO {
    public void savePlayerProgress(Videojuegos game, Jugador plr);
    public void updatePlayerProgress(Videojuegos game, Jugador plr);
    public ArrayList<Jugador> getTopPlayers(Videojuegos game);
    public ArrayList<Jugador> getGameStats(Videojuegos game, Jugador plr);
}
