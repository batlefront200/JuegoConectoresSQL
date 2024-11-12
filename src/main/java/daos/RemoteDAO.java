
package daos;

import clases.*;
import java.util.ArrayList;

public interface RemoteDAO {
    public void savePlayerProgress(Videogames game, Players plr);
    public void updatePlayerProgress(Videogames game, Players plr);
    public ArrayList<Players> getTopPlayers(Videogames game);
    public ArrayList<Players> getGameStats(Videogames game, Players plr);
}
