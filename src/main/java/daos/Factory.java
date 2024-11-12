
package daos;

public class Factory {

    private String dbType; // "SQLite", "JSON", "XML", "MySQL", "MSQL"

    public Factory(String dbType) {
        this.dbType = dbType;
    }

    /*public LocalDAO createLocalDAO() {
        if (dbType.equals("SQLite")) {
            return new SQLiteDAO("localDB.db");
        } else if (dbType.equals("JSON")) {
            return new JSONDAO();
        } else if (dbType.equals("XML")) {
            return new XMLDAO();
        }
        throw new IllegalArgumentException("Unsupported Local DAO type");
    }*/

    public RemoteDAO createRemoteDAO() {
        if (dbType.equals("MySQL")) {
            return new MySQL("jdbc:mysql://localhost:3306/db", "root", "");
        } else if (dbType.equals("MSQL")) {
            return new MSQL("jdbc:sqlserver://localhost:5432/db", "user", "password");
        }
        throw new IllegalArgumentException("Unsupported Remote DAO type");
    }
}
