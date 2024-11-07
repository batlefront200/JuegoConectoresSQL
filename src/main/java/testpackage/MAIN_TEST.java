
package testpackage;

import daos.Factory;
import daos.RemoteDAO;

public class MAIN_TEST {
    public static void main(String[] args) {
        RemoteDAO remote = new Factory("MySQL").createRemoteDAO();
    }
}
