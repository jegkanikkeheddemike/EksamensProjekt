// Den her thread forbereder de lister som shaderne skal bruge, da det kan tage
// lang tid gøres det imens der bliver kørt step!
package Threads;

import Framework.*;
import GameObjects.*;
import Setup.Main;

public class ShaderPreRenderWorkThread extends Thread {
    private static volatile boolean beginWork = false;
    public static ShaderPreRenderWorkThread thread = new ShaderPreRenderWorkThread();

    @Override
    public void run() {
        while (Main.isRunning) {
            if (beginWork) {

                Shaders.zombies.clear();
                Shaders.walls.clear();
                // LOAD WALLS AND ZOMBIES
                for (int i = 0; i < Main.nearObjects.size(); i++) {
                    if (Main.nearObjects.get(i).classID == "Wall") {
                        Shaders.walls.add((Wall) Main.nearObjects.get(i));
                    } else if (Main.nearObjects.get(i).classID == "Zombie") {
                        if (Shaders.zombies.size() >= 30) {
                            System.out.println(
                                    "Failed to push zombies to shader because there were too many zombies (max 30)");
                            continue;
                        }
                        Shaders.zombies.add((Zombie) Main.nearObjects.get(i));
                    }
                }
                // ONLY RUNS ONCE EVERYTIME BEGINWORK IS MADE TRUE!
                beginWork = false;
            }
        }
    }

    public static void beginWork() {
        beginWork = true;
    }
}