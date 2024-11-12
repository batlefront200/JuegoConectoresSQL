
package daos;

/* Implementar métodos savePlayerProgress(), updatePlayerProgress(), 
getTopPlayers(), y getGameStats() para la gestión del progreso y estadísticas 
de videojuegos. */

import clases.Players;
import clases.Videogames;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;

public class MySQL implements RemoteDAO {
    private Connection conexion;
    
    public MySQL(String url, String user, String password) {
        try {
            //Class.forName("com.mysql.cj.jdbc.Driver");
            this.conexion = DriverManager.getConnection(url, user, password);
        } catch(SQLException e) {
            System.out.println("No se pudo realizar la conexion MySQL");
            System.out.println(e);
        } /*catch(ClassNotFoundException e) {
            System.out.println("No se pudo encontrar el plugin de MySQL");
            System.out.println(e);
        }*/
    }

    @Override
    public void savePlayerProgress(Videogames game, Players plr) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void updatePlayerProgress(Videogames game, Players plr) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public ArrayList<Players> getTopPlayers(Videogames game) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public ArrayList<Players> getGameStats(Videogames game, Players plr) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
}
