
package daos;

import clases.*;
import java.util.ArrayList;

public interface RemoteDAO {
    public Player getPlayerByNickname(String nickname);
    public void savePlayerProgress(Videogame game, Player plr);
    public ArrayList<Player> getTopPlayers(Videogame game);
    public ArrayList<Player> getGameStats(Videogame game, Player plr);
    public void deleteVideogameById(int id);
    public void savePlayer(Player plr);
    
    public void saveGame(Videogame game);
    public Videogame getGameById(int id);
    public void updateGame(Game game);
    public ArrayList<Game> getAllGames();
    public void deleteGameById(int id);
    public void updateVideogame(Videogame game) ;
    public ArrayList<Player> getAllPlayers();
    public Player getPlayerById(int id);
    public void updatePlayer(Player plr);
    public void deletePlayer(Player plr);
    public void deletePlayerById(int id);
    public ArrayList<Videogame> getAllVideogames();
    
    public Game getGameByID(int id);
    
}
