

import java.util.ArrayList;



public class NearThread extends Thread {
    public static ArrayList<GameObject> nearObjectsUpdated = new ArrayList<GameObject>();
    public static boolean isReady = false;
    public static NearThread thread = new NearThread();

    @Override
    public void run() {
        while (Main.isRunning) {
            isReady = false;
            nearObjectsUpdated.clear();
            for (GameObject gameObject : Main.allObjects) {
                if (GameMath.objectDistance(gameObject, Main.player) < 2000) {
                    nearObjectsUpdated.add(gameObject);
                }
            }
            isReady = true;
            try {
                sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}