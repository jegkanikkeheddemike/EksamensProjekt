package Framework.Threads;

import Framework.GameMath;
import Framework.GameObject;
import Setup.Main;

public class NearThread extends Thread {
    @Override
    public void run() {
        while(Main.isRunning){
            for (GameObject gameObject : Main.allObjects) {
                //if (GameMath.objectDistance(gameObject, Main.player)
            }
        }
    }
}
