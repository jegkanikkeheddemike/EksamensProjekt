package Threads;

import java.util.ArrayList;

import Framework.GameMath;
import Framework.GameObject;
import Framework.Shaders;
import GameObjects.Wall;
import GameObjects.Zombie;
import Setup.Main;

public class NearThread extends Thread {
    public static volatile ArrayList<GameObject> nearObjectsUpdated = new ArrayList<GameObject>();
    public static volatile boolean isReady = false;
    public static volatile NearThread thread = new NearThread();

    public static final int nearDist = 1000;
    public static volatile int roundsCompleted = 0;

    private static boolean wait = false;

    @Override
    public void run() {
        while (Main.isRunning) {
            int timeWaited = 0;
            while (wait && timeWaited < 100) {
                try {
                    Thread.sleep(0, 1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            if (timeWaited > 100)
                System.out.println(
                        "NearThread waited for too long and began again. Check if you forgot to call endWait(). If not increase waittime?");

            isReady = false;
            nearObjectsUpdated.clear();
            Shaders.zombies.clear();
            Shaders.walls.clear();

            for (int i = 0; i < Main.allObjects.size(); i++) {
                GameObject gameObject = Main.allObjects.get(i);
                if (
                    GameMath.pointDistance(gameObject.x, gameObject.y, Main.player.x, Main.player.y) < nearDist || 
                    GameMath.pointDistance(gameObject.x + gameObject.w, gameObject.y + gameObject.h, Main.player.x + Main.player.w, Main.player.y + Main.player.h) < nearDist ||
                     (gameObject.x < Main.player.x && gameObject.x+gameObject.w > Main.player.x && Math.abs(Main.player.y - gameObject.y) < nearDist) ||
                     (gameObject.y < Main.player.y && gameObject.y + gameObject.h > Main.player.y && Math.abs(Main.player.x - gameObject.x) < nearDist)) {

                    nearObjectsUpdated.add(gameObject);

                    if (gameObject.classID == "Wall") {
                        if (Shaders.walls.size() < Shaders.wallShaderAmount) {
                            Shaders.walls.add((Wall) gameObject);
                        } else {
                            System.out.println("Too may nearby walls. Max: " + Shaders.wallShaderAmount);
                        }
                    } else if (gameObject.classID == "Zombie") {
                        if (Shaders.zombies.size() < Shaders.wallShaderAmount) {
                            Shaders.zombies.add((Zombie) gameObject);
                        } else {
                            System.out.println("Too may nearby zombies. Max: " + Shaders.zombieShaderAmount);
                        }
                    }
                }
            }
            isReady = true;
            roundsCompleted++;
            try {
                sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void beginWait() {
        wait = true;
    }

    public static void endWait() {
        wait = false;
    }

}