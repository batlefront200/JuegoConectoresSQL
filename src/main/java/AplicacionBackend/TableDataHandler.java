
package AplicacionBackend;

import daos.RemoteDAO;

public interface TableDataHandler {
    String[] getColumnNames();
    String[][] getData(RemoteDAO controller);
}
