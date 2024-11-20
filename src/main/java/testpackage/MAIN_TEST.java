
package testpackage;

import PitufoBros.GameEngine;
import PitufoBros.GameThreadClass;
import daos.Factory;
import daos.RemoteDAO;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class MAIN_TEST {
    public static void main(String[] args) {
        new GameThreadClass().startGame();
    }
}
