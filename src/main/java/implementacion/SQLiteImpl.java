package implementacion;

import clases.Player;
import daos.SQLiteDAO;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class SQLiteImpl implements SQLiteDAO {

    private boolean soundEnabled;
    private String resolution, language;
    // Driver  
    private String driver = "org.sqlite.JDBC";
    private Connection conexion;

    public SQLiteImpl() {
        try {
            Class.forName(driver);

            this.conexion = DriverManager.getConnection("jdbc:sqlite:.\\src\\main\\java\\datosLocales\\datosLocales2.db");
            System.out.println("Conectado a SQLite");
        } catch (ClassNotFoundException e) {
            System.out.println("Driver JDBC de SQLite no encontrado: " + e.getMessage());
        } catch (SQLException e) {
            System.out.println("No se pudo realizar la conexión en SQLite: " + e.getMessage());
        }
    }

    @Override
    public boolean saveConfig() {
        try (PreparedStatement sentencia = conexion.prepareStatement(
                "INSERT INTO configuracion_jugador (sound_enabled, resolution, language) VALUES (?, ?, ?)")) {
            sentencia.setBoolean(1, soundEnabled);
            sentencia.setString(2, resolution);
            sentencia.setString(3, language);
            int rowsInserted = sentencia.executeUpdate();
            return rowsInserted > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean updateConfig(String[] datosAct) {
        try (PreparedStatement sentencia = conexion.prepareStatement(
                "UPDATE configuracion_jugador SET sound_enabled = ?, resolution = ?, language = ?")) {
            sentencia.setBoolean(1, Boolean.parseBoolean(datosAct[0]));
            sentencia.setString(2, datosAct[1]);
            sentencia.setString(3, datosAct[2]);

            int rowsUpdated = sentencia.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public String[] getConfig() {
        String[] datosConfig = new String[3];
        try (PreparedStatement sentencia = conexion.prepareStatement(
                "SELECT * FROM configuracion_jugador ORDER BY contador DESC LIMIT 1")) {
            ResultSet resultado = sentencia.executeQuery();

            if (resultado.next()) {
                datosConfig[0] = resultado.getString("sound_Enabled").toString();
                datosConfig[1] = resultado.getString("resolution");
                datosConfig[2] = resultado.getString("language");
            } else {
                System.out.println("No hay datos de configuracion");
            }
            return datosConfig;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean savePlayerState(Player plr) {
        try (PreparedStatement sentencia = conexion.prepareStatement(
                "INSERT INTO EstadoJugador (player_id, nick_name, experience, life_level, coins, session_count, last_login) "
                + "VALUES (?, ?, ?, ?, ?, ?,?)")) {
            sentencia.setInt(1, plr.getId());
            sentencia.setString(2, plr.getNick_name());
            sentencia.setInt(3, plr.getExperience());
            sentencia.setInt(4, plr.getLife_level());
            sentencia.setInt(5, plr.getCoins());
            sentencia.setInt(6, plr.getSession_count());
            sentencia.setString(7, plr.getLast_login().toString());

            int rowsInserted = sentencia.executeUpdate();
            return rowsInserted > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public String[] getPlayerState(int playerId) {
        String[] playerState = new String[6]; 
        try (PreparedStatement sentencia = conexion.prepareStatement(
                "SELECT nick_name, experience, life_level, coins, session_count, last_login "
                + "FROM EstadoJugador WHERE player_id = ?")) {
            sentencia.setInt(1, playerId);
            ResultSet resultado = sentencia.executeQuery();

            if (resultado.next()) {
                playerState[0] = resultado.getString("nick_name");
                playerState[1] = String.valueOf(resultado.getInt("experience"));
                playerState[2] = String.valueOf(resultado.getInt("life_level"));
                playerState[3] = String.valueOf(resultado.getInt("coins"));
                playerState[4] = String.valueOf(resultado.getInt("session_count"));
                playerState[5] = resultado.getString("last_login");
            } else {
                System.out.println("Jugador con ID " + playerId + " no encontrado.");
                return null;
            }
            return playerState;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean deletePlayerState(int playerId) {
        try (PreparedStatement sentencia = conexion.prepareStatement(
                "DELETE FROM EstadoJugador WHERE player_id = ?")) {
            sentencia.setInt(1, playerId);

            int rowsDeleted = sentencia.executeUpdate();
            return rowsDeleted > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Player getPlayerByNickname(String nickname) {
        try (PreparedStatement sentencia = conexion.prepareStatement(
                "SELECT player_id, nick_name, experience, life_level, coins, session_count, last_login "
                + "FROM EstadoJugador WHERE nick_name = ?")) {
            sentencia.setString(1, nickname);
            ResultSet resultado = sentencia.executeQuery();

            if (resultado.next()) {
                // Crear un objeto Jugador con los datos obtenidos
                return new Player(
                        resultado.getInt("player_id"),
                        resultado.getString("nick_name"),
                        resultado.getInt("experience"),
                        resultado.getInt("life_level"),
                        resultado.getInt("coins"),
                        resultado.getInt("session_count"),
                        LocalDateTime.parse(resultado.getString("last_login"))
                );
            } else {
                System.out.println("Jugador con nickname " + nickname + " no encontrado.");
                return null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean updatePlayerState(Player jugador) {
        try {
       
            String checkSql = "SELECT player_id FROM EstadoJugador WHERE nick_name = ?";
            PreparedStatement checkStmt = conexion.prepareStatement(checkSql);
            checkStmt.setString(1, jugador.getNick_name());
            ResultSet resultado = checkStmt.executeQuery();

            if (!resultado.next()) {
                savePlayerState(jugador);
            }

          
            String sql = "UPDATE EstadoJugador SET experience = ?, life_level = ?, coins = ?, session_count = ?, last_login = ? "
                    + "WHERE player_id = ?";
            PreparedStatement sentencia = conexion.prepareStatement(sql);
            sentencia.setInt(1, jugador.getExperience());
            sentencia.setInt(2, jugador.getLife_level());
            sentencia.setInt(3, jugador.getCoins());
            sentencia.setInt(4, jugador.getSession_count());
            sentencia.setString(5, jugador.getLast_login().toString());
            sentencia.setInt(6, resultado.getInt("player_id"));

            int rowsUpdated = sentencia.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

}
