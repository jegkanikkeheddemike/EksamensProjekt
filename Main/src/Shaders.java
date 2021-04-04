import java.util.ArrayList;

import processing.opengl.PShader;

public class Shaders {
    static PShader zombieFOVConeShader;
    static ArrayList<Zombie> zombies = new ArrayList<Zombie>();
    static ArrayList<Wall> walls = new ArrayList<Wall>();

    static void loadShaders() {
        zombieFOVConeShader = Main.main.loadShader("Shaders\\ZombieViewCone.frag");
    }

    static void drawZombieFOVCone() {
        
        // MAX AMOUNT OF ZOMBIES ON SCREEN IS 30 FOR NOW
        float[] zombieX = new float[30];
        float[] zombieY = new float[30];
        float[] zombieRotation = new float[30];
        float[] wallX = new float[100];
        float[] wallY = new float[100];
        float[] wallWidth = new float[100];
        float[] wallHeight = new float[100];

        for (int i = 0; i < zombies.size(); i++) {
            Zombie z = zombies.get(i);
            zombieX[i] = z.middleX();
            zombieY[i] = z.middleY();
            zombieRotation[i] = z.rotation;
        }

        for (int i = 0; i < walls.size(); i++) {
            Wall w = walls.get(i);
            wallX[i] = w.x;
            wallY[i] = w.y;
            wallWidth[i] = w.w;
            wallHeight[i] = w.h;
        }

        zombieFOVConeShader.set("zombies", zombies.size());
        zombieFOVConeShader.set("walls", walls.size());
        zombieFOVConeShader.set("camX", Main.player.middleX());
        zombieFOVConeShader.set("camY", Main.player.middleY());
        zombieFOVConeShader.set("zombieX", zombieX);
        zombieFOVConeShader.set("zombieY", zombieY);
        zombieFOVConeShader.set("zombieRotation", zombieRotation);
        zombieFOVConeShader.set("wallX", wallX);
        zombieFOVConeShader.set("wallY", wallY);
        zombieFOVConeShader.set("wallWidth", wallWidth);
        zombieFOVConeShader.set("wallHeight", wallHeight);

        // DET ER MEGET VIGTIGT AT DER IKKE ER NOFILL(), FOR SÅ VIRKER SHADEREN IKKE!!!
        // TILFØJ NOSTROKE ellers Kommer der stroke på hjørnerne af shaderen

        Main.main.fill(255);
        Main.main.noStroke();
        
        Main.main.shader(zombieFOVConeShader);

        // SHADER RECT DÆKKER HELE SKÆRMEN
        Main.main.rect(Main.player.middleX() - 1920 / 2, Main.player.middleY() - 1080 / 2, 1920, 1080);

        // HUST AT RESET SHADER BAGEFTER, ELLER FÅR ANDRE TEXTURES FORKERT SHADER :( !!
        Main.main.resetShader();
    }
}

// Den her thread forbereder de lister som shaderne skal bruge, da det kan tage
// lang tid gøres det imens der bliver kørt step!
class ShaderPreRenderWorkThread extends Thread {
    static volatile boolean beginWork = false;
    static ShaderPreRenderWorkThread thread = new ShaderPreRenderWorkThread();

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
}