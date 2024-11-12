
package daos;

import clases.*;
import java.util.ArrayList;

public interface RemoteDAO {
    // Videogames
    public void savePlayerProgress(Videogame game, Player plr);
    public void updatePlayerProgress(Videogame game, Player plr);
    public ArrayList<Player> getTopPlayers(Videogame game);
    public ArrayList<Player> getGameStats(Videogame game, Player plr);
    
    // Games
    public void saveGame(Videogame game);
    public Videogame getGameById(int id);
    public void deleteGameById(int id);
    
    // Player
    public Player getPlayerById(int id);
    public void updatePlayer(Player plr);
    public void deletePlayer(Player plr);
    
}
