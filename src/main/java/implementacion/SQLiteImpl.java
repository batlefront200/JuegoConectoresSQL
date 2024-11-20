package implementacion;

import clases.Player;
import daos.SQLiteDAO;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SQLiteImpl implements SQLiteDAO {

    private boolean soundEnabled;
    private String resolution, language;
    // Driver  
    private String driver = "org.sqlite.JDBC";
    // Cargar el driver

    // Establecer conexi�n
    private String url = "jdbc:sqlite:.\\datosLocales\\datosLocales.db";		//

    @Override
    public boolean saveConfig() {
        try {
            Class.forName(driver);
        } catch (ClassNotFoundException e) {
            System.out.println("Controlador JDBC de SQLite no encontrado: " + e.toString());
        }
        try (Connection conn = DriverManager.getConnection(url)) {
            String sql = "INSERT INTO configuracion_jugador (sound_enabled, resolution, language) VALUES (?, ?, ?)";
            PreparedStatement sentencia = conn.prepareStatement(sql);
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
        try (Connection conn = DriverManager.getConnection(url)) {
            String sql = "UPDATE configuracion_jugador SET sound_enabled = ?, resolution = ?, language = ?";
            PreparedStatement sentencia = conn.prepareStatement(sql);

            // Assuming the `datosAct` array has the same order as the table columns
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
        try (Connection conn = DriverManager.getConnection(url)) {
            String sql = "SELECT * FROM configuracion_jugador ORDER BY contador DESC LIMIT 1";
            PreparedStatement sentencia = conn.prepareStatement(sql);
            ResultSet rs = sentencia.executeQuery();

            if (rs.next()) {
                datosConfig[0] = rs.getString("sound_Enabled").toString();
                datosConfig[1] = rs.getString("resolution");
                datosConfig[2] = rs.getString("language");
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
        try (Connection conn = DriverManager.getConnection(url)) {
            String sql = "INSERT INTO EstadoJugador (player_id, nick_name, experience, life_level, coins, session_count, last_login) "
                    + "VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement sentencia = conn.prepareStatement(sql);
            sentencia.setInt(1, plr.getId());
            sentencia.setString(1, plr.getNick_name());
            sentencia.setInt(2, plr.getExperience());
            sentencia.setInt(3, plr.getLife_level());
            sentencia.setInt(4, plr.getCoins());
            sentencia.setInt(5, plr.getSession_count());
            sentencia.setString(6, plr.getLast_login().toString());

            int rowsInserted = sentencia.executeUpdate();
            return rowsInserted > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean updatePlayerState(Player plr) {
        try (Connection conn = DriverManager.getConnection(url)) {
            String sql = "UPDATE EstadoJugador SET experience = ?, life_level = ?, coins = ?, session_count = ?, last_login = ? "
                    + "WHERE player_id = ?";
            PreparedStatement sentencia = conn.prepareStatement(sql);
            sentencia.setInt(1, plr.getExperience());
            sentencia.setInt(2, plr.getLife_level());
            sentencia.setInt(3, plr.getCoins());
            sentencia.setInt(4, plr.getSession_count());
            sentencia.setString(5, plr.getLast_login().toString());

            int rowsUpdated = sentencia.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public String[] getPlayerState(int playerId) {
        String[] playerState = new String[6]; // Seis columnas relevantes
        try (Connection conn = DriverManager.getConnection(url)) {
            String sql = "SELECT nick_name, experience, life_level, coins, session_count, last_login "
                    + "FROM EstadoJugador WHERE player_id = ?";
            PreparedStatement sentencia = conn.prepareStatement(sql);
            sentencia.setInt(1, playerId);
            ResultSet rs = sentencia.executeQuery();

            if (rs.next()) {
                playerState[0] = rs.getString("nick_name");
                playerState[1] = String.valueOf(rs.getInt("experience"));
                playerState[2] = String.valueOf(rs.getInt("life_level"));
                playerState[3] = String.valueOf(rs.getInt("coins"));
                playerState[4] = String.valueOf(rs.getInt("session_count"));
                playerState[5] = rs.getString("last_login");
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
        try (Connection conn = DriverManager.getConnection(url)) {
            String sql = "DELETE FROM EstadoJugador WHERE player_id = ?";
            PreparedStatement sentencia = conn.prepareStatement(sql);
            sentencia.setInt(1, playerId);

            int rowsDeleted = sentencia.executeUpdate();
            return rowsDeleted > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

}
