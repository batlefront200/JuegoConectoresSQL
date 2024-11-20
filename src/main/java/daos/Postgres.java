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
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, game.getId());
            stmt.setInt(2, plr.getId());
            stmt.setInt(3, plr.getExperience());
            stmt.setInt(4, plr.getLife_level());
            stmt.setInt(5, plr.getCoins());
            stmt.executeUpdate();
            System.out.println("Progreso del jugador guardado correctamente.");
        } catch (SQLException e) {
            System.err.println("Error al guardar el progreso del jugador: " + e.getMessage());
        }
    }

    @Override
    public ArrayList<Player> getTopPlayers(Videogame game) {
        ArrayList<Player> topPlayers = new ArrayList<>();
        String sql = "SELECT p.player_id, p.nick_name, p.experience, p.life_level, p.coins, p.session_count, p.last_login "
                + "FROM Players p JOIN Games g ON p.player_id = g.player_id WHERE g.game_id = ? "
                + "ORDER BY p.experience DESC LIMIT 10";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, game.getId()); // Establece el ID del juego
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
                        rs.getTimestamp("last_login").toLocalDateTime() // Último inicio de sesión
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

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, game.getId()); // Establece el ID del videojuego
            stmt.setInt(2, plr.getId());  // Establece el ID del jugador
            ResultSet rs = stmt.executeQuery(); // Ejecuta la consulta SQL

            // Procesa los resultados
            while (rs.next()) {
                Player playerStats = new Player();
                playerStats.setExperience(rs.getInt("experience"));               // Establece la experiencia
                playerStats.setLife_level(rs.getInt("life_level"));               // Establece el nivel de vida
                playerStats.setCoins(rs.getInt("coins"));                         // Establece las monedas
                playerStats.setLast_login(rs.getTimestamp("session_date").toLocalDateTime()); // Fecha de la sesión
                gameStats.add(playerStats); // Agrega las estadísticas del jugador a la lista
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener las estadísticas del juego: " + e.getMessage());
            e.printStackTrace(); // Para depuración
        }

        return gameStats; // Devuelve la lista de estadísticas del juego
    }

    @Override
    public void saveGame(Videogame game) {
        String sql = "INSERT INTO Videogames (isbn, title, player_count, total_sessions, last_session) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, game.getIsbn()); // Establece el ISBN del videojuego
            stmt.setString(2, game.getTitle()); // Establece el título del videojuego
            stmt.setInt(3, game.getPlayer_count()); // Establece la cantidad de jugadores
            stmt.setInt(4, game.getTotal_sessions()); // Establece el total de sesiones
            // Convertir LocalDateTime a Timestamp antes de pasarlo al PreparedStatement
            stmt.setTimestamp(5, game.getLast_session() != null ? Timestamp.valueOf(game.getLast_session()) : null);
            stmt.executeUpdate(); // Ejecuta la consulta de inserción
            System.out.println("Videojuego guardado exitosamente");
        } catch (SQLException e) {
            System.err.println("Error al guardar el videojuego: " + e.getMessage());
            e.printStackTrace(); // Para depuración
        }
    }

    @Override
    public Videogame getGameById(int id) {
        Videogame game = null;
        String sql = "SELECT * FROM Videogames WHERE game_id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id); // Establece el ID del videojuego a buscar
            ResultSet rs = stmt.executeQuery(); // Ejecuta la consulta

            if (rs.next()) {
                // Si existe un videojuego con el ID dado, se crea un objeto Videogame
                game = new Videogame(
                        // Obtiene el game_id
                        rs.getString("isbn"), // Obtiene el ISBN
                        rs.getString("title"), // Obtiene el título
                        rs.getInt("player_count"), // Obtiene la cantidad de jugadores
                        rs.getInt("total_sessions"), // Obtiene el total de sesiones
                        rs.getTimestamp("last_session").toLocalDateTime() // Obtiene la fecha de la última sesión
                );
                game.setId(rs.getInt("game_id"));
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener el videojuego: " + e.getMessage());
            e.printStackTrace(); // Para depuración
        }

        return game;
    }

    @Override
    public void deleteGameById(int id) {
        String sql = "DELETE FROM Videogames WHERE game_id = ?";  // Consulta para eliminar el videojuego

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {  // Usa 'connection' ya que ya está definida
            stmt.setInt(1, id);  // Establece el ID del videojuego a eliminar
            int rowsAffected = stmt.executeUpdate();  // Ejecuta la consulta de eliminación

            if (rowsAffected > 0) {
                System.out.println("El videojuego con ID " + id + " ha sido eliminado.");
            } else {
                System.out.println("No se encontró un videojuego con ID " + id + " para eliminar.");
            }
        } catch (SQLException e) {
            System.out.println("Error al eliminar el videojuego: " + e.getMessage());
            e.printStackTrace();  // Para depuración
        }
    }

    @Override
    public Player getPlayerById(int id) {
        Player player = null;
        String sql = "SELECT * FROM Players WHERE player_id = ?";  // Consulta para obtener un jugador por su ID

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {  // Usa 'connection' ya que ya está definida
            stmt.setInt(1, id);  // Establece el ID del jugador a buscar
            ResultSet rs = stmt.executeQuery();  // Ejecuta la consulta

            if (rs.next()) {  // Si hay un resultado
                player = new Player(
                        rs.getInt("player_id"), // Obtener el ID del jugador
                        rs.getString("nick_name"), // Obtener el nombre del jugador
                        rs.getInt("experience"), // Obtener la experiencia
                        rs.getInt("life_level"), // Obtener el nivel de vida
                        rs.getInt("coins"), // Obtener la cantidad de monedas
                        rs.getInt("session_count"), // Obtener la cantidad de sesiones
                        rs.getTimestamp("last_login").toLocalDateTime() // Obtener la última fecha de sesión y convertirla a LocalDateTime
                );
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener el jugador: " + e.getMessage());
            e.printStackTrace();  // Para depuración
        }

        return player;  // Retorna el jugador encontrado, o null si no se encuentra
    }

    @Override
    public void updatePlayer(Player plr) {
        String sql = "UPDATE Players SET nick_name = ?, experience = ?, life_level = ?, coins = ?, session_count = ?, last_login = ? WHERE player_id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {  // Usar 'connection' en vez de 'conexion'
            stmt.setString(1, plr.getNick_name());  // Establecer el nombre del jugador
            stmt.setInt(2, plr.getExperience());    // Establecer la experiencia del jugador
            stmt.setInt(3, plr.getLife_level());    // Establecer el nivel de vida del jugador
            stmt.setInt(4, plr.getCoins());         // Establecer la cantidad de monedas
            stmt.setInt(5, plr.getSession_count()); // Establecer la cantidad de sesiones
            stmt.setTimestamp(6, java.sql.Timestamp.valueOf(plr.getLast_login())); // Establecer la última fecha de sesión
            stmt.setInt(7, plr.getId());            // Establecer el ID del jugador para la condición WHERE

            stmt.executeUpdate();  // Ejecutar la actualización
        } catch (SQLException e) {
            System.out.println("Error al actualizar el jugador: " + e.getMessage());
            e.printStackTrace();  // Para depuración si ocurre un error
        }
    }

    @Override
    public void deletePlayer(Player plr) {
        String sql = "DELETE FROM Players WHERE player_id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {  // Usar 'connection' en vez de 'conexion'
            stmt.setInt(1, plr.getId());  // Establecer el ID del jugador a eliminar
            stmt.executeUpdate();  // Ejecutar la eliminación
        } catch (SQLException e) {
            System.out.println("Error al eliminar el jugador: " + e.getMessage());
            e.printStackTrace();  // Para depuración en caso de error
        }
    }

    @Override
    public ArrayList<Player> getAllPlayers() {
        ArrayList<Player> players = new ArrayList<>();
        String sql = "SELECT * FROM Players";  // Consulta SQL para obtener todos los jugadores

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {  // Usar 'connection' en vez de 'conexion'
            ResultSet rs = stmt.executeQuery();  // Ejecutar la consulta

            while (rs.next()) {  // Iterar sobre los resultados
                // Comprobar si 'last_login' es NULL antes de convertirlo
                Timestamp lastLoginTimestamp = rs.getTimestamp("last_login");
                LocalDateTime lastLogin = null;
                if (lastLoginTimestamp != null) {
                    lastLogin = lastLoginTimestamp.toLocalDateTime();
                }

                // Crear el jugador con la fecha de login que puede ser null
                Player player = new Player(
                        rs.getInt("player_id"), // Nombre de la columna "player_id"
                        rs.getString("nick_name"), // Nombre de la columna "nick_name"
                        rs.getInt("experience"), // Nombre de la columna "experience"
                        rs.getInt("life_level"), // Nombre de la columna "life_level"
                        rs.getInt("coins"), // Nombre de la columna "coins"
                        rs.getInt("session_count"), // Nombre de la columna "session_count"
                        lastLogin // Asignar la fecha de login, que puede ser null
                );
                players.add(player);  // Agregar el jugador a la lista
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener todos los jugadores: " + e.getMessage());
            e.printStackTrace();  // Para depuración en caso de error
        }

        return players;  // Devolver la lista de jugadores
    }

    @Override
    public void updateGame(Game game) {
        String sql = "UPDATE Games SET player_id = ?, experience = ?, life_level = ?, coins = ?, session_date = ? WHERE game_id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {  // Usamos 'connection' en vez de 'conexion'
            stmt.setInt(1, game.getPlayer_id());             // Establecer el ID del jugador
            stmt.setInt(2, game.getExperience());            // Establecer la experiencia
            stmt.setInt(3, game.getLife_level());            // Establecer el nivel de vida
            stmt.setInt(4, game.getCoins());                 // Establecer la cantidad de monedas
            stmt.setTimestamp(5, java.sql.Timestamp.valueOf(game.getSession_date()));  // Establecer la fecha de la sesión
            stmt.setInt(6, game.getGame_id());               // Establecer el ID del juego

            stmt.executeUpdate();  // Ejecutar la actualización en la base de datos
        } catch (SQLException e) {
            System.out.println("Error al actualizar el juego: " + e.getMessage());
            e.printStackTrace();  // Para depuración
        }
    }

    @Override
    public ArrayList<Game> getAllGames() {
        ArrayList<Game> games = new ArrayList<>();
        String sql = "SELECT * FROM Games";  // La consulta SQL para obtener todos los juegos

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {  // Usamos 'connection' en vez de 'conexion'
            ResultSet rs = stmt.executeQuery();  // Ejecutamos la consulta

            while (rs.next()) {  // Iteramos sobre los resultados
                Game game = new Game(
                        rs.getInt("session_id"), // Establecer el ID de la sesión
                        rs.getInt("player_id"), // Establecer el ID del jugador
                        rs.getInt("experience"), // Establecer la experiencia
                        rs.getInt("life_level"), // Establecer el nivel de vida
                        rs.getInt("coins"), // Establecer las monedas
                        rs.getTimestamp("session_date").toLocalDateTime() // Establecer la fecha de la sesión
                );
                game.setGame_id(rs.getInt("game_id"));   // Establecer el ID del juego (si es necesario)
                games.add(game);  // Añadir el juego a la lista
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener todos los juegos: " + e.getMessage());
            e.printStackTrace();  // Para depuración
        }

        return games;  // Retornar la lista de juegos
    }

    @Override
    public void deleteVideogameById(int id) {
        String sql = "DELETE FROM Videogames WHERE game_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
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

    @Override
    public void deletePlayerById(int id) {
        String sql = "DELETE FROM Players WHERE player_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);  // Establece el ID del jugador que quieres eliminar
            int rowsAffected = stmt.executeUpdate();
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

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            // Establecer los valores de los parámetros en la consulta SQL
            stmt.setString(1, plr.getNick_name());          // Nombre del jugador
            stmt.setInt(2, plr.getExperience());            // Experiencia del jugador
            stmt.setInt(3, plr.getLife_level());            // Nivel de vida del jugador
            stmt.setInt(4, plr.getCoins());                 // Monedas del jugador
            stmt.setInt(5, plr.getSession_count());         // Cantidad de sesiones
            if (plr.getLast_login() != null) {
                stmt.setTimestamp(6, Timestamp.valueOf(plr.getLast_login()));
            } else {
                stmt.setNull(6, java.sql.Types.TIMESTAMP);  // Se pasa null si last_login es null
            } // Último inicio de sesión

            // Ejecutar la inserción
            stmt.executeUpdate();
            System.out.println("Jugador guardado exitosamente.");
        } catch (SQLException e) {
            System.err.println("Error al guardar el jugador: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public ArrayList<Videogame> getAllVideogames() {
        ArrayList<Videogame> videogames = new ArrayList<>();
        String sql = "SELECT * FROM Videogames";  // Consulta SQL para obtener todos los videojuegos

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {  // Usamos 'connection' en vez de 'conexion'
            ResultSet rs = stmt.executeQuery();  // Ejecutamos la consulta

            while (rs.next()) {  // Iteramos sobre los resultados
                // Si 'last_session' es NULL, asignamos un valor por defecto (puede ser 'null' o alguna fecha específica)
                Timestamp lastSessionTimestamp = rs.getTimestamp("last_session");
                LocalDateTime lastSession = (lastSessionTimestamp != null) ? lastSessionTimestamp.toLocalDateTime() : null;

                Videogame videogame = new Videogame(
                        rs.getString("isbn"), // Establecer el ISBN del videojuego
                        rs.getString("title"), // Establecer el título del videojuego
                        rs.getInt("player_count"), // Establecer la cantidad de jugadores
                        rs.getInt("total_sessions"), // Establecer el total de sesiones
                        lastSession // Establecer la fecha de la última sesión, que puede ser null
                );
                videogame.setId(rs.getInt("game_id"));   // Establecer el ID del videojuego
                videogames.add(videogame);  // Añadir el videojuego a la lista
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener todos los videojuegos: " + e.getMessage());
            e.printStackTrace();  // Para depuración
        }

        return videogames;  // Retornar la lista de videojuegos
    }

    @Override
    public void updateVideogame(Videogame game) {
        String sql = "UPDATE Videogames SET isbn = ?, title = ?, player_count = ?, total_sessions = ?, last_session = ? WHERE game_id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            // Establecer los parámetros de la consulta
            stmt.setString(1, game.getIsbn());              // Establecer el ISBN del videojuego
            stmt.setString(2, game.getTitle());              // Establecer el título del videojuego
            stmt.setInt(3, game.getPlayer_count());          // Establecer la cantidad de jugadores
            stmt.setInt(4, game.getTotal_sessions());       // Establecer el total de sesiones
            stmt.setTimestamp(5, game.getLast_session() != null ? Timestamp.valueOf(game.getLast_session()) : null); // Establecer la fecha de la última sesión
            stmt.setInt(6, game.getId());                   // Establecer el ID del videojuego para la condición WHERE

            // Ejecutar la actualización
            stmt.executeUpdate();
            System.out.println("Videojuego actualizado correctamente.");
        } catch (SQLException e) {
            System.out.println("Error al actualizar el videojuego: " + e.getMessage());
            e.printStackTrace();  // Para depuración
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
                        resultado.getInt("game_id"), // ID del juego
                        resultado.getInt("player_id"), // ID del jugador
                        resultado.getInt("experience"), // Experiencia acumulada
                        resultado.getInt("life_level"), // Nivel de vida
                        resultado.getInt("coins"), // Monedas acumuladas
                        resultado.getTimestamp("session_date").toLocalDateTime() // Fecha y hora de la sesión
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
                        resultado.getTimestamp("last_login").toLocalDateTime()
                );
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener el jugador: " + e.getMessage());
        }
        return player;
    }

}
