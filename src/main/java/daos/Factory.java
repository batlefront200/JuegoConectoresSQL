
package daos;

public class Factory {

    private String dbType; // "SQLite", "JSON", "XML", "MySQL", "MSQL"

    public Factory(String dbType) {
        this.dbType = dbType;
    }

    public SQLiteDAO createSQLiteDAO() {
      return new implementacion.SQLiteImpl();
    }

    public RemoteDAO createRemoteDAO() {
        if (dbType.equals("MySQL")) {
            return new MySQL("jdbc:mysql://localhost:3306/gamesql", "root", "");
        } else if (dbType.equals("Postgres")) {
            return new Postgres("jdbc:postgresql://localhost:5432/gamesql?user=postgres&password=admin");
        }
        throw new IllegalArgumentException("Unsupported Remote DAO type");
    }
}

