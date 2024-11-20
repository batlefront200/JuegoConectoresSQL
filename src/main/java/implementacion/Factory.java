
package implementacion;

import daos.RemoteDAO;
import daos.SQLiteDAO;
import daos.XmlDAO;

public class Factory {

    private String dbType; // "SQLite", "JSON", "XML", "MySQL", "MSQL"

    public Factory(String dbType) {
        this.dbType = dbType;
    }

    public SQLiteDAO createSQLiteDAO() {
      return new implementacion.SQLiteImpl();
    }
    
    public XmlDAO createXmlDAO() {
        return new implementacion.XmlImpl();
    }

    public RemoteDAO createRemoteDAO() {
        String[] data = new Factory("XML").createXmlDAO().getConfig();
        if (dbType.equals("MySQL")) {
            return new MySQL("jdbc:mysql://"+data[0]+":"+data[1]+"/gamesql", data[2], data[3]);
        } else if (dbType.equals("Postgres")) {
            return new Postgres("jdbc:postgresql://"+data[0]+":"+data[1]+"/gamesql?user="+data[2]+"&password="+data[3]);
        }
        throw new IllegalArgumentException("Unsupported Remote DAO type");
    }
}

