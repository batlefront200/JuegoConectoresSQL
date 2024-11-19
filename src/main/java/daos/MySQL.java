package daos;

import clases.Game;
import clases.Player;
import clases.Videogame;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class MySQL implements RemoteDAO {

    private Connection conexion;

    public MySQL(String url, String user, String password) {
        try {
            this.conexion = DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
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

    // Métodos adicionales completados
    @Override
    public void saveGame(Videogame game) {
        String sql = "INSERT INTO Videogames (isbn, title, player_count, total_sessions, last_session) VALUES (?, ?, ?, ?, NOW())";
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setString(1, game.getIsbn());
            stmt.setString(2, game.getTitle());
            stmt.setInt(3, game.getPlayer_count());
            stmt.setInt(4, game.getTotal_sessions());
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error al guardar el videojuego: " + e.getMessage());
        }
    }

    @Override
    public Videogame getGameById(int id) {
        Videogame game = null;
        String sql = "SELECT * FROM Videogames WHERE game_id = ?";
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                game = new Videogame(
                        rs.getInt("game_id"),
                        rs.getString("isbn"),
                        rs.getString("title"),
                        rs.getInt("player_count"),
                        rs.getInt("total_sessions"),
                        rs.getTimestamp("last_session").toLocalDateTime()
                );
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener el videojuego: " + e.getMessage());
        }
        return game;
    }

    @Override
    public void deleteGameById(int id) {
        String sql = "DELETE FROM Videogames WHERE game_id = ?";
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error al eliminar el videojuego: " + e.getMessage());
        }
    }

    @Override
    public Player getPlayerById(int id) {
        Player player = null;
        String sql = "SELECT * FROM Players WHERE player_id = ?";
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                player = new Player(
                        rs.getInt("player_id"),
                        rs.getString("nick_name"),
                        rs.getInt("experience"),
                        rs.getInt("life_level"),
                        rs.getInt("coins"),
                        rs.getInt("session_count"),
                        rs.getTimestamp("last_login").toLocalDateTime()
                );
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener el jugador: " + e.getMessage());
        }
        return player;
    }

    @Override
    public void updatePlayer(Player plr) {
        String sql = "UPDATE Players SET nick_name = ?, experience = ?, life_level = ?, coins = ?, session_count = ?, last_login = ? WHERE player_id = ?";
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setString(1, plr.getNick_name());
            stmt.setInt(2, plr.getExperience());
            stmt.setInt(3, plr.getLife_level());
            stmt.setInt(4, plr.getCoins());
            stmt.setInt(5, plr.getSession_count());
            stmt.setTimestamp(6, java.sql.Timestamp.valueOf(plr.getLast_login()));
            stmt.setInt(7, plr.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error al actualizar el jugador: " + e.getMessage());
        }
    }

    @Override
    public void deletePlayer(Player plr) {
        String sql = "DELETE FROM Players WHERE player_id = ?";
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setInt(1, plr.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error al eliminar el jugador: " + e.getMessage());
        }
    }

    @Override
    public ArrayList<Player> getAllPlayers() {
        ArrayList<Player> players = new ArrayList<>();
        String sql = "SELECT * FROM Players";

        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Player player = new Player(
                        rs.getInt("player_id"), // Assuming the column name is "player_id"
                        rs.getString("nick_name"), // Assuming the column name is "nick_name"
                        rs.getInt("experience"), // Assuming the column name is "experience"
                        rs.getInt("life_level"), // Assuming the column name is "life_level"
                        rs.getInt("coins"), // Assuming the column name is "coins"
                        rs.getInt("session_count"), // Assuming the column name is "session_count"
                        rs.getTimestamp("last_login").toLocalDateTime() // Assuming the column name is "last_login"
                );
                players.add(player);
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener todos los jugadores: " + e.getMessage());
        }

        return players;
    }

    @Override
    public void updateGame(Game game) {
        String sql = "UPDATE Games SET player_id = ?, experience = ?, life_level = ?, coins = ?, session_date = ? WHERE game_id = ?";
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setInt(1, game.getPlayer_id());
            stmt.setInt(2, game.getExperience());
            stmt.setInt(3, game.getLife_level());
            stmt.setInt(4, game.getCoins());
            stmt.setTimestamp(5, java.sql.Timestamp.valueOf(game.getSession_date()));
            stmt.setInt(6, game.getGame_id());
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error al actualizar el juego: " + e.getMessage());
        }
    }

    @Override
    public ArrayList<Game> getAllGames() {
        ArrayList<Game> games = new ArrayList<>();
        String sql = "SELECT * FROM Games";

        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Game game = new Game(
                        rs.getInt("session_id"),
                        rs.getInt("player_id"), // Suponiendo que la columna es "player_id"
                        rs.getInt("experience"), // Suponiendo que la columna es "experience"
                        rs.getInt("life_level"), // Suponiendo que la columna es "life_level"
                        rs.getInt("coins"), // Suponiendo que la columna es "coins"
                        rs.getTimestamp("session_date").toLocalDateTime() // Suponiendo que la columna es "session_date"
                );
                game.setGame_id(rs.getInt("game_id"));      // Suponiendo que la columna es "game_id"
                games.add(game);
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener todos los juegos: " + e.getMessage());
        }

        return games;
    }

    @Override
    public void deletePlayerById(int id) {
        String sql = "DELETE FROM players WHERE player_id = ?";
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
            System.out.println("Jugador con ID " + id + " eliminado exitosamente.");
        } catch (SQLException e) {
            System.err.println("Error al eliminar el jugador: " + e.getMessage());
        }
    }

    @Override
    public void savePlayer(Player plr) {
        String sql = "INSERT INTO players ( nick_name, experience, life_level, coins, session_count, last_login) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setString(1, plr.getNick_name());
            stmt.setInt(2, plr.getExperience());
            stmt.setInt(3, plr.getLife_level());
            stmt.setInt(4, plr.getCoins());
            stmt.setInt(5, plr.getSession_count());
            stmt.setTimestamp(6, plr.getLast_login() != null ? Timestamp.valueOf(plr.getLast_login()) : null); // Convertir LocalDateTime a Timestamp
            stmt.executeUpdate();
            System.out.println("Jugador guardado exitosamente.");
        } catch (SQLException e) {
            System.err.println("Error al guardar el jugador: " + e.getMessage());
        }
    }

    @Override
    public void updateVideogame(Videogame game) {
        String sql = "UPDATE videogames SET title = ?, player_count = ?, total_sessions = ?, last_session = ? WHERE game_id = ?";
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setString(1, game.getTitle());
            stmt.setInt(2, game.getPlayer_count());
            stmt.setInt(3, game.getTotal_sessions());
            stmt.setTimestamp(4, game.getLast_session() != null ? Timestamp.valueOf(game.getLast_session()) : null); // Convertir LocalDateTime a Timestamp
            stmt.setInt(5, game.getId());
            stmt.executeUpdate();
            System.out.println("Videojuego actualizado exitosamente.");
        } catch (SQLException e) {
            System.err.println("Error al actualizar el videojuego: " + e.getMessage());
        }
    }

    @Override
    public ArrayList<Videogame> getAllVideogames() {
        String sql = "SELECT * FROM videogames";
        ArrayList<Videogame> games = new ArrayList<>();
        try (Statement stmt = conexion.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                int gameId = rs.getInt("game_id");
                String isbn = rs.getString("isbn");
                String title = rs.getString("title");
                int playerCount = rs.getInt("player_count");
                int totalSessions = rs.getInt("total_sessions");
                Timestamp lastSessionTimestamp = rs.getTimestamp("last_session");
                LocalDateTime lastSession = (lastSessionTimestamp != null) ? lastSessionTimestamp.toLocalDateTime() : null;
                games.add(new Videogame(gameId, isbn, title, playerCount, totalSessions, lastSession));
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener los videojuegos: " + e.getMessage());
        }
        return games;
    }

    @Override
    public void deleteVideogameById(int id) {
        String sql = "DELETE FROM Videogames WHERE game_id = ?";
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setInt(1, id);  // Establece el ID del videojuego que quieres eliminar
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Videojuego eliminado con éxito.");
            } else {
                System.out.println("No se encontró el videojuego con el ID proporcionado.");
            }
        } catch (SQLException e) {
            System.out.println("Error al eliminar el videojuego: " + e.getMessage());
        }
    }

}
