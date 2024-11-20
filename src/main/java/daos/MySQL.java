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
        try (PreparedStatement sentencia = conexion.prepareStatement(sql)) {
            sentencia.setInt(1, game.getId());
            sentencia.setInt(2, plr.getId());
            sentencia.setInt(3, plr.getExperience());
            sentencia.setInt(4, plr.getLife_level());
            sentencia.setInt(5, plr.getCoins());
            sentencia.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error al guardar el progreso del jugador: " + e.getMessage());
        }
    }

    /*@Override
    public void updatePlayerProgress(Videogame game, Player plr) {
        String sql = "UPDATE Players SET experience = ?, life_level = ?, coins = ?, session_count = session_count + 1, last_login = NOW() WHERE player_id = ?";
        try (PreparedStatement sentencia = conexion.prepareStatement(sql)) {
            sentencia.setInt(1, plr.getExperience());
            sentencia.setInt(2, plr.getLife_level());
            sentencia.setInt(3, plr.getCoins());
            sentencia.setInt(4, plr.getId());
            sentencia.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error al actualizar el progreso del jugador: " + e.getMessage());
        }
    }*/
    @Override
    public ArrayList<Player> getTopPlayers() {
        ArrayList<Player> topPlayers = new ArrayList<>();
        String sql = "SELECT p.player_id, p.nick_name, p.experience, p.life_level, p.coins, p.session_count, p.last_login "
                + "FROM Players p ORDER BY p.experience DESC LIMIT 10";

        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery(); // Ejecuta la consulta SQL

            // Itera sobre los resultados y crea objetos Player
            while (rs.next()) {
                Player player = new Player(
                        rs.getInt("player_id"), // ID del jugador
                        rs.getString("nick_name"), // Apodo del jugador
                        rs.getInt("experience"), // Experiencia
                        rs.getInt("life_level"), // Nivel de vida
                        rs.getInt("coins"), // Monedas
                        rs.getInt("session_count"), // Cantidad de sesiones
                        rs.getTimestamp("last_login") != null
                        ? rs.getTimestamp("last_login").toLocalDateTime()
                        : null // Manejar valor nulo para last_login
                );
                topPlayers.add(player); // Agrega el jugador a la lista
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener los jugadores destacados: " + e.getMessage());
            e.printStackTrace(); // Para depuración
        }

        return topPlayers; // Devuelve la lista de jugadores destacados
    }

    @Override
    public ArrayList<Player> getGameStats(Videogame game, Player plr) {
        ArrayList<Player> gameStats = new ArrayList<>();
        String sql = "SELECT g.experience, g.life_level, g.coins, g.session_date "
                + "FROM Games g WHERE g.game_id = ? AND g.player_id = ? ORDER BY g.session_date DESC";

        try (PreparedStatement sentencia = conexion.prepareStatement(sql)) {
            sentencia.setInt(1, game.getId());
            sentencia.setInt(2, plr.getId());
            ResultSet rs = sentencia.executeQuery();
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
        try (PreparedStatement sentencia = conexion.prepareStatement(sql)) {
            sentencia.setString(1, game.getIsbn());
            sentencia.setString(2, game.getTitle());
            sentencia.setInt(3, game.getPlayer_count());
            sentencia.setInt(4, game.getTotal_sessions());
            sentencia.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error al guardar el videojuego: " + e.getMessage());
        }
    }

    @Override
    public Videogame getGameById(int id) {
        Videogame game = null;
        String sql = "SELECT * FROM Videogames WHERE game_id = ?";
        try (PreparedStatement sentencia = conexion.prepareStatement(sql)) {
            sentencia.setInt(1, id);
            ResultSet rs = sentencia.executeQuery();
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
        try (PreparedStatement sentencia = conexion.prepareStatement(sql)) {
            sentencia.setInt(1, id);
            sentencia.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error al eliminar el videojuego: " + e.getMessage());
        }
    }

    @Override
    public Player getPlayerById(int id) {
        Player player = null;
        String sql = "SELECT * FROM Players WHERE player_id = ?";
        try (PreparedStatement sentencia = conexion.prepareStatement(sql)) {
            sentencia.setInt(1, id);
            ResultSet resultado = sentencia.executeQuery();
            if (resultado.next()) {
                player = new Player(
                        resultado.getInt("player_id"),
                        resultado.getString("nick_name"),
                        resultado.getInt("experience"),
                        resultado.getInt("life_level"),
                        resultado.getInt("coins"),
                        resultado.getInt("session_count"),
                        resultado.getTimestamp("last_login").toLocalDateTime()
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
        try (PreparedStatement sentencia = conexion.prepareStatement(sql)) {
            sentencia.setString(1, plr.getNick_name());
            sentencia.setInt(2, plr.getExperience());
            sentencia.setInt(3, plr.getLife_level());
            sentencia.setInt(4, plr.getCoins());
            sentencia.setInt(5, plr.getSession_count());
            sentencia.setTimestamp(6, java.sql.Timestamp.valueOf(plr.getLast_login()));
            sentencia.setInt(7, plr.getId());
            sentencia.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error al actualizar el jugador: " + e.getMessage());
        }
    }

    @Override
    public void deletePlayer(Player plr) {
        String sql = "DELETE FROM Players WHERE player_id = ?";
        try (PreparedStatement sentencia = conexion.prepareStatement(sql)) {
            sentencia.setInt(1, plr.getId());
            sentencia.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error al eliminar el jugador: " + e.getMessage());
        }
    }

    @Override
    public ArrayList<Player> getAllPlayers() {
        ArrayList<Player> players = new ArrayList<>();
        String sql = "SELECT * FROM Players";

        try (PreparedStatement sentencia = conexion.prepareStatement(sql)) {
            ResultSet resultado = sentencia.executeQuery();

            while (resultado.next()) {
                Player player = new Player(
                        resultado.getInt("player_id"), // Assuming the column name is "player_id"
                        resultado.getString("nick_name"), // Assuming the column name is "nick_name"
                        resultado.getInt("experience"), // Assuming the column name is "experience"
                        resultado.getInt("life_level"), // Assuming the column name is "life_level"
                        resultado.getInt("coins"), // Assuming the column name is "coins"
                        resultado.getInt("session_count"), // Assuming the column name is "session_count"
                        resultado.getTimestamp("last_login").toLocalDateTime() // Assuming the column name is "last_login"
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
        try (PreparedStatement sentencia = conexion.prepareStatement(sql)) {
            sentencia.setInt(1, game.getPlayer_id());
            sentencia.setInt(2, game.getExperience());
            sentencia.setInt(3, game.getLife_level());
            sentencia.setInt(4, game.getCoins());
            sentencia.setTimestamp(5, java.sql.Timestamp.valueOf(game.getSession_date()));
            sentencia.setInt(6, game.getGame_id());
            sentencia.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error al actualizar el juego: " + e.getMessage());
        }
    }

    @Override
    public ArrayList<Game> getAllGames() {
        ArrayList<Game> games = new ArrayList<>();
        String sql = "SELECT * FROM Games";

        try (PreparedStatement sentencia = conexion.prepareStatement(sql)) {
            ResultSet resultado = sentencia.executeQuery();

            while (resultado.next()) {
                Game game = new Game(
                        resultado.getInt("session_id"),
                        resultado.getInt("player_id"), // Suponiendo que la columna es "player_id"
                        resultado.getInt("experience"), // Suponiendo que la columna es "experience"
                        resultado.getInt("life_level"), // Suponiendo que la columna es "life_level"
                        resultado.getInt("coins"), // Suponiendo que la columna es "coins"
                        resultado.getTimestamp("session_date").toLocalDateTime() // Suponiendo que la columna es "session_date"
                );
                game.setGame_id(resultado.getInt("game_id"));      // Suponiendo que la columna es "game_id"
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
        try (PreparedStatement sentencia = conexion.prepareStatement(sql)) {
            sentencia.setInt(1, id);
            sentencia.executeUpdate();
            System.out.println("Jugador con ID " + id + " eliminado exitosamente.");
        } catch (SQLException e) {
            System.err.println("Error al eliminar el jugador: " + e.getMessage());
        }
    }

    @Override
    public void savePlayer(Player plr) {
        String sql = "INSERT INTO players ( nick_name, experience, life_level, coins, session_count, last_login) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement sentencia = conexion.prepareStatement(sql)) {
            sentencia.setString(1, plr.getNick_name());
            sentencia.setInt(2, plr.getExperience());
            sentencia.setInt(3, plr.getLife_level());
            sentencia.setInt(4, plr.getCoins());
            sentencia.setInt(5, plr.getSession_count());
            sentencia.setTimestamp(6, plr.getLast_login() != null ? Timestamp.valueOf(plr.getLast_login()) : null); // Convertir LocalDateTime a Timestamp
            sentencia.executeUpdate();
            System.out.println("Jugador guardado exitosamente.");
        } catch (SQLException e) {
            System.err.println("Error al guardar el jugador: " + e.getMessage());
        }
    }

    @Override
    public void updateVideogame(Videogame game) {
        String sql = "UPDATE videogames SET title = ?, player_count = ?, total_sessions = ?, last_session = ? WHERE game_id = ?";
        try (PreparedStatement sentencia = conexion.prepareStatement(sql)) {
            sentencia.setString(1, game.getTitle());
            sentencia.setInt(2, game.getPlayer_count());
            sentencia.setInt(3, game.getTotal_sessions());
            sentencia.setTimestamp(4, game.getLast_session() != null ? Timestamp.valueOf(game.getLast_session()) : null); // Convertir LocalDateTime a Timestamp
            sentencia.setInt(5, game.getId());
            sentencia.executeUpdate();
            System.out.println("Videojuego actualizado exitosamente.");
        } catch (SQLException e) {
            System.err.println("Error al actualizar el videojuego: " + e.getMessage());
        }
    }

    @Override
    public ArrayList<Videogame> getAllVideogames() {
        String sql = "SELECT * FROM videogames";
        ArrayList<Videogame> games = new ArrayList<>();
        try (Statement sentencia = conexion.createStatement(); ResultSet datos = sentencia.executeQuery(sql)) {

            while (datos.next()) {
                int gameId = datos.getInt("game_id");
                String isbn = datos.getString("isbn");
                String title = datos.getString("title");
                int playerCount = datos.getInt("player_count");
                int totalSessions = datos.getInt("total_sessions");
                Timestamp lastSessionTimestamp = datos.getTimestamp("last_session");
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
        try (PreparedStatement sentencia = conexion.prepareStatement(sql)) {
            sentencia.setInt(1, id);  // Establece el ID del videojuego que quieres eliminar
            int rowsAffected = sentencia.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Videojuego eliminado con éxito.");
            } else {
                System.out.println("No se encontró el videojuego con el ID proporcionado.");
            }
        } catch (SQLException e) {
            System.out.println("Error al eliminar el videojuego: " + e.getMessage());
        }
    }

    @Override
    public Game getGameByID(int id) {
        Game game = null;
        String sql = "SELECT * FROM Games WHERE game_id = ?";
        try (PreparedStatement sentencia = conexion.prepareStatement(sql)) {
            sentencia.setInt(1, id);
            ResultSet resultado = sentencia.executeQuery();
            if (resultado.next()) {
                game = new Game(
                        resultado.getInt("game_id"), // ID del juego
                        resultado.getInt("player_id"), // ID del jugador
                        resultado.getInt("experience"), // Experiencia acumulada
                        resultado.getInt("life_level"), // Nivel de vida
                        resultado.getInt("coins"), // Monedas acumuladas
                        resultado.getTimestamp("session_date").toLocalDateTime() // Fecha y hora de la sesión
                );
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener el juego por ID: " + e.getMessage());
        }
        return game;
    }

    @Override
    public Player getPlayerByNickname(String nickname) {
        Player player = null;
        String sql = "SELECT * FROM Players WHERE nick_name = ?";
        try (PreparedStatement sentencia = conexion.prepareStatement(sql)) {
            sentencia.setString(1, nickname);
            ResultSet resultado = sentencia.executeQuery();
            if (resultado.next()) {
                player = new Player(
                        resultado.getInt("player_id"),
                        resultado.getString("nick_name"),
                        resultado.getInt("experience"),
                        resultado.getInt("life_level"),
                        resultado.getInt("coins"),
                        resultado.getInt("session_count"),
                        resultado.getTimestamp("last_login").toLocalDateTime()
                );
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener el jugador: " + e.getMessage());
        }
        return player;
    }

}
