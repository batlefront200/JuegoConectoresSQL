package daos;

import clases.Game;
import clases.Player;
import clases.Videogame;
import daos.RemoteDAO;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class Postgres implements RemoteDAO {

    private Connection connection;

    public Postgres(String url) {
        try {
            this.connection = DriverManager.getConnection(url);
            System.out.println("Conexión exitosa a PostgreSQL");
        } catch (SQLException e) {
            System.err.println("No se pudo realizar la conexión a PostgreSQL");
            e.printStackTrace();
        }
    }

    @Override
    public void savePlayerProgress(Videogame game, Player plr) {
        String sql = "INSERT INTO Games (game_id, player_id, experience, life_level, coins, session_date) VALUES (?, ?, ?, ?, ?, NOW())";
        try (PreparedStatement sentencia = connection.prepareStatement(sql)) {
            sentencia.setInt(1, game.getId());
            sentencia.setInt(2, plr.getId());
            sentencia.setInt(3, plr.getExperience());
            sentencia.setInt(4, plr.getLife_level());
            sentencia.setInt(5, plr.getCoins());
            sentencia.executeUpdate();
            System.out.println("Progreso del jugador guardado correctamente.");
        } catch (SQLException e) {
            System.err.println("Error al guardar el progreso del jugador: " + e.getMessage());
        }
    }

    @Override
    public ArrayList<Player> getTopPlayers() {
        ArrayList<Player> topPlayers = new ArrayList<>();
        String sql = "SELECT p.player_id, p.nick_name, p.experience, p.life_level, p.coins, p.session_count, p.last_login "
                + "FROM Players p ORDER BY p.experience DESC LIMIT 10";

        try (PreparedStatement sentencia = connection.prepareStatement(sql)) {
            ResultSet resultado = sentencia.executeQuery(); 

          
            while (resultado.next()) {
                Player player = new Player(
                        resultado.getInt("player_id"), 
                        resultado.getString("nick_name"), 
                        resultado.getInt("experience"), 
                        resultado.getInt("life_level"),
                        resultado.getInt("coins"), // Monedas
                        resultado.getInt("session_count"), 
                        resultado.getTimestamp("last_login") != null
                        ? resultado.getTimestamp("last_login").toLocalDateTime()
                        : null 
                );
                topPlayers.add(player); 
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener los jugadores destacados: " + e.getMessage());
            e.printStackTrace(); 
        }

        return topPlayers; 
    }

    @Override
    public ArrayList<Player> getGameStats(Videogame game, Player plr) {
        ArrayList<Player> gameStats = new ArrayList<>();
        String sql = "SELECT g.experience, g.life_level, g.coins, g.session_date "
                + "FROM Games g WHERE g.game_id = ? AND g.player_id = ? ORDER BY g.session_date DESC";

        try (PreparedStatement sentencia = connection.prepareStatement(sql)) {
            sentencia.setInt(1, game.getId()); 
            sentencia.setInt(2, plr.getId()); 
            ResultSet resultado = sentencia.executeQuery(); 

            
            while (resultado.next()) {
                Player playerStats = new Player();
                playerStats.setExperience(resultado.getInt("experience"));               
                playerStats.setLife_level(resultado.getInt("life_level"));              
                playerStats.setCoins(resultado.getInt("coins"));                        
                playerStats.setLast_login(resultado.getTimestamp("session_date").toLocalDateTime()); 
                gameStats.add(playerStats); 
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener las estadísticas del juego: " + e.getMessage());
            e.printStackTrace();
        }

        return gameStats; 
    }

    @Override
    public void saveGame(Videogame game) {
        String sql = "INSERT INTO Videogames (isbn, title, player_count, total_sessions, last_session) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement sentencia = connection.prepareStatement(sql)) {
            sentencia.setString(1, game.getIsbn()); 
            sentencia.setString(2, game.getTitle()); 
            sentencia.setInt(3, game.getPlayer_count()); 
            sentencia.setInt(4, game.getTotal_sessions()); 
            
            sentencia.setTimestamp(5, game.getLast_session() != null ? Timestamp.valueOf(game.getLast_session()) : null);
            sentencia.executeUpdate(); 
            System.out.println("Videojuego guardado exitosamente");
        } catch (SQLException e) {
            System.err.println("Error al guardar el videojuego: " + e.getMessage());
            e.printStackTrace(); 
        }
    }

    @Override
    public Videogame getGameById(int id) {
        Videogame game = null;
        String sql = "SELECT * FROM Videogames WHERE game_id = ?";

        try (PreparedStatement sentencia = connection.prepareStatement(sql)) {
            sentencia.setInt(1, id); 
            ResultSet rs = sentencia.executeQuery(); 

            if (rs.next()) {
                
                Timestamp lastSessionTimestamp = rs.getTimestamp("last_session");

               
                LocalDateTime lastSession = null;
                if (lastSessionTimestamp != null) {
                    lastSession = lastSessionTimestamp.toLocalDateTime();
                }

               
                game = new Videogame(
                        rs.getString("isbn"), 
                        rs.getString("title"), 
                        rs.getInt("player_count"),
                        rs.getInt("total_sessions"), 
                        lastSession 
                );
                game.setId(rs.getInt("game_id"));
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener el videojuego: " + e.getMessage());
            e.printStackTrace(); 
        }

        return game;
    }

    @Override
    public void deleteGameById(int id) {
        String sql = "DELETE FROM Videogames WHERE game_id = ?";  

        try (PreparedStatement sentencia = connection.prepareStatement(sql)) {  
            sentencia.setInt(1, id);  
            int rowsAffected = sentencia.executeUpdate();  

            if (rowsAffected > 0) {
                System.out.println("El videojuego con ID " + id + " ha sido eliminado.");
            } else {
                System.out.println("No se encontró un videojuego con ID " + id + " para eliminar.");
            }
        } catch (SQLException e) {
            System.out.println("Error al eliminar el videojuego: " + e.getMessage());
            e.printStackTrace();  
        }
    }

    @Override
    public Player getPlayerById(int id) {
        Player player = null;
        String sql = "SELECT * FROM Players WHERE player_id = ?";  

        try (PreparedStatement sentencia = connection.prepareStatement(sql)) {  
            sentencia.setInt(1, id);  
            ResultSet rs = sentencia.executeQuery();  

            if (rs.next()) {  // Si hay un resultado
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
            e.printStackTrace();  // Para depuración
        }

        return player;
    }

    @Override
    public void updatePlayer(Player plr) {
        String sql = "UPDATE Players SET nick_name = ?, experience = ?, life_level = ?, coins = ?, session_count = ?, last_login = ? WHERE player_id = ?";

        try (PreparedStatement sentencia = connection.prepareStatement(sql)) {  
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
            e.printStackTrace();  
        }
    }

    @Override
    public void deletePlayer(Player plr) {
        String sql = "DELETE FROM Players WHERE player_id = ?";

        try (PreparedStatement sentencia = connection.prepareStatement(sql)) {  
            sentencia.setInt(1, plr.getId());  
            sentencia.executeUpdate();  
        } catch (SQLException e) {
            System.out.println("Error al eliminar el jugador: " + e.getMessage());
            e.printStackTrace();  
        }
    }

    @Override
    public ArrayList<Player> getAllPlayers() {
        ArrayList<Player> players = new ArrayList<>();
        String sql = "SELECT * FROM Players";  

        try (PreparedStatement sentencia = connection.prepareStatement(sql)) { 
            ResultSet rs = sentencia.executeQuery();  

            while (rs.next()) { 
                
                Timestamp lastLoginTimestamp = rs.getTimestamp("last_login");
                LocalDateTime lastLogin = null;
                if (lastLoginTimestamp != null) {
                    lastLogin = lastLoginTimestamp.toLocalDateTime();
                }

                
                Player player = new Player(
                        rs.getInt("player_id"), 
                        rs.getString("nick_name"), 
                        rs.getInt("experience"), 
                        rs.getInt("life_level"), 
                        rs.getInt("coins"), 
                        rs.getInt("session_count"), 
                        lastLogin 
                );
                players.add(player); 
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener todos los jugadores: " + e.getMessage());
            e.printStackTrace(); 
        }

        return players;  
    }

    @Override
    public void updateGame(Game game) {
        String sql = "UPDATE Games SET player_id = ?, experience = ?, life_level = ?, coins = ?, session_date = ? WHERE game_id = ?";

        try (PreparedStatement sentencia = connection.prepareStatement(sql)) {  
            sentencia.setInt(1, game.getPlayer_id());             
            sentencia.setInt(2, game.getExperience());           
            sentencia.setInt(3, game.getLife_level());            
            sentencia.setInt(4, game.getCoins());                
            sentencia.setTimestamp(5, java.sql.Timestamp.valueOf(game.getSession_date()));  
            sentencia.setInt(6, game.getGame_id());               

            sentencia.executeUpdate();  
        } catch (SQLException e) {
            System.out.println("Error al actualizar el juego: " + e.getMessage());
            e.printStackTrace(); 
        }
    }

    @Override
    public ArrayList<Game> getAllGames() {
        ArrayList<Game> games = new ArrayList<>();
        String sql = "SELECT * FROM Games";

        try (PreparedStatement sentencia = connection.prepareStatement(sql)) {
            ResultSet resultado = sentencia.executeQuery();

            while (resultado.next()) {
                Game game = new Game(
                        resultado.getInt("session_id"),
                        resultado.getInt("player_id"),
                        resultado.getInt("experience"),
                        resultado.getInt("life_level"),
                        resultado.getInt("coins"),
                        resultado.getTimestamp("session_date").toLocalDateTime()
                );
                game.setGame_id(resultado.getInt("game_id"));
                games.add(game);
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener todos los juegos: " + e.getMessage());
            e.printStackTrace();
        }

        return games;
    }

    @Override
    public void deleteVideogameById(int id) {
        String sql = "DELETE FROM Videogames WHERE game_id = ?";
        try (PreparedStatement resultado = connection.prepareStatement(sql)) {
            resultado.setInt(1, id);
            int rowsAffected = resultado.executeUpdate();
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
    public void deletePlayerById(int id) {
        String sql = "DELETE FROM Players WHERE player_id = ?";
        try (PreparedStatement resultado = connection.prepareStatement(sql)) {
            resultado.setInt(1, id);
            int rowsAffected = resultado.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Jugador eliminado con éxito.");
            } else {
                System.out.println("No se encontró el jugador con el ID proporcionado.");
            }
        } catch (SQLException e) {
            System.out.println("Error al eliminar el jugador: " + e.getMessage());
        }
    }

    @Override
    public void savePlayer(Player plr) {
        String sql = "INSERT INTO Players (nick_name, experience, life_level, coins, session_count, last_login) VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement resultado = connection.prepareStatement(sql)) {
            resultado.setString(1, plr.getNick_name());
            resultado.setInt(2, plr.getExperience());
            resultado.setInt(3, plr.getLife_level());
            resultado.setInt(4, plr.getCoins());
            resultado.setInt(5, plr.getSession_count());
            if (plr.getLast_login() != null) {
                resultado.setTimestamp(6, Timestamp.valueOf(plr.getLast_login()));
            } else {
                resultado.setNull(6, java.sql.Types.TIMESTAMP);
            }

            resultado.executeUpdate();
            System.out.println("Jugador guardado exitosamente.");
        } catch (SQLException e) {
            System.err.println("Error al guardar el jugador: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public ArrayList<Videogame> getAllVideogames() {
        ArrayList<Videogame> videogames = new ArrayList<>();
        String sql = "SELECT * FROM Videogames";

        try (PreparedStatement sentencia = connection.prepareStatement(sql)) {
            ResultSet resultado = sentencia.executeQuery();

            while (resultado.next()) {
                Timestamp lastSessionTimestamp = resultado.getTimestamp("last_session");
                LocalDateTime lastSession = (lastSessionTimestamp != null) ? lastSessionTimestamp.toLocalDateTime() : null;

                Videogame videogame = new Videogame(
                        resultado.getString("isbn"),
                        resultado.getString("title"),
                        resultado.getInt("player_count"),
                        resultado.getInt("total_sessions"),
                        lastSession
                );
                videogame.setId(resultado.getInt("game_id"));
                videogames.add(videogame);
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener todos los videojuegos: " + e.getMessage());
            e.printStackTrace();
        }

        return videogames;
    }

    @Override
    public void updateVideogame(Videogame game) {
        String sql = "UPDATE Videogames SET isbn = ?, title = ?, player_count = ?, total_sessions = ?, last_session = ? WHERE game_id = ?";

        try (PreparedStatement sentencia = connection.prepareStatement(sql)) {
            sentencia.setString(1, game.getIsbn());
            sentencia.setString(2, game.getTitle());
            sentencia.setInt(3, game.getPlayer_count());
            sentencia.setInt(4, game.getTotal_sessions());
            sentencia.setTimestamp(5, game.getLast_session() != null ? Timestamp.valueOf(game.getLast_session()) : null);
            sentencia.setInt(6, game.getId());

            sentencia.executeUpdate();
            System.out.println("Videojuego actualizado correctamente.");
        } catch (SQLException e) {
            System.out.println("Error al actualizar el videojuego: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public Game getGameByID(int id) {
        Game game = null;
        String sql = "SELECT * FROM Games WHERE game_id = ?";
        try (PreparedStatement sentencia = connection.prepareStatement(sql)) {
            sentencia.setInt(1, id);
            ResultSet resultado = sentencia.executeQuery();
            if (resultado.next()) {
                game = new Game(
                        resultado.getInt("game_id"),
                        resultado.getInt("player_id"),
                        resultado.getInt("experience"),
                        resultado.getInt("life_level"),
                        resultado.getInt("coins"),
                        resultado.getTimestamp("session_date").toLocalDateTime()
                );
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener el juego por ID en PostgreSQL: " + e.getMessage());
        }
        return game;
    }

    @Override
    public Player getPlayerByNickname(String nickname) {
        Player player = null;
        String sql = "SELECT * FROM Players WHERE nick_name = ?";
        try (PreparedStatement sentencia = connection.prepareStatement(sql)) {
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
                        resultado.getTimestamp("last_login") != null
                        ? resultado.getTimestamp("last_login").toLocalDateTime()
                        : null
                );
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener el jugador: " + e.getMessage());
        }
        return player;
    }

}
