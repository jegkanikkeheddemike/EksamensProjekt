package Threads;

import java.util.ArrayList;

import Framework.GameMath;
import Framework.GameObject;
import Setup.Main;

public class NearThread extends Thread {
    public static ArrayList<GameObject> nearObjectsUpdated = new ArrayList<GameObject>();
    public static volatile boolean isReady = false;
    public static NearThread thread = new NearThread();

    @Override
    public void run() {
        while (Main.isRunning) {
            isReady = false;
            nearObjectsUpdated.clear();

            for (int i = 0; i < Main.allObjects.size(); i++) {
                GameObject gameObject = Main.allObjects.get(i);
                if (GameMath.pointDistance(gameObject.x, gameObject.y, Main.player.x, Main.player.y) < 2000
                        || GameMath.pointDistance(gameObject.x + gameObject.w, gameObject.y + gameObject.h,
                                Main.player.x + Main.player.w, Main.player.y + Main.player.h) < 2000
                        || (gameObject.x > Main.player.middleX() && gameObject.x + gameObject.w < Main.player.middleX()
                                && Math.abs(gameObject.y - Main.player.y) < 2000)
                        || (gameObject.y < Main.player.middleY() && gameObject.y + gameObject.h > Main.player.middleY()
                                && Math.abs(gameObject.y - Main.player.y) < 2000)) {
                    nearObjectsUpdated.add(gameObject);
                }
            }
            isReady = true;
            try {
                sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}