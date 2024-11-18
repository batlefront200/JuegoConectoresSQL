
package implementacion;

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
    private String url = "jdbc:sqlite:D:\\Kevin\\Entornos\\UD2\\sqlite-tools-win-x64-3460100\\datosLocales.db";		//

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
}
