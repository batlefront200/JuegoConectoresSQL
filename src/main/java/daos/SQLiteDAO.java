
package daos;

public interface SQLiteDAO {
    public boolean saveConfig();
    public boolean updateConfig();
    public boolean getConfig();
    
    public boolean saveProgress();
    public boolean updateProgress();
    public boolean getProgress();
}
