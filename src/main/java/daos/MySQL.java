package daos;

import clases.Player;
import clases.Videogame;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class MySQL implements RemoteDAO {
    private Connection conexion;
    
    public MySQL(String url, String user, String password) {
        try {
            this.conexion = DriverManager.getConnection(url, user, password);
        } catch(SQLException e) {
            System.out.println("No se pudo realizar la conexión MySQL");
            System.out.println(e);
        }
    }

    @Override
    public void savePlayerProgress(Videogame game, Player plr) {
        String sql = "INSERT INTO Games (game_id, player_id, experience, life_level, coins, session_date) VALUES (?, ?, ?, ?, ?, NOW())";
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setInt(1, game.getId());
            stmt.setInt(2, plr.getId());
            stmt.setInt(3, plr.getExperience());
            stmt.setInt(4, plr.getLife_level());
            stmt.setInt(5, plr.getCoins());
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error al guardar el progreso del jugador: " + e.getMessage());
        }
    }

    @Override
    public void updatePlayerProgress(Videogame game, Player plr) {
        String sql = "UPDATE Players SET experience = ?, life_level = ?, coins = ?, session_count = session_count + 1, last_login = NOW() WHERE player_id = ?";
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setInt(1, plr.getExperience());
            stmt.setInt(2, plr.getLife_level());
            stmt.setInt(3, plr.getCoins());
            stmt.setInt(4, plr.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error al actualizar el progreso del jugador: " + e.getMessage());
        }
    }

    @Override
    public ArrayList<Player> getTopPlayers(Videogame game) {
        ArrayList<Player> topPlayers = new ArrayList<>();
        String sql = "SELECT p.player_id, p.nick_name, p.experience, p.life_level, p.coins, p.session_count, p.last_login "
                   + "FROM Players p JOIN Games g ON p.player_id = g.player_id WHERE g.game_id = ? "
                   + "ORDER BY p.experience DESC LIMIT 10";
        
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setInt(1, game.getId());
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Player player = new Player(
                    rs.getInt("player_id"),
                    rs.getString("nick_name"),
                    rs.getInt("experience"),
                    rs.getInt("life_level"),
                    rs.getInt("coins"),
                    rs.getInt("session_count"),
                    rs.getTimestamp("last_login").toLocalDateTime()
                );
                topPlayers.add(player);
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener los jugadores destacados: " + e.getMessage());
        }
        return topPlayers;
    }

    @Override
    public ArrayList<Player> getGameStats(Videogame game, Player plr) {
        ArrayList<Player> gameStats = new ArrayList<>();
        String sql = "SELECT g.experience, g.life_level, g.coins, g.session_date "
                   + "FROM Games g WHERE g.game_id = ? AND g.player_id = ? ORDER BY g.session_date DESC";
        
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setInt(1, game.getId());
            stmt.setInt(2, plr.getId());
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Player playerStats = new Player();
                playerStats.setExperience(rs.getInt("experience"));
                playerStats.setLife_level(rs.getInt("life_level"));
                playerStats.setCoins(rs.getInt("coins"));
                playerStats.setLast_login(rs.getTimestamp("session_date").toLocalDateTime());
                gameStats.add(playerStats);
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener las estadísticas del juego: " + e.getMessage());
        }
        return gameStats;
    }

    // Métodos adicionales aún no implementados
    @Override
    public void saveGame(Videogame game) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Videogame getGameById(int id) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void deleteGameById(int id) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Player getPlayerById(int id) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void updatePlayer(Player plr) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void deletePlayer(Player plr) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
