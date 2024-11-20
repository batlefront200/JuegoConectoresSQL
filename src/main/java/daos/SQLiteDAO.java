package daos;

import clases.Player;

public interface SQLiteDAO {

    public boolean saveConfig();

    public boolean updateConfig(String[] datosAct);

    public String[] getConfig();

    public String[] getPlayerState(int playerId);

    public boolean updatePlayerState(Player plr);

    public boolean savePlayerState(Player plr);

    public boolean deletePlayerState(int playerId);

    public String[] getPlayerByNickname(String nickname);
}
