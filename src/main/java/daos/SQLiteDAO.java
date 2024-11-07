
package daos;

public interface SQLiteDAO {
    public boolean saveConfig();
    public boolean updateConfig(String [] datosAct);
    public String [] getConfig();
}
